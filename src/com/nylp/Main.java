package com.nylp;

import com.base.Chord;
import com.base.Progression;
import com.base.Scale;
import com.chord.CommonClassicalChords;
import com.common.ChordStructure;
import com.common.Interval;
import com.base.Note;
import com.validation.ChordScorer;
import com.validation.ChordValidator;

public class Main {

    public static void main(String[] args)
    {
        Scale scale = new Scale(Note.build("C"), new Interval[] {Interval.M2, Interval.M2, Interval.m2, Interval.M2, Interval.M2, Interval.M2, Interval.m2});
        
        Progression progression = new Progression();
        progression.addHarmony(CommonClassicalChords.Triad_i(Note.build("C")).build().build());
        progression.addHarmony(CommonClassicalChords.Triad_iio(Note.build("D"), 1).build().build());
        progression.addHarmony(CommonClassicalChords.Seventh_V7(Note.build("G")).build().build());
        progression.addHarmony(CommonClassicalChords.Triad_VI(Note.build("Ab")).build().build());
        progression.addHarmony(CommonClassicalChords.Seventh_ivmm7(Note.build("F")).build().build());
        progression.addHarmony(CommonClassicalChords.Triad_i(Note.build("C"), 2).build().build());
        progression.addHarmony(CommonClassicalChords.Seventh_V7(Note.build("G")).build().build());
        progression.addHarmony(CommonClassicalChords.Triad_i(Note.build("C")).build().build());

        progression.insist(1, 1, Note.build("C3"));
        progression.insist(1, 2, Note.build("Eb4"));
        progression.insist(1, 3, Note.build("C5"));
        progression.insist(1, 4, Note.build("Ab5"));
        
        progression.fixNoteClass(3, 4, Note.build("G"));
        progression.fixNoteClass(4, 4, Note.build("Eb"));
        progression.fixNoteClass(5, 4, Note.build("F"));
        progression.fixNoteClass(6, 4, Note.build("Eb"));
        progression.fixNoteClass(7, 4, Note.build("D"));
        progression.fixNoteClass(8, 4, Note.build("C"));
        
        progression.yield();
        
        System.out.println(progression.getPieces().size() + " realizations generated");
        for (int i = 0; i < 4; i++)
        {
            System.out.println(progression.getPieces().get(i) + " loss: " + progression.getPieces().get(i).getLoss());
            progression.getPieces().get(i).play();
        }
    }
}
