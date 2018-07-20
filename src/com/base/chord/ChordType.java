package com.base.chord;

import com.base.interval.Interval;

import java.util.Arrays;
import java.util.List;

public enum ChordType {
    /**
     * maj 1 3 5
     * maj7 1 3 (5) 7
     * maj9 1 3 (5) 7 9
     * maj#11 1 3 (5) (7) (9) #11
     * maj13 1 3 (5) 7 (9) (#11) 13
     * 2 add2 add9 1 3 (5) 9
     * 6 add6 add13 1 3 (5) 6
     * 69 1 3 (5) 6 9
     * maj7#5 1 3 #5 7
     * maj7b6 1 3 (5) (7) (9) (b13), (#11)
     *
     * min 1 b3 5
     * min7 1 b3 (5) b7
     * minM7 1 b3 (5) 7 (9, b13, 13)
     * min6 1 b3 (5) 6
     * min9 1 b3 (5) b7 9
     * min11 1 b3 (5) b7 (9) 11
     * min13 1 b3 (5) b7 (9) 13
     * min7b5 1 b3 b5 b7
     * minb6 1 b3 (5) b6
     *
     * dim 1 b3 b5
     * dim7 1 b3 b5 bb7
     * aug 1 3 #5
     *
     * 7 1 3 (5) b7
     * 9 1 3 (5) b7 9
     * 13 1 3 (5) b7 (9) 13
     * 7b9 1 3 (5) b7 b9 (#9,b5,6..)
     * 7#9 1 3 (5) b7 #9
     * 7#11 1 3 (5) b7 #11 (9,13..)
     * 7alt 1 3 b7 (b9) (b5,b6,#9..)
     * 7sus 1 4 (5) b7
     * 11sus 1 (5) b7 (9) 11
     *
     * sus4 1 4 (5)
     * sus2 1 2 (5)
     * b9sus 1 4 (5) b9
     * 5 1 5
     */

    maj("maj", new Interval[] {Interval.M3, Interval.P5}),
    maj7("maj7", new Interval[] {Interval.M3, Interval.P5, Interval.M7}),
    maj9("maj9", new Interval[] {Interval.M3, Interval.P5, Interval.M7, Interval.M9}),
    majA11("maj#11", new Interval[] {Interval.M3, Interval.P5, Interval.M7, Interval.M9, Interval.A11}),
    maj13("maj13", new Interval[] {Interval.M3, Interval.P5, Interval.M7, Interval.M9, Interval.A11, Interval.M13}),
    add2("2", new Interval[] {Interval.M3, Interval.P5, Interval.M9}),
    add6("6", new Interval[] {Interval.M3, Interval.P5, Interval.M6}),
    six9("69", new Interval[] {Interval.M3, Interval.P5, Interval.M6, Interval.M9}),
    maj7A5("maj7#5", new Interval[] {Interval.M3, Interval.A5, Interval.M7}),
    maj7b6("maj7b6", new Interval[] {Interval.M3, Interval.P5, Interval.M7, Interval.M9, Interval.A11, Interval.d13}),

    min("min", new Interval[] {Interval.m3, Interval.P5}),
    min7("min7", new Interval[] {Interval.m3, Interval.P5, Interval.m7}),
    minM7("minM7", new Interval[] {Interval.m3, Interval.P5, Interval.M7, Interval.M9, Interval.d13, Interval.M13}),
    min6("min6", new Interval[] {Interval.m3, Interval.P5, Interval.M6}),
    min9("min9", new Interval[] {Interval.m3, Interval.P5, Interval.m7, Interval.M9}),
    min11("min11", new Interval[] {Interval.m3, Interval.P5, Interval.m7, Interval.M9, Interval.P11}),
    min13("min13", new Interval[] {Interval.m3, Interval.P5, Interval.m7, Interval.M9, Interval.M13}),
    min7b5("min7b5", new Interval[] {Interval.m3, Interval.d5, Interval.m7}),
    minb6("minb6", new Interval[] {Interval.m3, Interval.P5, Interval.m6}),

    dim("dim", new Interval[] {Interval.m3, Interval.d5}),
    dim7("dim7", new Interval[] {Interval.m3, Interval.d5, Interval.d7}),
    aug("aug", new Interval[] {Interval.M3, Interval.A5}),
    sus4("sus4", new Interval[] {Interval.P4, Interval.P5}),
    sus2("sus2", new Interval[] {Interval.M2, Interval.P5}),
    b9sus("b9sus", new Interval[] {Interval.P4, Interval.P5, Interval.d9}),
    power("5", new Interval[] {Interval.P5}),

    dom7("7", new Interval[] {Interval.M3, Interval.P5, Interval.m7}),
    dom9("9", new Interval[] {Interval.M3, Interval.P5, Interval.m7, Interval.M9}),
    dom13("13", new Interval[] {Interval.M3, Interval.P5, Interval.m7, Interval.M9, Interval.M13}),
    dom7b9("7b9", new Interval[] {Interval.M3, Interval.P5, Interval.m7, Interval.d9, Interval.A9, Interval.d5, Interval.M6}),
    dom7A9("7#9", new Interval[] {Interval.M3, Interval.P5, Interval.m7, Interval.A9}),
    dom7A11("7#11", new Interval[] {Interval.M3, Interval.P5, Interval.m7, Interval.A11, Interval.M9, Interval.M13}),
    dom7alt("7alt", new Interval[] {Interval.M3, Interval.m7, Interval.d9, Interval.d5, Interval.m6, Interval.A9}),
    dom7sus("7sus", new Interval[] {Interval.P4, Interval.P5, Interval.m7}),
    dom11sus("11sus", new Interval[] {Interval.P5, Interval.m7, Interval.M9, Interval.P11});

    private List<Interval> chordIntervals;
    private String name;
    ChordType(String name, Interval[] intervals)
    {
        this.name = name;
        chordIntervals = Arrays.asList(intervals);
    }

    public List<Interval> getIntervals() {
        return chordIntervals;
    }

    public String getName() {
        return name;
    }
}