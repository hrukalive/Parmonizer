package com.nylp;

import com.base.Chord;
import com.base.Progression;
import com.common.ChordStructure;
import com.common.Interval;
import com.base.Note;
import com.validation.ChordScorer;
import com.validation.ChordValidator;
import com.validation.VoiceLeadingScorer;
import com.validation.VoiceLeadingValidator;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

public class Main {

    public static void main(String[] args)
    {
        Chord tonic_triad = new Chord.Builder(Note.n("C"), ChordStructure.M.ctor()).build();
        ChordValidator tonic_triad_cv = new ChordValidator(new boolean[]{true, true, true}, new boolean[]{false, false, true});
        ChordScorer tonic_triad_cs = new ChordScorer(new int[]{50, 300, 200}, new int[] {0, 0, 500}, new int[]{100, 200, 300});
        tonic_triad.applyValidation(tonic_triad_cv);
        tonic_triad.applyScorer(tonic_triad_cs);
        System.out.println(tonic_triad.getRealizations().size() + " tonic chords generated");
        
        Chord tonic9 = new Chord.Builder(Note.n("C"), new Interval[] {Interval.M3, Interval.P5, Interval.M7, Interval.M9})
                .voices(5)
                .low(new Note[]{Note.n("E2"), Note.n("B2"), Note.n("F3"), Note.n("C4"), Note.n("C4")})
                .high(new Note[]{Note.n("E4"), Note.n("A4"), Note.n("E5"), Note.n("A5"), Note.n("A5")}).build();
        ChordValidator tonic9_cv = new ChordValidator(new boolean[]{true, true, true, false, false}, new boolean[]{false, false, true, true, false});
        ChordScorer tonic9_cs = new ChordScorer(new int[]{50, 300, 200, 0, 0}, new int[] {0, 0, 300, 500, 0}, new int[]{100, 200, 300, 400});
        tonic9.applyValidation(tonic9_cv);
        tonic9.applyScorer(tonic9_cs);
        System.out.println(tonic9.getRealizations().size() + " tonic9 chords generated");
        
        Chord tonic6 = new Chord.Builder(Note.n("C"), new Interval[] {Interval.M3, Interval.P5, Interval.M6})
                .voices(5)
                .low(new Note[]{Note.n("E2"), Note.n("B2"), Note.n("F3"), Note.n("C4"), Note.n("C4")})
                .high(new Note[]{Note.n("E4"), Note.n("A4"), Note.n("E5"), Note.n("A5"), Note.n("A5")}).build();
        ChordValidator tonic6_cv = new ChordValidator(new boolean[]{true, true, true, false}, new boolean[]{false, false, true, false});
        ChordScorer tonic6_cs = new ChordScorer(new int[]{50, 300, 200, 0}, new int[] {0, 0, 300, 0}, new int[]{100, 200, 300, 400});
        tonic6.applyValidation(tonic6_cv);
        tonic6.applyScorer(tonic6_cs);
        System.out.println(tonic6.getRealizations().size() + " tonic6 chords generated");

        Chord tonic64 = new Chord.Builder(Note.n("C"), ChordStructure.M.ctor()).inversion(2)
                .tendency(0, false, Interval.m2)
                .tendency(1, false, Interval.M2).build();
        ChordValidator tonic64_cv = new ChordValidator(new boolean[]{true, true, true}, new boolean[]{false, false, false});
        ChordScorer tonic64_cs = new ChordScorer(new int[]{500, 300, 50}, new int[] {0, 0, 0}, new int[]{100, 200, 300});
        tonic64.applyValidation(tonic64_cv);
        tonic64.applyScorer(tonic64_cs);
        System.out.println(tonic64.getRealizations().size() + " tonic64 chords generated");
        
        Chord predom7 = new Chord.Builder(Note.n("D"), ChordStructure.mm7.ctor()).build();
        ChordValidator predom7_cv = new ChordValidator(new boolean[]{true, true, true, false}, new boolean[]{false, false, false, false});
        ChordScorer predom7_cs = new ChordScorer(new int[]{100, 50, 500, 0}, new int[] {0, 0, 0, 0}, new int[]{100, 200, 300});
        predom7.applyValidation(predom7_cv);
        predom7.applyScorer(predom7_cs);
        System.out.println(predom7.getRealizations().size() + " pre-dom7 chords generated");

        Chord predom11 = new Chord.Builder(Note.n("D"), new Interval[] {Interval.m3, Interval.P5, Interval.m7, Interval.P11})
                .voices(5)
                .low(new Note[]{Note.n("E2"), Note.n("B2"), Note.n("F3"), Note.n("C4"), Note.n("C4")})
                .high(new Note[]{Note.n("E4"), Note.n("A4"), Note.n("E5"), Note.n("A5"), Note.n("A5")}).build();
        ChordValidator predom11_cv = new ChordValidator(new boolean[]{true, true, true, false, false}, new boolean[]{false, false, true, true, false});
        ChordScorer predom11_cs = new ChordScorer(new int[]{100, 50, 500, 0, 0}, new int[] {0, 0, 100, 300, 0}, new int[]{100, 200, 300, 400});
        predom11.applyValidation(predom11_cv);
        predom11.applyScorer(predom11_cs);
        System.out.println(predom11.getRealizations().size() + " pre-dom11 chords generated");
        
        Chord pre_triad = new Chord.Builder(Note.n("F"), ChordStructure.M.ctor()).build();
        ChordValidator pre_triad_cv = new ChordValidator(new boolean[]{true, true, true}, new boolean[]{false, false, false});
        ChordScorer pre_triad_cs = new ChordScorer(new int[]{50, 100, 200}, new int[] {0, 0, 0}, new int[]{100, 200, 300});
        pre_triad.applyValidation(pre_triad_cv);
        pre_triad.applyScorer(pre_triad_cs);
        System.out.println(pre_triad.getRealizations().size() + " pre chords generated");

        Chord dom7 = new Chord.Builder(Note.n("G"), ChordStructure.Mm7.ctor())
                .tendency(1, true, Interval.m2)
                .tendency(1, false, Interval.M3)
                .tendency(3, false, Interval.m2)
                .tendency(3, false, Interval.M2).build();
        ChordValidator dom7_cv = new ChordValidator(new boolean[]{true, false, true, false}, new boolean[]{false, false, true, false});
        ChordScorer dom7_cs = new ChordScorer(new int[]{50, 0, 300, 0}, new int[] {0, 0, 30, 0}, new int[]{500, 200, 300});
        dom7.applyValidation(dom7_cv);
        dom7.applyScorer(dom7_cs);
        System.out.println(dom7.getRealizations().size() + " dom7 chords generated");
        
        Chord dom9 = new Chord.Builder(Note.n("G"), new Interval[] {Interval.M3, Interval.P5, Interval.m7, Interval.M9})
                .voices(5)
                .low(new Note[]{Note.n("E2"), Note.n("B2"), Note.n("F3"), Note.n("C4"), Note.n("C4")})
                .high(new Note[]{Note.n("E4"), Note.n("A4"), Note.n("E5"), Note.n("A5"), Note.n("A5")})
                .tendency(1, true, Interval.m2)
                .tendency(1, false, Interval.M3)
                .tendency(3, false, Interval.m2)
                .tendency(3, false, Interval.M2).build();
        ChordValidator dom9_cv = new ChordValidator(new boolean[]{true, false, true, false, false}, new boolean[]{false, false, true, false, false});
        ChordScorer dom9_cs = new ChordScorer(new int[]{50, 0, 300, 0, 0}, new int[] {0, 0, 100, 0, 0}, new int[]{500, 200, 300, 400});
        dom9.applyValidation(dom9_cv);
        dom9.applyScorer(dom9_cs);
        System.out.println(dom9.getRealizations().size() + " dom9 chords generated");
        
        Chord dom65 = new Chord.Builder(Note.n("G"), ChordStructure.Mm7.ctor()).inversion(1)
                .tendency(1, true, Interval.m2)
                .tendency(1, false, Interval.M3)
                .tendency(3, false, Interval.m2)
                .tendency(3, false, Interval.M2).build();
        dom65.applyValidation(dom7_cv);
        dom65.applyScorer(dom7_cs);
        System.out.println(dom65.getRealizations().size() + " dom65 chords generated");

        Chord submed_triad = new Chord.Builder(Note.n("A"), ChordStructure.m.ctor()).build();
        ChordValidator submed_triad_cv = new ChordValidator(new boolean[]{true, true, true}, new boolean[]{false, false, false});
        ChordScorer submed_triad_cs = new ChordScorer(new int[]{100, 50, 500}, new int[] {0, 0, 500}, new int[]{100, 200, 300});
        submed_triad.applyValidation(submed_triad_cv);
        submed_triad.applyScorer(submed_triad_cs);
        System.out.println(submed_triad.getRealizations().size() + " submed chords generated");

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
        
        progression.insist(5, 4, Note.n("F"));
        progression.insist(6, 4, Note.n("E"));
        progression.insist(7, 4, Note.n("D"));
        progression.insist(8, 4, Note.n("C"));
        progression.yield();
        System.out.println(progression.getPieces().size() + " realizations generated");
        for (int i = 0; i < 4; i++)
        {
            System.out.println(progression.getPieces().get(i) + " loss: " + progression.getPieces().get(i).getLoss());
            progression.getPieces().get(i).play();
        }

    }
}
