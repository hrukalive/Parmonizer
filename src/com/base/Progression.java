package com.base;

import com.validation.VoiceLeadingScorer;
import com.validation.VoiceLeadingValidator;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Collection of chords so that under certain restraints, progression
 * can be realized.
 * 
 * Created by NyLP on 6/20/17.
 */

public class Progression
{
    public class VoiceLeading implements Comparable<VoiceLeading>
    {
        private ArrayList<Chord.NoteCluster> piece;
        private int loss = 0;

        public VoiceLeading() { piece = new ArrayList<>(); }
        public VoiceLeading(VoiceLeading vl) { piece = new ArrayList<>(vl.piece); loss = vl.loss; }

        public void addChord(Chord.NoteCluster notes) { piece.add(notes); }
        public VoiceLeading addChordNew(Chord.NoteCluster notes)
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

        public ArrayList<Chord.NoteCluster> getPiece()
        {
            return piece;
        }

        public Chord.NoteCluster lastChord() { return piece.get(piece.size() - 1); }

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
//            for (Instrument inst : synth.getAvailableInstruments())
//                System.out.println(inst._toString());
            MidiChannel[] channels = synth.getChannels();
            MidiChannel	channel = channels[0];
            channel.programChange(48);

            for (Chord.NoteCluster nc : piece)
            {
                for (Note n : nc.getNotes())
                {
                    channel.noteOn(n.getCode(), 100);
                }
                try
                {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e)
                {
                }
                channel.allNotesOff();
            }
            synth.close();
            try
            {
                Thread.sleep(500);
            }
            catch (InterruptedException e)
            {
            }
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

    private ArrayList<Chord> progression = new ArrayList<>();
    private ArrayList<VoiceLeading> collection = new ArrayList<>();

    public void addHarmony(Chord chord)
    { 
        progression.add(new Chord(chord));
    }

    public void yield()
    {
        collection.clear();
        yieldHelper(0, null);
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
            Chord harmony = progression.get(num);
            harmony.yield();
            ArrayList<Chord.NoteCluster> crs = harmony.getRealizations();
            for (int i = 0; i < crs.size(); i++)
            {
                Chord.NoteCluster cr = crs.get(i);

                if (num == 0)
                {
                    VoiceLeading tempvl = new VoiceLeading();
                    tempvl.addChord(cr);
                    tempvl.setLoss(tempvl.getLoss() + cr.getLoss());
                    yieldHelper(num + 1, tempvl);
                }
                else
                {
                    VoiceLeadingValidator.VoiceLeadingValidationResult result = VoiceLeadingValidator.validate(vl.lastChord(), cr);
                    if (result.equals(VoiceLeadingValidator.VoiceLeadingValidationResult.SUCCESS))
                    {
                        VoiceLeading vln = vl.addChordNew(cr);
                        vln.setLoss(vl.getLoss() + VoiceLeadingScorer.score(vl.lastChord(), cr) + cr.getLoss());
                        yieldHelper(num + 1, vln);
                    }
//                    else
//                    {
//                        System.err.println(result.name());
//                    }
                }
            }
        }
    }

    public ArrayList<VoiceLeading> getPieces()
    {
        return collection;
    }
}
