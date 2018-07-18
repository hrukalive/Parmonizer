package com.base.chord;

import com.base.interval.Interval;

import java.util.Arrays;
import java.util.List;

public enum ChordType {
    maj(new Interval[] {Interval.M3, Interval.P5}),
    min(new Interval[] {Interval.m3, Interval.P5});

    private List<Interval> chordIntervals;
    ChordType(Interval[] intervals)
    {
        chordIntervals = Arrays.asList(intervals);
    }

    public List<Interval> getIntervals() {
        return chordIntervals;
    }
}
