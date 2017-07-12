package com.nylp;

import com.base.*;
import com.base.Progression;
import com.chord.CommonClassicalChords;
import com.base.DiatonicScale;
import com.base.Note;

public class Main {

    public static void main(String[] args)
    {
        DiatonicScale scale = new DiatonicScale(Note.build("C"));
        DiatonicScale.DiatonicMode minor = (DiatonicScale.DiatonicMode)scale.getMode("HarmonicMinor");
        Progression progression = new Progression();

        progression.addHarmony(minor.getTriad(1).build().build());
        progression.addHarmony(minor.getTriad(3).build().build());
        progression.addHarmony(minor.getTriad(6, 2).build().build());
        progression.addHarmony(minor.getTriad(2, 1).build().build());
        progression.addHarmony(minor.getSeventh(5).build().build());
        progression.addHarmony(minor.getTriad(6).build().build());
        progression.addHarmony(minor.getSeventh(4).build().build());
        progression.addHarmony(minor.getTriad(1, 2).build().build());
        progression.addHarmony(minor.getSeventh(5).build().build());
        progression.addHarmony(minor.getTriad(1).build().build());
        
        progression.insist(1, 1, Note.build("C3"));
        progression.insist(1, 2, Note.build("Eb4"));
        progression.insist(1, 3, Note.build("C5"));
        progression.insist(1, 4, Note.build("Ab5"));
        progression.insist(3, 4, Note.build("G5"));

        progression.fixNoteClass(2, 4, Note.build("Bb"));
        progression.fixNoteClass(5, 4, Note.build("G"));
        progression.fixNoteClass(6, 4, Note.build("Eb"));
        progression.fixNoteClass(7, 4, Note.build("F"));
        progression.fixNoteClass(8, 4, Note.build("Eb"));
        progression.fixNoteClass(9, 4, Note.build("D"));
        progression.fixNoteClass(10, 4, Note.build("C"));

        progression.yield();

        System.out.println(progression.getPieces().size() + " realizations generated");
        for (int i = 0; i < 4; i++)
        {
            System.out.println(progression.getPieces().get(i) + " loss: " + progression.getPieces().get(i).getLoss());
            progression.getPieces().get(i).play();
        }
    }
}
