package com.chord;

import com.base.Chord;
import com.base.Note;
import com.common.ChordStructure;
import com.common.Interval;

/**
 * Chords that often used in Classical music theory.
 * 
 * Created by NyLP on 6/30/17.
 */

public final class CommonClassicalChords
{
    // Tonic function
    public static ChordBuilder Triad_I(Note root)
    {
        return Triad_I(root, 0);
    }
    public static ChordBuilder Triad_I(Note root, int inv)
    {
        return Triad_1(root, inv, true);
    }
    public static ChordBuilder Triad_i(Note root)
    {
        return Triad_i(root, 0);
    }
    public static ChordBuilder Triad_i(Note root, int inv)
    {
        return Triad_1(root, inv, false);
    }
    private static ChordBuilder Triad_1(Note root, int inv, boolean isMajor)
    {
        if (inv > 2)
            throw new IllegalArgumentException("Triad do not have inversion higher than 2.");
        
        if (inv < 2)
            return new ChordBuilder().chord(new Chord.Builder(root, isMajor ? ChordStructure.M.ctor() : ChordStructure.m.ctor()).inversion(inv));
        else
            return new ChordBuilder()
                    .omitability(3, false)
                    .repeatPenalty(1, 500)
                    .repeatPenalty(3, 50)
                    .chord(new Chord.Builder(root, isMajor ? ChordStructure.M.ctor() : ChordStructure.m.ctor())
                                   .tendency(1, Note.Dir.Below, Interval.m2)
                                   .tendency(1, Note.Dir.Below, Interval.M2)
                                   .tendency(2, Note.Dir.Below, Interval.m2)
                                   .tendency(2, Note.Dir.Below, Interval.M2).inversion(2));
    }
    
    // Pre-dominant function
    public static ChordBuilder Triad_ii(Note root)
    {
        return Triad_ii(root, 0);
    }
    public static ChordBuilder Triad_ii(Note root, int inv)
    {
        return Triad_2(root, inv, true);
    }
    public static ChordBuilder Triad_iio6(Note root)
    {
        return Triad_iio(root, 1);
    }
    public static ChordBuilder Triad_iio(Note root, int inv)
    {
        return Triad_2(root, inv, false);
    }
    private static ChordBuilder Triad_2(Note root, int inv, boolean isMinor)
    {
        if (!isMinor && inv == 0)
            throw new IllegalArgumentException("In classical music theory, root position diminished chord is not allowed.");
        if (inv > 2)
            throw new IllegalArgumentException("Triad do not have inversion higher than 2.");
        if (inv != 1)
        {
            return new ChordBuilder()
                    .omitability(3, false)
                    .chord(new Chord.Builder(root, isMinor ? ChordStructure.m.ctor() : ChordStructure.dim.ctor()).inversion(inv));
        }
        else
        {
            return new ChordBuilder()
                    .omitability(3, false)
                    .repeatPenalty(1, 500)
                    .repeatPenalty(2, 50)
                    .repeatPenalty(3, 1000)
                    .chord(new Chord.Builder(root, isMinor ? ChordStructure.m.ctor() : ChordStructure.dim.ctor()).inversion(inv));
        }
    }
    
    public static ChordBuilder Triad_IV(Note root)
    {
        return Triad_IV(root, 0);
    }
    public static ChordBuilder Triad_IV(Note root, int inv)
    {
        return Triad_4(root, inv, true);
    }
    public static ChordBuilder Triad_iv(Note root)
    {
        return Triad_iv(root, 0);
    }
    public static ChordBuilder Triad_iv(Note root, int inv)
    {
        return Triad_4(root, inv, false);
    }
    private static ChordBuilder Triad_4(Note root, int inv, boolean isMajor)
    {
        if (inv > 2)
            throw new IllegalArgumentException("Triad do not have inversion higher than 2.");

        return new ChordBuilder()
                .omitability(3, false)
                .chord(new Chord.Builder(root, isMajor ? ChordStructure.M.ctor() : ChordStructure.m.ctor()).inversion(inv));
    }
    
    public static ChordBuilder Seventh_iimm7(Note root)
    {
        return Seventh_iimm7(root, 0);
    }
    private static ChordBuilder Seventh_iimm7(Note root, int inv)
    {
        if (inv > 3)
            throw new IllegalArgumentException("Seventh chord do not have inversion higher than 3.");
        
        ChordBuilder cb = new ChordBuilder(4, 4)
                .omitPenalty(3, 500)
                .unisonPenalty(1, 500)
                .chord(new Chord.Builder(root, ChordStructure.mm7.ctor())
                               .preparedBy(4, Note.Dir.Above, Interval.P1)
                               .preparedBy(4, Note.Dir.Above, Interval.m2)
                               .preparedBy(4, Note.Dir.Above, Interval.M2)
                               .tendency(4, Note.Dir.Below, Interval.m2)
                               .tendency(4, Note.Dir.Below, Interval.M2)
                               .inversion(inv));
        
        if (inv != 0)
            cb.omitability(3, false);
        return cb;
    }
    
    public static ChordBuilder Seventh_iiom7(Note root)
    {
        return Seventh_iiom7(root, 0);
    }
    private static ChordBuilder Seventh_iiom7(Note root, int inv)
    {
        if (inv > 3)
            throw new IllegalArgumentException("Seventh chord do not have inversion higher than 3.");

        ChordBuilder cb = new ChordBuilder(4, 4)
                .omitPenalty(3, 500)
                .unisonPenalty(1, 500)
                .chord(new Chord.Builder(root, ChordStructure.om7.ctor())
                               .preparedBy(4, Note.Dir.Above, Interval.P1)
                               .preparedBy(4, Note.Dir.Above, Interval.m2)
                               .preparedBy(4, Note.Dir.Above, Interval.M2)
                               .tendency(4, Note.Dir.Below, Interval.m2)
                               .tendency(4, Note.Dir.Below, Interval.M2)
                               .inversion(inv));

        if (inv != 0)
            cb.omitability(3, false);
        return cb;
    }
    
    public static ChordBuilder Seventh_ivmm7(Note root)
    {
        return Seventh_ivmm7(root, 0);
    }
    private static ChordBuilder Seventh_ivmm7(Note root, int inv)
    {
        if (inv > 3)
            throw new IllegalArgumentException("Seventh chord do not have inversion higher than 3.");

        ChordBuilder cb = new ChordBuilder(4, 4)
                .omitPenalty(3, 500)
                .unisonPenalty(1, 500)
                .chord(new Chord.Builder(root, ChordStructure.mm7.ctor())
                               .preparedBy(4, Note.Dir.Above, Interval.P1)
                               .preparedBy(4, Note.Dir.Above, Interval.m2)
                               .preparedBy(4, Note.Dir.Above, Interval.M2)
                               .altTendency(4, Note.Dir.Below, Interval.m2)
                               .altTendency(4, Note.Dir.Below, Interval.M2)
                               .inversion(inv));

        if (inv != 0)
            cb.omitability(3, false);
        return cb;
    }
    
    public static ChordBuilder Seventh_IVMM7(Note root)
    {
        return Seventh_IVMM7(root, 0);
    }
    private static ChordBuilder Seventh_IVMM7(Note root, int inv)
    {
        if (inv > 3)
            throw new IllegalArgumentException("Seventh chord do not have inversion higher than 3.");

        ChordBuilder cb = new ChordBuilder(4, 4)
                .omitPenalty(3, 500)
                .unisonPenalty(1, 500)
                .chord(new Chord.Builder(root, ChordStructure.MM7.ctor())
                               .preparedBy(4, Note.Dir.Above, Interval.P1)
                               .preparedBy(4, Note.Dir.Above, Interval.m2)
                               .preparedBy(4, Note.Dir.Above, Interval.M2)
                               .altTendency(4, Note.Dir.Below, Interval.m2)
                               .altTendency(4, Note.Dir.Below, Interval.M2)
                               .inversion(inv));

        if (inv != 0)
            cb.omitability(3, false);
        return cb;
    }
    
    // Dominant function
    public static ChordBuilder Triad_V(Note root)
    {
        return Triad_V(root, 0);
    }
    private static ChordBuilder Triad_V(Note root, int inv)
    {
        if (inv > 2)
            throw new IllegalArgumentException("Triad do not have inversion higher than 2.");

        return new ChordBuilder()
                .repeatability(2, false)
                .omitPenalty(3, 1000)
                .unisonPenalty(1, 500)
                .chord(new Chord.Builder(root, ChordStructure.M.ctor())
                               .tendency(2, Note.Dir.Above, Interval.m2)
                               .tendency(2, Note.Dir.Below, Interval.M3)
                               .inversion(inv));
    }
    
    public static ChordBuilder Triad_viio6(Note root)
    {
        return Triad_viio(root, 1);
    }
    private static ChordBuilder Triad_viio(Note root, int inv)
    {
        if (inv == 0)
            throw new IllegalArgumentException("In classical music theory, root position diminished chord is not allowed.");
        if (inv > 2)
            throw new IllegalArgumentException("Triad do not have inversion higher than 2.");
        if (inv != 1)
        {
            return new ChordBuilder()
                    .repeatability(1, false)
                    .omitability(3, false)
                    .chord(new Chord.Builder(root, ChordStructure.dim.ctor()).inversion(inv));
        }
        else
        {
            return new ChordBuilder()
                    .repeatability(1, false)
                    .omitability(3, false)
                    .repeatPenalty(1, 500)
                    .repeatPenalty(2, 50)
                    .repeatPenalty(3, 1000)
                    .chord(new Chord.Builder(root, ChordStructure.dim.ctor()).inversion(inv));
        }
    }
    
    public static ChordBuilder Seventh_V7(Note root)
    {
        return Seventh_V7(root, 0);
    }
    private static ChordBuilder Seventh_V7(Note root, int inv)
    {
        if (inv > 3)
            throw new IllegalArgumentException("Seventh chord do not have inversion higher than 3.");

        return new ChordBuilder(4, 4)
                .repeatability(2, false)
                .omitPenalty(3, 500)
                .unisonPenalty(1, 500)
                .chord(new Chord.Builder(root, ChordStructure.Mm7.ctor())
                               .tendency(2, Note.Dir.Above, Interval.m2)
                               .altTendency(2, Note.Dir.Below, Interval.M3)
                               .tendency(4, Note.Dir.Below, Interval.m2)
                               .tendency(4, Note.Dir.Below, Interval.M2)
                               .inversion(inv));
    }
    
    public static ChordBuilder Seventh_viiom7(Note root)
    {
        return Seventh_viiom7(root, 0);
    }
    private static ChordBuilder Seventh_viiom7(Note root, int inv)
    {
        if (inv > 3)
            throw new IllegalArgumentException("Seventh chord do not have inversion higher than 3.");

        return new ChordBuilder(4, 4)
                .repeatability(1, false)
                .omitPenalty(3, 500)
                .unisonPenalty(1, 500)
                .chord(new Chord.Builder(root, ChordStructure.om7.ctor())
                               .preparedBy(4, Note.Dir.Above, Interval.P1)
                               .preparedBy(4, Note.Dir.Above, Interval.m2)
                               .preparedBy(4, Note.Dir.Above, Interval.M2)
                               .preparedBy(4, Note.Dir.Below, Interval.m2)
                               .preparedBy(4, Note.Dir.Below, Interval.M2)
                               .tendency(1, Note.Dir.Above, Interval.m2)
                               .tendency(2, Note.Dir.Above, Interval.M2)
                               .altTendency(2, Note.Dir.Below, Interval.M2)
                               .tendency(3, Note.Dir.Below, Interval.m2)
                               .tendency(4, Note.Dir.Below, Interval.M2)
                               .inversion(inv));
    }
    
    public static ChordBuilder Seventh_viio7(Note root)
    {
        return Seventh_viio7(root, 0);
    }
    private static ChordBuilder Seventh_viio7(Note root, int inv)
    {
        if (inv > 3)
            throw new IllegalArgumentException("Seventh chord do not have inversion higher than 3.");

        return new ChordBuilder(4, 4)
                .repeatability(1, false)
                .omitPenalty(3, 500)
                .unisonPenalty(1, 500)
                .chord(new Chord.Builder(root, ChordStructure.oo7.ctor())
                               .preparedBy(4, Note.Dir.Above, Interval.P1)
                               .preparedBy(4, Note.Dir.Above, Interval.m2)
                               .preparedBy(4, Note.Dir.Above, Interval.M2)
                               .preparedBy(4, Note.Dir.Below, Interval.m2)
                               .preparedBy(4, Note.Dir.Below, Interval.M2)
                               .tendency(1, Note.Dir.Above, Interval.m2)
                               .tendency(2, Note.Dir.Above, Interval.M2)
                               .altTendency(2, Note.Dir.Below, Interval.M2)
                               .tendency(3, Note.Dir.Below, Interval.m2)
                               .tendency(4, Note.Dir.Below, Interval.m2)
                               .inversion(inv));
    }
    
    // Sub-mediant
    public static ChordBuilder Triad_VI(Note root)
    {
        return Triad_VI(root, 0);
    }
    public static ChordBuilder Triad_VI(Note root, int inv)
    {
        return Triad_6(root, inv, true);
    }
    public static ChordBuilder Triad_vi(Note root)
    {
        return Triad_vi(root, 0);
    }
    public static ChordBuilder Triad_vi(Note root, int inv)
    {
        return Triad_6(root, inv, false);
    }
    private static ChordBuilder Triad_6(Note root, int inv, boolean isMajor)
    {
        if (inv > 2)
            throw new IllegalArgumentException("Triad do not have inversion higher than 2.");

        return new ChordBuilder()
                .omitability(3, false)
                .repeatPenalty(2, 100)
                .chord(new Chord.Builder(root, isMajor ? ChordStructure.M.ctor() : ChordStructure.m.ctor()).inversion(inv));
    }
    
    // Mediant
    public static ChordBuilder Triad_III(Note root)
    {
        return Triad_III(root, 0);
    }
    public static ChordBuilder Triad_III(Note root, int inv)
    {
        return Triad_3(root, inv, true);
    }
    public static ChordBuilder Triad_iii(Note root)
    {
        return Triad_iii(root, 0);
    }
    public static ChordBuilder Triad_iii(Note root, int inv)
    {
        return Triad_3(root, inv, false);
    }
    private static ChordBuilder Triad_3(Note root, int inv, boolean isMajor)
    {
        if (inv > 2)
            throw new IllegalArgumentException("Triad do not have inversion higher than 2.");

        return new ChordBuilder()
                .omitability(3, false)
                .repeatPenalty(1, 200)
                .repeatPenalty(2, 50)
                .chord(new Chord.Builder(root, isMajor ? ChordStructure.M.ctor() : ChordStructure.m.ctor()).inversion(inv));
    }
}
