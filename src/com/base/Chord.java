package com.base;

import com.common.Interval;
import com.validation.ChordScorer;
import com.validation.ChordValidator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Implemented the concept of a chord, with the functionality
 * to generate all possible configurations under certain restraints.
 * 
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
            ret.addVoice(Note.build(note));
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
        
        public void replace(int index, Note note)
        {
            cluster.remove(index);
            cluster.add(index, Note.build(note));
        }

        @Override public int compareTo(ChordRealization o)
        {
            if (o == null) throw new NullPointerException("Null Chord to compare");
            if (loss > o.loss) return 1;
            if (loss < o.loss) return -1;
            return 0;
        }
    }

    private ArrayList<Note> noteSet = new ArrayList<Note>();
    private ArrayList<ArrayList<Interval>> prepareIntv = new ArrayList<>();
    private ArrayList<ArrayList<Note.Dir>> prepareDir = new ArrayList<>();
    private ArrayList<ArrayList<Interval>> tendencyIntv = new ArrayList<>();
    private ArrayList<ArrayList<Note.Dir>> tendencyDir = new ArrayList<>();
    private ArrayList<ArrayList<Interval>> altTendencyIntv = new ArrayList<>();
    private ArrayList<ArrayList<Note.Dir>> altTendencyDir = new ArrayList<>();
    private ArrayList<ArrayList<Interval>> bonusIntv = new ArrayList<>();
    private ArrayList<ArrayList<Note.Dir>> bonusDir = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> bonusValue = new ArrayList<>();
    private Note bass;

    private int inversion;
    private int voices;
    private Note[] lo;
    private Note[] hi;
    private ChordValidator validator;
    private boolean validatorChanged = true;
    private ChordScorer scorer;
    private HashMap<Integer, Note> insistList = new HashMap<>();

    private ArrayList<ChordRealization> realizations = new ArrayList<>();

    public static class Builder
    {
        private final ArrayList<Note> noteSet = new ArrayList<>();
        private final ArrayList<ArrayList<Interval>> prepareIntv = new ArrayList<>();
        private final ArrayList<ArrayList<Note.Dir>> prepareDir = new ArrayList<>();
        private final ArrayList<ArrayList<Interval>> tendencyIntv = new ArrayList<>();
        private final ArrayList<ArrayList<Note.Dir>> tendencyDir = new ArrayList<>();
        private final ArrayList<ArrayList<Interval>> altTendencyIntv = new ArrayList<>();
        private final ArrayList<ArrayList<Note.Dir>> altTendencyDir = new ArrayList<>();
        private final ArrayList<ArrayList<Interval>> bonusIntv = new ArrayList<>();
        private final ArrayList<ArrayList<Note.Dir>> bonusDir = new ArrayList<>();
        private final ArrayList<ArrayList<Integer>> bonusValue = new ArrayList<>();
        private Note bass;

        private int inversion = 0;
        private int voices = 4;
        private Note[] lo = new Note[] {Note.build("E2"), Note.build("B2"), Note.build("F3"), Note.build("C4")};
        private Note[] hi = new Note[] {Note.build("E4"), Note.build("A4"), Note.build("E5"), Note.build("C6")};

        private ChordValidator validator = null;
        private ChordScorer scorer = null;

        public Builder(Note root, Interval intervals[])
        {
            this.noteSet.add(Note.build(root));
            for (int i = 0; i < intervals.length; i++)
                this.noteSet.add(root.intervalAbove(intervals[i]));
            
            for (int i = 0; i < noteSet.size(); i++)
            {
                prepareIntv.add(new ArrayList<>());
                prepareDir.add(new ArrayList<>());
                tendencyIntv.add(new ArrayList<>());
                tendencyDir.add(new ArrayList<>());
                altTendencyIntv.add(new ArrayList<>());
                altTendencyDir.add(new ArrayList<>());
                bonusIntv.add(new ArrayList<>());
                bonusDir.add(new ArrayList<>());
                bonusValue.add(new ArrayList<>());
            }
            
            this.bass = Note.build(root);
        }
        
        public Builder(Builder chord)
        {
            this.noteSet.addAll(chord.noteSet);
            for (int i = 0; i < chord.tendencyIntv.size(); i++)
            {
                prepareIntv.add(new ArrayList<>(chord.prepareIntv.get(i)));
                prepareDir.add(new ArrayList<>(chord.prepareDir.get(i)));
                tendencyDir.add(new ArrayList<>(chord.tendencyDir.get(i)));
                tendencyIntv.add(new ArrayList<>(chord.tendencyIntv.get(i)));
                altTendencyDir.add(new ArrayList<>(chord.altTendencyDir.get(i)));
                altTendencyIntv.add(new ArrayList<>(chord.altTendencyIntv.get(i)));
                bonusDir.add(new ArrayList<>(chord.bonusDir.get(i)));
                bonusIntv.add(new ArrayList<>(chord.bonusIntv.get(i)));
                bonusValue.add(new ArrayList<>(chord.bonusValue.get(i)));
            }
            this.bass = Note.build(chord.bass);
            this.inversion = chord.inversion;
            this.voices = chord.voices;
            this.lo = chord.lo;
            this.hi = chord.hi;
            this.validator = chord.validator;
            this.scorer = chord.scorer;
        }
        
        public Builder(Chord chord)
        {
            this.noteSet.addAll(chord.noteSet);
            for (int i = 0; i < chord.tendencyIntv.size(); i++)
            {
                prepareIntv.add(new ArrayList<>(chord.prepareIntv.get(i)));
                prepareDir.add(new ArrayList<>(chord.prepareDir.get(i)));
                tendencyDir.add(new ArrayList<>(chord.tendencyDir.get(i)));
                tendencyIntv.add(new ArrayList<>(chord.tendencyIntv.get(i)));
                altTendencyDir.add(new ArrayList<>(chord.altTendencyDir.get(i)));
                altTendencyIntv.add(new ArrayList<>(chord.altTendencyIntv.get(i)));
                bonusDir.add(new ArrayList<>(chord.bonusDir.get(i)));
                bonusIntv.add(new ArrayList<>(chord.bonusIntv.get(i)));
                bonusValue.add(new ArrayList<>(chord.bonusValue.get(i)));
            }
            this.bass = Note.build(chord.bass);
            this.inversion = chord.inversion;
            this.voices = chord.voices;
            this.lo = chord.lo;
            this.hi = chord.hi;
            this.validator = chord.validator;
            this.scorer = chord.scorer;
        }

        public Builder inversion(int inv)
        {
            this.inversion = inv;
            this.bass = Note.build(noteSet.get(inv));
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
        public Builder preparedBy(int chordTone, Note.Dir dir, Interval intv)
        {
            if (chordTone < 1 || chordTone > voices)
                throw new IllegalArgumentException("Voice chosen cannot have tendency.");
            prepareIntv.get(chordTone - 1).add(intv);
            prepareDir.get(chordTone - 1).add(dir);
            return this;
        }
        public Builder tendency(int chordTone, Note.Dir dir, Interval intv)
        {
            if (chordTone < 1 || chordTone > voices)
                throw new IllegalArgumentException("Voice chosen cannot have tendency.");
            // Bass is not to be driven by tendency
            tendencyIntv.get(chordTone - 1).add(intv);
            tendencyDir.get(chordTone - 1).add(dir);
            return this;
        }
        public Builder altTendency(int chordTone, Note.Dir dir, Interval intv)
        {
            if (chordTone < 1 || chordTone > voices)
                throw new IllegalArgumentException("Voice chosen cannot have tendency.");
            altTendencyIntv.get(chordTone - 1).add(intv);
            altTendencyDir.get(chordTone - 1).add(dir);
            return this;
        }
        public Builder bonus(int chordTone, Note.Dir dir, Interval intv, int bonus)
        {
            if (chordTone < 1 || chordTone > voices)
                throw new IllegalArgumentException("Voice chosen cannot have tendency.");
            bonusIntv.get(chordTone - 1).add(intv);
            bonusDir.get(chordTone - 1).add(dir);
            bonusValue.get(chordTone - 1).add(bonus);
            return this;
        }
        public Builder validator(ChordValidator validator)
        {
            this.validator = validator;
            return this;
        }
        public Builder scorer(ChordScorer scorer)
        {
            this.scorer = scorer;
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

        prepareDir = builder.prepareDir;
        prepareIntv = builder.prepareIntv;
        tendencyDir = builder.tendencyDir;
        tendencyIntv = builder.tendencyIntv;
        altTendencyDir = builder.altTendencyDir;
        altTendencyIntv = builder.altTendencyIntv;
        bonusDir = builder.bonusDir;
        bonusIntv = builder.bonusIntv;
        bonusValue = builder.bonusValue;
        
        if (builder.validator != null && builder.scorer != null)
        {
            setValidator(builder.validator);
            setScorer(builder.scorer);
        }
    }

    public void yield()
    {
        if (!validatorChanged)
            return;
        
        validatorChanged = false;
        realizations.clear();
        for (Note bassNote : bass.allInRange(lo[0], hi[0]))
        {
            ChordRealization temp = new ChordRealization();
            Note tempnote = Note.build(bassNote);
            temp.addVoice(tempnote);
            yieldHelper(1, temp, lo, hi, voices);
        }
    }
    private void yieldHelper(int num, ChordRealization accum, Note[] lo, Note[] hi, int voices)
    {
        if (num == voices)
        {
            if (validator.validate(accum, this))
            {
                accum.setLoss(scorer.score(accum, this));

                for (Map.Entry<Integer, Note> entry : insistList.entrySet())
                    accum.replace(entry.getKey() - 1, entry.getValue());
                if (!validator.withinOctave(accum.getNotes()))
                    return;
                
                if (realizations.indexOf(accum) == -1)
                    realizations.add(accum);
            }
        }
        else if (num < voices)
        {
            for (int i = 0; i < noteSet.size(); i++)
            {
                Note note_proto = noteSet.get(i);
                for (Note note : note_proto.allInRange(lo[num], hi[num]))
                {
                    for (int j = 0; j < prepareDir.get(i).size(); j++)
                    {
                        if (prepareDir.get(i).get(j) == Note.Dir.Above)
                            note.addPrepare(note.intervalAbove(prepareIntv.get(i).get(j)));
                        else
                            note.addPrepare(note.intervalBelow(prepareIntv.get(i).get(j)));
                    }
                    for (int j = 0; j < tendencyDir.get(i).size(); j++)
                    {
                        if (tendencyDir.get(i).get(j) == Note.Dir.Above)
                            note.addTendency(note.intervalAbove(tendencyIntv.get(i).get(j)));
                        else
                            note.addTendency(note.intervalBelow(tendencyIntv.get(i).get(j)));
                    }
                    for (int j = 0; j < altTendencyDir.get(i).size(); j++)
                    {
                        if (altTendencyDir.get(i).get(j) == Note.Dir.Above)
                            note.addAltTendency(note.intervalAbove(altTendencyIntv.get(i).get(j)));
                        else
                            note.addAltTendency(note.intervalBelow(altTendencyIntv.get(i).get(j)));
                    }
                    for (int j = 0; j < bonusDir.get(i).size(); j++)
                    {
                        if (bonusDir.get(i).get(j) == Note.Dir.Above)
                            note.addBonus(note.intervalAbove(bonusIntv.get(i).get(j)), bonusValue.get(i).get(j));
                        else
                            note.addBonus(note.intervalBelow(bonusIntv.get(i).get(j)), bonusValue.get(i).get(j));
                    }
                    if (note.compareTo(accum.highestNote()) >= 0)
                        yieldHelper(num + 1, accum.addVoiceNew(note), lo, hi, voices);
                }
            }
        }
    }

    public void setValidator(ChordValidator validator)
    {
        this.validator = validator;
        validatorChanged = true;
    }

    public void setScorer(ChordScorer scorer)
    {
        this.scorer = scorer;
        
        if (!realizations.isEmpty())
        {
            for (ChordRealization cr : realizations)
                cr.setLoss(scorer.score(cr, this));
        }
    }

    public void setInsistList(HashMap<Integer, Note> insistList)
    {
        this.insistList = insistList;
    }

    public ArrayList getNoteSet()
    {
        return noteSet;
    }

    public ArrayList<ChordRealization> getRealizations()
    {
        Collections.sort(realizations);
        return realizations;
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
