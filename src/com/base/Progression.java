package com.base;

import com.validation.ChordScorer;
import com.validation.ChordValidator;
import com.validation.VoiceLeadingScorer;
import com.validation.VoiceLeadingValidator;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by NyLP on 6/20/17.
 */

public class Progression
{
    public class VoiceLeading implements Comparable<VoiceLeading>
    {
        private ArrayList<NoteCluster> piece;
        private int loss = 0;

        public VoiceLeading() { piece = new ArrayList<>(); }
        public VoiceLeading(VoiceLeading vl) { piece = new ArrayList<>(vl.piece); loss = vl.loss; }

        public void addChord(NoteCluster notes) { piece.add(notes); }
        public VoiceLeading addChordNew(NoteCluster notes)
        {
            VoiceLeading ret = new VoiceLeading(this);
            ret.addChord(notes);
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

        public ArrayList<NoteCluster> getPiece()
        {
            return piece;
        }

        public NoteCluster lastChord() { return piece.get(piece.size() - 1); }

        public void play()
        {
            Synthesizer synth = null;
            try { synth = MidiSystem.getSynthesizer(); }
            catch (MidiUnavailableException e)
            {
                e.printStackTrace();
                System.exit(1);
            }
            try { synth.open(); }
            catch (MidiUnavailableException e)
            {
                e.printStackTrace();
                System.exit(1);
            }
            MidiChannel[] channels = synth.getChannels();
            MidiChannel	channel = channels[0];

            for (NoteCluster nc : piece)
            {
                for (Note n : nc.getNotes())
                {
                    channel.noteOn(n.getCode(), 100);
                }
                try
                {
                    Thread.sleep(600);
                }
                catch (InterruptedException e)
                {
                }
                channel.allNotesOff();
            }
            synth.close();
        }

        @Override public int compareTo(VoiceLeading o)
        {
            if (o == null) throw new NullPointerException("Null VoiceLeading to compare");
            if (loss > o.loss) return 1;
            if (loss < o.loss) return -1;
            return 0;
        }

        @Override public String toString()
        {
            String temp = "";
            for (int i = 0; i < piece.size() - 1; i++)
            {
                temp += piece.get(i).toString() + " -> ";
            }
            temp += piece.get(piece.size() - 1).toString();
            return temp;
        }
    }

    public static class Harmony
    {
        private Chord chord;
        private ChordValidator cv;
        private ChordScorer cs;
//        private VoiceLeadingValidator vv;
//        private VoiceLeadingScorer vs;

        public Harmony(Chord chord, ChordValidator cv, ChordScorer cs) //, VoiceLeadingValidator vv, VoiceLeadingScorer vs)
        {
            this.chord = chord;
            this.cv = cv;
            this.cs = cs;
//            this.vv = vv;
//            this.vs = vs;
        }
//        public Harmony(Chord chord, ChordValidator cv, ChordScorer cs)
//        {
//            this(chord, cv, cs, null, null);
//        }
    }

    private ArrayList<Harmony> progression = new ArrayList<>();
    private ArrayList<VoiceLeading> collection;

    public void addHarmony(Harmony harmony) { progression.add(harmony); }
    public void yield()
    {
        collection = new ArrayList<>();
        for (Chord.ChordRealization cr : progression.get(0).chord.getRealizations().getRealizations())
        {
            VoiceLeading vl = new VoiceLeading();
            vl.addChord(cr);
            vl.setLoss(vl.getLoss() + cr.getLoss());
            yieldHelper(1, vl);
        }
        Collections.sort(collection);
    }
    private void yieldHelper(int num, VoiceLeading vl)
    {
        if (num == progression.size())
        {
            collection.add(vl);
        }
        else if (num < progression.size())
        {
            Harmony harmony = progression.get(num);
            Chord.ChordRealizationCollection crc = harmony.chord.getRealizations();
            for (Chord.ChordRealization cr : crc.getRealizations())
            {
                if (VoiceLeadingValidator.validate(vl.lastChord(), cr))
                {
                    VoiceLeading vln = vl.addChordNew(cr);
                    vln.setLoss(vl.getLoss() + VoiceLeadingScorer.score(vl.lastChord(), cr) + cr.getLoss());
                    yieldHelper(num + 1, vln);
                }
            }
        }
    }

    public ArrayList<VoiceLeading> getPieces()
    {
        return collection;
    }
}
