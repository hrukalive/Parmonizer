package com.nylp;

import com.base.Note;
import com.base.Progression;
import com.base.chord.Chord;
import com.base.interval.Interval;
import com.base.realization.ChordVoicing;
import com.base.scale.Mode;
import com.base.scale.Scale;
import com.utils.VoiceLeadingPlayer;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        Mode scale = Scale.MELODIC_MINOR;
        scale.build(Note.parse("C"));
        System.out.println(scale);

        System.out.println(scale.chord(0, 3, 7));
        System.out.println(scale.negativeChord(4, 3, 7));
        System.out.println(Chord.parse("Cdim7").build().getChordNotes());

        scale = Scale.WHOLE_TONE;
        scale.build(Note.parse("Db"));
        System.out.println(scale);

        Progression prog = new Progression();
        prog.addHarmony(new ChordVoicing(Chord.parse("C69").build()));
        prog.addHarmony(new ChordVoicing(Chord.parse("-Dmin7/F", Note.parse("C")).build()));
        prog.addHarmony(new ChordVoicing(Chord.parse("-G7", Note.parse("C"), Interval.P5).build()));
        prog.addHarmony(new ChordVoicing(Chord.parse("Cmaj").build()));

        prog.yield();
        System.out.println(prog.getPieces().size() + " realizations generated");
        for (int i = 0; i < 4; i++) {
            System.out.println(prog.getPieces().get(i) + " loss: " + prog.getPieces().get(i).getLoss());
            VoiceLeadingPlayer.play(prog.getPieces().get(i));
        }
    }
}
