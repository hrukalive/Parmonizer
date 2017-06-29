package com.base;

import com.common.Interval;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.validation.ChordScorer;
import com.validation.ChordValidator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * Created by NyLP on 6/12/17.
 */

public class Chord
{
    public class ChordRealization extends NoteCluster implements Comparable<ChordRealization>
    {
        private int loss = 0;
        public ChordRealization() { super(); }
        public ChordRealization(ChordRealization realization) { super(realization); this.loss = realization.loss; }

        @Override public ChordRealization addVoiceNew(Note note)
        {
            ChordRealization ret = new ChordRealization(this);
            ret.addVoice(Note.n(note));
            return ret;
        }

        public int getLoss()
        {
            return loss;
        }

        public void setLoss(int loss)
        {
            this.loss = loss;
        }

        @Override public int compareTo(ChordRealization o)
        {
            if (o == null) throw new NullPointerException("Null Chord to compare");
            if (loss > o.loss) return 1;
            if (loss < o.loss) return -1;
            return 0;
        }
    }

    public class ChordRealizationCollection
    {
        private ArrayList<ChordRealization> realizations = new ArrayList<>();
        private boolean validated = false, scored = false;
        private boolean sorted = false;

        public void add(ChordRealization r) { realizations.add(r); }

        public ArrayList<ChordRealization> getRealizations()
        {
            if (!validated || !scored)
                throw new InternalError("Chord Realizations not prepared.");
            if (!sorted)
            {
                Collections.sort(realizations);
                sorted = true;
            }
            return realizations;
        }

        public boolean isValidated()
        {
            return validated;
        }

        public boolean isScored()
        {
            return scored;
        }

        public boolean isSorted()
        {
            return sorted;
        }

        public void setValidated()
        {
            this.validated = true;
        }

        public void setScored()
        {
            this.scored = true;
        }

        public void resetFlag()
        {
            sorted = false;
            validated = false;
            scored = false;
        }

        public int size() { return realizations.size(); }

        public Iterator<ChordRealization> iterator() { return realizations.iterator(); }
    }

    private ArrayList<Note> noteSet = new ArrayList<Note>();
    private ArrayList<ArrayList<Interval>> tendencyIntv = new ArrayList<>();
    private ArrayList<ArrayList<Boolean>> tendencyDir = new ArrayList<>();
    private Note bass;

    private int inversion;
    private int voices;
    private Note[] lo;
    private Note[] hi;

    private ChordRealizationCollection collection = new ChordRealizationCollection();

    public static class Builder
    {
        private final ArrayList<Note> noteSet = new ArrayList<>();
        private final ArrayList<ArrayList<Interval>> tendencyIntv = new ArrayList<>();
        private final ArrayList<ArrayList<Boolean>> tendencyDir = new ArrayList<>();
        private Note bass;

        private int inversion = 0;
        private int voices = 4;
        private Note[] lo = new Note[]{Note.n("E2"), Note.n("B2"), Note.n("F3"), Note.n("C4")};
        private Note[] hi = new Note[]{Note.n("E4"), Note.n("A4"), Note.n("E5"), Note.n("C6")};

        public Builder(Note root, Interval intervals[])
        {
            this.noteSet.add(Note.n(root));
            tendencyIntv.add(new ArrayList<Interval>());
            tendencyDir.add(new ArrayList<Boolean>());
            for (int i = 0; i < intervals.length; i++)
            {
                Note temp = root.intervalAbove(intervals[i]);
                this.noteSet.add(temp);
                tendencyIntv.add(new ArrayList<Interval>());
                tendencyDir.add(new ArrayList<Boolean>());
            }
            this.bass = Note.n(root);
        }

        public Builder inversion(int inv)
        {
            this.inversion = inv;
            this.bass = Note.n(noteSet.get(inv));
            return this;
        }
        public Builder voices(int voices)
        {
            this.voices = voices;
            return this;
        }
        public Builder low(Note notes[])
        {
            if (notes.length < voices)
                throw new IllegalArgumentException("Range specification does not match #voices.");
            this.lo = notes;
            return this;
        }
        public Builder high(Note notes[])
        {
            if (notes.length < voices)
                throw new IllegalArgumentException("Range specification does not match #voices.");
            this.hi = notes;
            return this;
        }
        public Builder tendency(int voice, boolean above, Interval intv)
        {
            if (voice >= voices)
                throw new IllegalArgumentException("Voice required does not exist.");
            tendencyIntv.get(voice).add(intv);
            tendencyDir.get(voice).add(above);
            return this;
        }
        public Chord build()
        {
            return new Chord(this);
        }
    }

    private Chord(Builder builder)
    {
        noteSet = builder.noteSet;
        bass = builder.bass;

        inversion = builder.inversion;
        voices = builder.voices;
        lo = builder.lo;
        hi = builder.hi;

        tendencyDir = builder.tendencyDir;
        tendencyIntv = builder.tendencyIntv;

        yield();
    }

    public void yield()
    {
        collection = new ChordRealizationCollection();
        for (Note bassNote : bass.allInRange(lo[0], hi[0]))
        {
            ChordRealization temp = new ChordRealization();
            Note tempnote = Note.n(bassNote);
            for (int i = 0; i < tendencyDir.get(0).size(); i++)
            {
                if (tendencyDir.get(0).get(i))
                    tempnote.addTendency(tempnote.intervalAbove(tendencyIntv.get(0).get(i)));
                else
                    tempnote.addTendency(tempnote.intervalBelow(tendencyIntv.get(0).get(i)));
            }
            temp.addVoice(tempnote);
            yieldHelper(1, temp, lo, hi, voices);
        }
    }
    private void yieldHelper(int num, ChordRealization accum, Note[] lo, Note[] hi, int voices)
    {
        if (num == voices)
        {
            collection.add(accum);
        }
        else if (num < voices)
        {
            for (int i = 0; i < noteSet.size(); i++)
            {
                Note note_proto = noteSet.get(i);
                for (Note note : note_proto.allInRange(lo[num], hi[num]))
                {
                    for (int j = 0; j < tendencyDir.get(i).size(); j++)
                    {
                        if (tendencyDir.get(i).get(j))
                            note.addTendency(note.intervalAbove(tendencyIntv.get(i).get(j)));
                        else
                            note.addTendency(note.intervalBelow(tendencyIntv.get(i).get(j)));
                    }
                    if (note.compareTo(accum.highestNote()) >= 0)
                        yieldHelper(num + 1, accum.addVoiceNew(note), lo, hi, voices);
                }
            }
        }
    }

    public void applyValidation(ChordValidator validator)
    {
        if (collection.isValidated())
            yield();

        validator.validate(this);

        collection.setValidated();
    }

    public void applyScorer(ChordScorer scorer)
    {
        if (!collection.isValidated())
            throw new InternalError("Not validated before scoring.");

        scorer.score(this);

        collection.setScored();
    }

    public ArrayList getNoteSet()
    {
        return noteSet;
    }

    public ChordRealizationCollection getRealizations()
    {
        return collection;
    }

    public Note getBass()
    {
        return bass;
    }

    @Override public String toString()
    {
        return noteSet.toString();
    }
}
