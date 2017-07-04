package com.nylp;

import com.base.Chord;
import com.base.Progression;
import com.common.ChordStructure;
import com.common.Interval;
import com.base.Note;
import com.validation.ChordScorer;
import com.validation.ChordValidator;

public class Main {

    public static void main(String[] args)
    {
        Chord tonic_triad = new Chord.Builder(Note.build("C"), ChordStructure.m.ctor()).build();
        ChordValidator tonic_triad_cv = new ChordValidator(new boolean[]{true, true, true}, new boolean[]{false, false, true});
        ChordScorer tonic_triad_cs = new ChordScorer(new int[]{50, 300, 200}, new int[] {0, 0, 500}, new int[]{100, 200, 300});
        
        Chord tonic9 = new Chord.Builder(Note.build("C"), new Interval[] {Interval.M3, Interval.P5, Interval.M7, Interval.M9})
                .voices(5)
                .low(new Note[]{Note.build("E2"), Note.build("B2"), Note.build("F3"), Note.build("C4"), Note.build("E4")})
                .high(new Note[]{Note.build("E4"), Note.build("A4"), Note.build("E5"), Note.build("A5"), Note.build("C6")}).build();
        ChordValidator tonic9_cv = new ChordValidator(new boolean[]{true, true, true, false, false}, new boolean[]{false, false, true, true, false});
        ChordScorer tonic9_cs = new ChordScorer(new int[]{50, 300, 200, 0, 0}, new int[] {0, 0, 300, 500, 0}, new int[]{100, 200, 300, 400});
        
        Chord tonic6 = new Chord.Builder(Note.build("C"), new Interval[] {Interval.M3, Interval.P5, Interval.M6})
                .voices(5)
                .low(new Note[]{Note.build("E2"), Note.build("B2"), Note.build("F3"), Note.build("C4"), Note.build("C4")})
                .high(new Note[]{Note.build("E4"), Note.build("A4"), Note.build("E5"), Note.build("A5"), Note.build("A5")}).build();
        ChordValidator tonic6_cv = new ChordValidator(new boolean[]{true, true, true, false}, new boolean[]{false, false, true, false});
        ChordScorer tonic6_cs = new ChordScorer(new int[]{50, 300, 200, 0}, new int[] {0, 0, 300, 0}, new int[]{100, 200, 300, 400});

        Chord tonic64 = new Chord.Builder(Note.build("C"), ChordStructure.m.ctor()).inversion(2)
                .tendency(1, Note.Dir.Below, Interval.m2)
                .tendency(1, Note.Dir.Below, Interval.M2)
                .tendency(2, Note.Dir.Below, Interval.m2)
                .tendency(2, Note.Dir.Below, Interval.M2).build();
        ChordValidator tonic64_cv = new ChordValidator(new boolean[]{true, true, true}, new boolean[]{false, false, false});
        ChordScorer tonic64_cs = new ChordScorer(new int[]{500, 300, 50}, new int[] {0, 0, 0}, new int[]{100, 200, 300});
        
        Chord predom7 = new Chord.Builder(Note.build("D"), ChordStructure.mm7.ctor()).build();
        ChordValidator predom7_cv = new ChordValidator(new boolean[]{true, true, true, false}, new boolean[]{false, false, false, false});
        ChordScorer predom7_cs = new ChordScorer(new int[]{100, 50, 500, 0}, new int[] {0, 0, 0, 0}, new int[]{100, 200, 300});

        Chord predom11 = new Chord.Builder(Note.build("D"), new Interval[] {Interval.m3, Interval.P5, Interval.m7, Interval.P11})
                .voices(5)
                .low(new Note[]{Note.build("E2"), Note.build("B2"), Note.build("F3"), Note.build("C4"), Note.build("C4")})
                .high(new Note[]{Note.build("E4"), Note.build("A4"), Note.build("E5"), Note.build("A5"), Note.build("A5")}).build();
        ChordValidator predom11_cv = new ChordValidator(new boolean[]{true, true, true, false, false}, new boolean[]{false, false, true, true, false});
        ChordScorer predom11_cs = new ChordScorer(new int[]{100, 50, 500, 0, 0}, new int[] {0, 0, 100, 300, 0}, new int[]{100, 200, 300, 400});
        
        Chord pre_triad = new Chord.Builder(Note.build("F"), ChordStructure.m.ctor()).build();
        ChordValidator pre_triad_cv = new ChordValidator(new boolean[]{true, true, true}, new boolean[]{false, false, false});
        ChordScorer pre_triad_cs = new ChordScorer(new int[]{50, 100, 200}, new int[] {0, 0, 0}, new int[]{100, 200, 300});

        Chord dom7 = new Chord.Builder(Note.build("G"), ChordStructure.Mm7.ctor())
                .tendency(2, Note.Dir.Above, Interval.m2)
                .tendency(2, Note.Dir.Below, Interval.M3)
                .tendency(4, Note.Dir.Below, Interval.m2)
                .tendency(4, Note.Dir.Below, Interval.M2).build();
        ChordValidator dom7_cv = new ChordValidator(new boolean[]{true, false, true, false}, new boolean[]{false, false, true, false});
        ChordScorer dom7_cs = new ChordScorer(new int[]{50, 0, 300, 0}, new int[] {0, 0, 30, 0}, new int[]{500, 200, 300});
        
        Chord dom9 = new Chord.Builder(Note.build("G"), new Interval[] {Interval.M3, Interval.P5, Interval.m7, Interval.M9})
                .voices(5)
                .low(new Note[]{Note.build("E2"), Note.build("B2"), Note.build("F3"), Note.build("C4"), Note.build("C4")})
                .high(new Note[]{Note.build("E4"), Note.build("A4"), Note.build("E5"), Note.build("A5"), Note.build("A5")})
                .tendency(2, Note.Dir.Above, Interval.m2)
                .tendency(2, Note.Dir.Below, Interval.M3)
                .tendency(4, Note.Dir.Below, Interval.m2)
                .tendency(4, Note.Dir.Below, Interval.M2).build();
        ChordValidator dom9_cv = new ChordValidator(new boolean[]{true, false, true, false, false}, new boolean[]{false, false, true, false, false});
        ChordScorer dom9_cs = new ChordScorer(new int[]{50, 0, 300, 0, 0}, new int[] {0, 0, 100, 0, 0}, new int[]{500, 200, 300, 400});
        
        Chord dom65 = new Chord.Builder(Note.build("G"), ChordStructure.Mm7.ctor()).inversion(1)
                .tendency(2, Note.Dir.Above, Interval.m2)
                .tendency(2, Note.Dir.Below, Interval.M3)
                .tendency(4, Note.Dir.Below, Interval.m2)
                .tendency(4, Note.Dir.Below, Interval.M2).build();

        Chord submed_triad = new Chord.Builder(Note.build("Ab"), ChordStructure.M.ctor()).build();
        ChordValidator submed_triad_cv = new ChordValidator(new boolean[]{true, true, true}, new boolean[]{false, false, false});
        ChordScorer submed_triad_cs = new ChordScorer(new int[]{100, 50, 500}, new int[] {0, 0, 500}, new int[]{100, 200, 300});

        Progression progression = new Progression();
        progression.addHarmony(new Progression.Harmony(tonic_triad, tonic_triad_cv, tonic_triad_cs));
        progression.addHarmony(new Progression.Harmony(pre_triad, pre_triad_cv, pre_triad_cs));
        progression.addHarmony(new Progression.Harmony(dom7, dom7_cv, dom7_cs));
        progression.addHarmony(new Progression.Harmony(submed_triad, submed_triad_cv, submed_triad_cs));
        progression.addHarmony(new Progression.Harmony(pre_triad, pre_triad_cv, pre_triad_cs));
        progression.addHarmony(new Progression.Harmony(tonic64, tonic64_cv, tonic64_cs));
        progression.addHarmony(new Progression.Harmony(dom7, dom7_cv, dom7_cs));
        progression.addHarmony(new Progression.Harmony(tonic_triad, tonic_triad_cv, tonic_triad_cs));
//        progression.addHarmony(new Progression.Harmony(tonic9, tonic9_cv, tonic9_cs));
//        progression.addHarmony(new Progression.Harmony(predom11, predom11_cv, predom11_cs));
//        progression.addHarmony(new Progression.Harmony(dom9, dom9_cv, dom9_cs));
//        progression.addHarmony(new Progression.Harmony(tonic6, tonic6_cv, tonic6_cs));

        progression.insist(1, 1, Note.build("C3"));
        progression.insist(1, 2, Note.build("Eb4"));
        progression.insist(1, 3, Note.build("C5"));
        progression.insist(1, 4, Note.build("Ab5"));
        progression.insist(2, 4, Note.build("G5"));

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
