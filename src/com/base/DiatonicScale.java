package com.base;

import com.chord.ChordBuilder;
import com.common.ChordStructure;
import com.common.Interval;

import java.util.ArrayList;

/**
 * Represent 7 tones western scale.
 * 
 * Created by NyLP on 7/5/17.
 */

public class DiatonicScale extends Scale
{
    public class DiatonicMode extends Mode implements IMode
    {
        public DiatonicMode(ArrayList<Note> scale_tones)
        {
            super(scale_tones);
        }
        
        public DiatonicMode(DiatonicMode mode)
        {
            super(mode);
        }
        
        @Override public ChordBuilder getTriad(int num, int inv, int voice)
        {
            if (num < 1 || num > triads.size())
                throw new IllegalArgumentException("Required chord does not exist.");
            if (inv > 2)
                throw new IllegalArgumentException("Triad do not have inversion higher than 2.");
            if ((num == 7 || num == 2) && getName().equals("Aeolian") && inv == 0)
                throw new IllegalArgumentException("In classical music theory, root position diminished chord is not allowed.");
            
            switch (num)
            {
            case 1:
                if (inv < 2)
                    return new ChordBuilder(3, voice).chord(triads.get(num - 1).inversion(inv));
                else
                    return new ChordBuilder()
                            .omitability(3, false)
                            .repeatPenalty(1, 500)
                            .repeatPenalty(3, 50)
                            .chord(triads.get(num - 1)
                                           .tendency(1, Note.Dir.Below, Interval.m2)
                                           .tendency(1, Note.Dir.Below, Interval.M2)
                                           .tendency(2, Note.Dir.Below, Interval.m2)
                                           .tendency(2, Note.Dir.Below, Interval.M2).inversion(inv));

            case 2:
                if (inv != 1)
                {
                    return new ChordBuilder()
                            .omitability(3, false)
                            .chord(triads.get(num - 1).inversion(inv));
                }
                else
                {
                    return new ChordBuilder()
                            .omitability(3, false)
                            .repeatPenalty(1, 500)
                            .repeatPenalty(2, 50)
                            .repeatPenalty(3, 1000)
                            .chord(triads.get(num - 1).inversion(inv));
                }

            case 3:
                return new ChordBuilder()
                        .omitability(3, false)
                        .repeatPenalty(1, 200)
                        .repeatPenalty(2, 50)
                        .chord(triads.get(num - 1).inversion(inv));

            case 4:
                return new ChordBuilder()
                        .omitability(3, false)
                        .chord(triads.get(num - 1).inversion(inv));

            case 5:
                return new ChordBuilder()
                        .repeatability(2, false)
                        .omitPenalty(3, 1000)
                        .unisonPenalty(1, 500)
                        .chord(triads.get(num - 1)
                                       .tendency(2, Note.Dir.Above, Interval.m2)
                                       .tendency(2, Note.Dir.Below, Interval.M3)
                                       .inversion(inv));

            case 6:
                return new ChordBuilder()
                        .omitability(3, false)
                        .repeatPenalty(2, 100)
                        .chord(triads.get(num - 1).inversion(inv));

            case 7:
                if (inv != 1)
                {
                    return new ChordBuilder()
                            .repeatability(1, false)
                            .omitability(3, false)
                            .chord(triads.get(num - 1).inversion(inv));
                }
                else
                {
                    return new ChordBuilder()
                            .repeatability(1, false)
                            .omitability(3, false)
                            .repeatPenalty(1, 500)
                            .repeatPenalty(2, 50)
                            .repeatPenalty(3, 1000)
                            .chord(triads.get(num - 1).inversion(inv));
                }
            default:
                return new ChordBuilder(3, voice).chord(triads.get(num));
            }
        }

        @Override public ChordBuilder getSeventh(int num, int inv, int voice)
        {
            if (num < 1 || num > sevenths.size())
                throw new IllegalArgumentException("Required chord does not exist.");
            if (inv > 3)
                throw new IllegalArgumentException("Seventh chord do not have inversion higher than 3.");
            
            ChordBuilder cb;
            switch (num)
            {
            case 2:
                cb = new ChordBuilder(4, 4)
                        .omitPenalty(3, 500)
                        .unisonPenalty(1, 500)
                        .chord(sevenths.get(num - 1)
                                       .preparedBy(4, Note.Dir.Above, Interval.P1)
                                       .preparedBy(4, Note.Dir.Above, Interval.m2)
                                       .preparedBy(4, Note.Dir.Above, Interval.M2)
                                       .tendency(4, Note.Dir.Below, Interval.m2)
                                       .tendency(4, Note.Dir.Below, Interval.M2)
                                       .inversion(inv));
                if (inv != 0)
                    cb.omitability(3, false);
                return cb;
            case 4:
                cb = new ChordBuilder(4, 4)
                        .omitPenalty(3, 500)
                        .unisonPenalty(1, 500)
                        .chord(sevenths.get(num - 1)
                                       .preparedBy(4, Note.Dir.Above, Interval.P1)
                                       .preparedBy(4, Note.Dir.Above, Interval.m2)
                                       .preparedBy(4, Note.Dir.Above, Interval.M2)
                                       .altTendency(4, Note.Dir.Below, Interval.m2)
                                       .altTendency(4, Note.Dir.Below, Interval.M2)
                                       .inversion(inv));
                if (inv != 0)
                    cb.omitability(3, false);
                return cb;
            case 5:
                return new ChordBuilder(4, 4)
                        .repeatability(2, false)
                        .omitPenalty(3, 500)
                        .unisonPenalty(1, 500)
                        .chord(sevenths.get(num - 1)
                                       .tendency(2, Note.Dir.Above, Interval.m2)
                                       .altTendency(2, Note.Dir.Below, Interval.M3)
                                       .tendency(4, Note.Dir.Below, Interval.m2)
                                       .tendency(4, Note.Dir.Below, Interval.M2)
                                       .inversion(inv));
            case 7:
                return new ChordBuilder(4, 4)
                        .repeatability(1, false)
                        .omitPenalty(3, 500)
                        .unisonPenalty(1, 500)
                        .chord(sevenths.get(num - 1)
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
            default:
                return new ChordBuilder(4, voice).chord(sevenths.get(num - 1));
            }
        }
    }
    
    public DiatonicScale(Note root)
    {
        super(root, new Interval[] { Interval.M2, Interval.M2, Interval.m2, Interval.M2, Interval.M2, Interval.M2, Interval.m2 });
    }

    @Override protected void build()
    {
        Note temproot = root;
        for (int i = 0; i < intervalSteps.length; i++)
        {
            ArrayList<Note> temptones = new ArrayList<>();
            temptones.add(Note.build(temproot));
            Note tempnote = temproot;
            for (int j = 0; j < intervalSteps.length - 1; j++)
            {
                tempnote = tempnote.intervalAbove(intervalSteps[(i + j) % intervalSteps.length]);
                temptones.add(tempnote);
            }
            String name = "";
            switch (i)
            {
            case 0: name = "Ionian"; break;
            case 1: name = "Dorian"; break;
            case 2: name = "Phrygian"; break;
            case 3: name = "Lydian"; break;
            case 4: name = "Mixolydian"; break;
            case 5: name = "Aeolian"; break;
            case 6: name = "Locian"; break;
            default: name = i + ""; break;
            }
            modes.put(name, new DiatonicMode(temptones));
            
            if (i == 5)
            {
                DiatonicMode mode = new DiatonicMode(temptones);
                mode.triads.remove(4);
                mode.triads.add(4, new Chord.Builder(temptones.get(4), ChordStructure.M.ctor()));
                mode.triads.remove(6);
                mode.triads.add(6, new Chord.Builder(temptones.get(5).intervalAbove(Interval.a2), ChordStructure.dim.ctor()));
                mode.sevenths.remove(4);
                mode.sevenths.add(4, new Chord.Builder(temptones.get(4), ChordStructure.Mm7.ctor()));
                mode.sevenths.remove(6);
                mode.sevenths.add(6, new Chord.Builder(temptones.get(5).intervalAbove(Interval.a2), ChordStructure.oo7.ctor()));
                modes.put("HarmonicMinor", mode);
            }
        }
    }
}
