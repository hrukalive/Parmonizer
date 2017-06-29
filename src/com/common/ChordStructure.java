package com.common;

/**
 * Created by NyLP on 6/15/17.
 */

public enum ChordStructure
{
    M(new Interval[] {Interval.M3, Interval.P5}),
    m(new Interval[] {Interval.m3, Interval.P5}),
    dim(new Interval[] {Interval.m3, Interval.d5}),
    aug(new Interval[] {Interval.M3, Interval.a5}),

    MM7(new Interval[] {Interval.M3, Interval.P5, Interval.M7}),
    Mm7(new Interval[] {Interval.M3, Interval.P5, Interval.m7}),
    mM7(new Interval[] {Interval.m3, Interval.P5, Interval.M7}),
    mm7(new Interval[] {Interval.m3, Interval.P5, Interval.m7}),
    om7(new Interval[] {Interval.m3, Interval.d5, Interval.m7}),
    oo7(new Interval[] {Interval.m3, Interval.d5, Interval.d7});

    private final Interval[] comp;
    ChordStructure(Interval[] comp) { this.comp = comp; }

    public Interval[] ctor()
    {
        return comp;
    }
}
