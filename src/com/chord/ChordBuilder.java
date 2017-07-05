package com.chord;

import com.base.Chord;
import com.validation.ChordScorer;
import com.validation.ChordValidator;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Helper builder for a chord, able to set validator and scorer params.
 * 
 * Created by NyLP on 7/4/17.
 */

public class ChordBuilder
{
    private int numChordtones = 3;
    private int voices = 4;
    private Chord.Builder chordBuilder;
    private final ArrayList<Boolean> repeatability = new ArrayList<>(Arrays.asList(true, true, true));
    private final ArrayList<Boolean> omitability = new ArrayList<>(Arrays.asList(false, false, true));
    private final ArrayList<Integer> repeatPenalty = new ArrayList<>(Arrays.asList(50, 500, 200));
    private final ArrayList<Integer> omitPenalty = new ArrayList<>(Arrays.asList(0, 0, 500));
    private final ArrayList<Integer> unisonPenalty = new ArrayList<>(Arrays.asList(100, 200, 300));
    
    public ChordBuilder() {}
    public ChordBuilder(int numChordtones, int voices)
    {
        this.numChordtones = numChordtones;
        this.voices = voices;
        for (int i = 0; i < voices - 4; i++)
            unisonPenalty.add((i + 4) * 100);
        for (int i = 0; i < numChordtones - 4; i++)
        {
            repeatability.add(false);
            repeatPenalty.add(0);
            omitability.add(true);
            omitPenalty.add(500);
        }
        repeatability.add(false);
        repeatPenalty.add(0);
        omitability.add(false);
        omitPenalty.add(0);
    }
    
    public ChordBuilder repeatability(int chordTone, boolean value)
    {
        if (chordTone < 1 || chordTone > numChordtones)
            throw new IllegalArgumentException("Intended chord tone does not exist.");
        repeatability.remove(chordTone - 1);
        repeatability.add(chordTone - 1, value);
        return this;
    }
    public ChordBuilder omitability(int chordTone, boolean value)
    {
        if (chordTone < 1 || chordTone > numChordtones)
            throw new IllegalArgumentException("Intended chord tone does not exist.");
        omitability.remove(chordTone - 1);
        omitability.add(chordTone - 1, value);
        return this;
    }
    public ChordBuilder repeatPenalty(int chordTone, int value)
    {
        if (chordTone < 1 || chordTone > numChordtones)
            throw new IllegalArgumentException("Intended chord tone does not exist.");
        repeatPenalty.remove(chordTone - 1);
        repeatPenalty.add(chordTone - 1, value);
        return this;
    }
    public ChordBuilder omitPenalty(int chordTone, int value)
    {
        if (chordTone < 1 || chordTone > numChordtones)
            throw new IllegalArgumentException("Intended chord tone does not exist.");
        omitPenalty.remove(chordTone - 1);
        omitPenalty.add(chordTone - 1, value);
        return this;
    }
    public ChordBuilder unisonPenalty(int voice, int value)
    {
        if (voice < 1 || voice > voices - 1)
            throw new IllegalArgumentException("Intended voice does not exist.");
        unisonPenalty.remove(voice - 1);
        unisonPenalty.add(voice - 1, value);
        return this;
    }
    public ChordBuilder chord(Chord.Builder chordBuilder)
    {
        this.chordBuilder = chordBuilder;
        boolean[] b1 = new boolean[repeatability.size()];
        boolean[] b2 = new boolean[omitability.size()];
        int[] i1 = new int[repeatPenalty.size()];
        int[] i2 = new int[omitPenalty.size()];
        int[] i3 = new int[unisonPenalty.size()];
        for (int i = 0; i < numChordtones; i++)
        {
            b1[i] = repeatability.get(i);
            b2[i] = omitability.get(i);
            i1[i] = repeatPenalty.get(i);
            i2[i] = omitPenalty.get(i);
        }
        for (int i = 0; i < voices - 1; i++)
            i3[i] = unisonPenalty.get(i);

        this.chordBuilder.validator(new ChordValidator(b1, b2));
        this.chordBuilder.scorer(new ChordScorer(i1, i2, i3));
        return this;
    }
    
    public Chord.Builder build()
    { return chordBuilder; }
}
