package com.nylp;

import com.base.Note;
import com.base.Progression;
import com.base.chord.Chord;
import com.base.interval.Interval;
import com.base.realization.ChordVoicing;
import com.base.scale.Scale;
import com.utils.VoiceLeadingPlayer;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        ArrayList<Interval> intervalSteps = new ArrayList<>();
        intervalSteps.add(Interval.M2);
        intervalSteps.add(Interval.M2);
        intervalSteps.add(Interval.m2);
        intervalSteps.add(Interval.M2);
        intervalSteps.add(Interval.M2);
        intervalSteps.add(Interval.M2);
        intervalSteps.add(Interval.m2);
        Scale scale = new Scale(Note.parse("C"), intervalSteps);

        System.out.println(scale.getMode(0).chord(0, 3, 7));
        System.out.println(scale.getMode(0).negativeChord(4, 3, 7));
        System.out.println(Chord.parse("Cdim7").build().getChordNotes());

        Progression prog = new Progression();
        prog.addHarmony(new ChordVoicing(Chord.parse("-Cmaj").build()));
        prog.addHarmony(new ChordVoicing(Chord.parse("-Fmaj7").build()));
        prog.addHarmony(new ChordVoicing(Chord.parse("G7b9").build()));
        prog.addHarmony(new ChordVoicing(Chord.parse("Cmaj").build()));

        prog.yield();
        System.out.println(prog.getPieces().size() + " realizations generated");
        for (int i = 0; i < 4; i++) {
            System.out.println(prog.getPieces().get(i) + " loss: " + prog.getPieces().get(i).getLoss());
            VoiceLeadingPlayer.play(prog.getPieces().get(i));
        }
    }
}
