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
     * maj7b6 1 3 (5) (7) (9) (#11) b13
     */

    maj("maj", new Interval[] {Interval.M3, Interval.P5},
            new Integer[] {-1, -1, 100}, new Integer[] {10, 50, 30}),
    maj7("maj7", new Interval[] {Interval.M3, Interval.P5, Interval.M7},
            new Integer[] {-1, -1, 100, -1}, new Integer[] {10, 50, 50, -1}),
    maj9("maj9", new Interval[] {Interval.M3, Interval.P5, Interval.M7, Interval.M9},
            new Integer[] {-1, -1, 40, -1, -1}, new Integer[] {10, 50, 50, -1, -1}),
    majA11("maj#11", new Interval[] {Interval.M3, Interval.P5, Interval.M7, Interval.M9, Interval.A11},
            new Integer[] {-1, -1, 20, 100, 90, -1}, new Integer[] {10, 50, 50, -1, -1, -1}),
    maj13("maj13", new Interval[] {Interval.M3, Interval.P5, Interval.M7, Interval.M9, Interval.A11, Interval.M13},
            new Integer[] {-1, -1, 20, -1, 100, 40, -1}, new Integer[] {10, 50, 50, -1, -1, -1, -1}),
    add2("2", new Interval[] {Interval.M3, Interval.P5, Interval.M9},
            new Integer[] {-1, -1, 100, -1}, new Integer[] {10, 50, 30, -1}),
    add6("6", new Interval[] {Interval.M3, Interval.P5, Interval.M6},
            new Integer[] {-1, -1, 100, -1}, new Integer[] {10, 50, 30, -1}),
    six9("69", new Interval[] {Interval.M3, Interval.P5, Interval.M6, Interval.M9},
            new Integer[] {-1, -1, 70, -1, -1}, new Integer[] {10, 50, 30, -1, -1}),
    maj7A5("maj7#5", new Interval[] {Interval.M3, Interval.A5, Interval.M7},
            new Integer[] {-1, -1, -1, -1}, new Integer[] {10, 50, -1, -1}),
    maj7b6("maj7b6", new Interval[] {Interval.M3, Interval.P5, Interval.M7, Interval.M9, Interval.A11, Interval.d13},
            new Integer[] {-1, -1, 30, 70, 80, 50, -1}, new Integer[] {10, 50, 30, -1, -1, -1, -1}),

    /**
     *
     * min 1 b3 5
     * min7 1 b3 (5) b7
     * minM7 1 b3 (5) 7 (9, b13, 13)
     * min6 1 b3 (5) 6
     * minb6 1 b3 5 b6
     * min9 1 b3 (5) b7 9
     * min11 1 b3 (5) b7 (9) 11
     * min13 1 b3 (5) b7 (9) 13
     * min7b5 1 b3 b5 b7
     */
    min("min", new Interval[] {Interval.m3, Interval.P5},
            new Integer[] {-1, -1, 100}, new Integer[] {10, 50, 30}),
    min7("min7", new Interval[] {Interval.m3, Interval.P5, Interval.m7},
            new Integer[] {-1, -1, 100, -1}, new Integer[] {10, 50, 30, -1}),
    minM7("minM7", new Interval[] {Interval.m3, Interval.P5, Interval.M7, Interval.M9, Interval.d13, Interval.M13},
            new Integer[] {-1, -1, 50, -1, 0, 0, 0}, new Integer[] {10, 50, 30, -1, -1, -1, -1}),
    min6("min6", new Interval[] {Interval.m3, Interval.P5, Interval.M6},
            new Integer[] {-1, -1, 100, -1}, new Integer[] {10, 50, 30, -1}),
    minb6("minb6", new Interval[] {Interval.m3, Interval.P5, Interval.m6},
            new Integer[] {-1, -1, -1, -1}, new Integer[] {10, 50, 30, -1}),
    min9("min9", new Interval[] {Interval.m3, Interval.P5, Interval.m7, Interval.M9},
            new Integer[] {-1, -1, 50, -1, -1}, new Integer[] {10, 50, 30, -1, -1}),
    min11("min11", new Interval[] {Interval.m3, Interval.P5, Interval.m7, Interval.M9, Interval.P11},
            new Integer[] {-1, -1, 40, -1, 100, -1}, new Integer[] {10, 50, 30, -1, -1, -1}),
    min13("min13", new Interval[] {Interval.m3, Interval.P5, Interval.m7, Interval.M9, Interval.M13},
            new Integer[] {-1, -1, 40, -1, 100, -1}, new Integer[] {10, 50, 30, -1, -1, -1}),
    min7b5("min7b5", new Interval[] {Interval.m3, Interval.d5, Interval.m7},
            new Integer[] {-1, 100, -1, -1}, new Integer[] {70, 50, 30, -1}),

    /*
     * dim 1 b3 b5
     * dim7 1 b3 b5 bb7
     * aug 1 3 #5
     * sus4 1 4 (5)
     * sus2 1 2 (5)
     * b9sus 1 4 (5) b9
     * 5 1 5
     */
    dim("dim", new Interval[] {Interval.m3, Interval.d5},
            new Integer[] {-1, -1, -1}, new Integer[] {-1, 30, -1}),
    dim7("dim7", new Interval[] {Interval.m3, Interval.d5, Interval.d7},
            new Integer[] {-1, -1, 100, -1}, new Integer[] {-1, 30, -1, -1}),
    aug("aug", new Interval[] {Interval.M3, Interval.A5},
            new Integer[] {-1, -1, -1}, new Integer[] {10, 10, 10}),
    sus4("sus4", new Interval[] {Interval.P4, Interval.P5},
            new Integer[] {-1, -1, 100}, new Integer[] {10, -1, 30}),
    sus2("sus2", new Interval[] {Interval.M2, Interval.P5},
            new Integer[] {-1, -1, 100}, new Integer[] {10, -1, 30}),
    b9sus("b9sus", new Interval[] {Interval.P4, Interval.P5, Interval.d9},
            new Integer[] {-1, -1, 100, -1}, new Integer[] {10, -1, 30, -1}),
    power("5", new Interval[] {Interval.P5},
            new Integer[] {-1, -1}, new Integer[] {10, 20}),

    /**
     * 7 1 3 (5) b7
     * 9 1 3 (5) b7 9
     * 13 1 3 (5) b7 (9) 13
     * 7b9 1 3 (5) b7 b9 (#9,b5,6..)
     * 7#9 1 3 (5) b7 #9
     * 7#11 1 3 (5) b7 #11 (9,13..)
     * 7alt 1 3 b7 (b9) (b5,b6,#9..)
     * 7sus 1 4 (5) b7
     * 11sus 1 (5) b7 (9) 11
     */
    dom7("7", new Interval[] {Interval.M3, Interval.P5, Interval.m7},
            new Integer[] {-1, -1, 100, -1}, new Integer[] {10, 50, 30, -1}),
    dom9("9", new Interval[] {Interval.M3, Interval.P5, Interval.m7, Interval.M9},
            new Integer[] {-1, -1, 50, -1, -1}, new Integer[] {10, 50, 30, -1, -1}),
    dom13("13", new Interval[] {Interval.M3, Interval.P5, Interval.m7, Interval.M9, Interval.M13},
            new Integer[] {-1, -1, 10, -1, 80, -1}, new Integer[] {10, 50, 30, -1, -1, -1}),
    dom7b9("7b9", new Interval[] {Interval.M3, Interval.P5, Interval.m7, Interval.d9, Interval.A9, Interval.d5, Interval.M6},
            new Integer[] {-1, -1, 10, -1, -1, 10, 10, 10}, new Integer[] {10, 50, 30, -1, -1, -1, -1, -1}),
    dom7A9("7#9", new Interval[] {Interval.M3, Interval.P5, Interval.m7, Interval.A9},
            new Integer[] {-1, -1, 10, -1, -1}, new Integer[] {10, 50, 30, -1, -1}),
    dom7A11("7#11", new Interval[] {Interval.M3, Interval.P5, Interval.m7, Interval.A11, Interval.M9, Interval.M13},
            new Integer[] {-1, -1, 10, -1, -1, 70, 60}, new Integer[] {10, 50, 30, -1, -1, -1, -1}),
    dom7alt("7alt", new Interval[] {Interval.M3, Interval.m7, Interval.d9, Interval.d5, Interval.m6, Interval.A9},
            new Integer[] {-1, -1, -1, 60, 60, 60, 60}, new Integer[] {10, 50, -1, -1, -1, -1, -1}),
    dom7sus("7sus", new Interval[] {Interval.P4, Interval.P5, Interval.m7},
            new Integer[] {-1, -1, 100, -1}, new Integer[] {10, 50, 30, -1}),
    dom11sus("11sus", new Interval[] {Interval.P5, Interval.m7, Interval.M9, Interval.P11},
            new Integer[] {-1, 50, -1, 80, -1}, new Integer[] {10, 30, -1, -1, -1});

    private List<Interval> chordIntervals;
    private List<Integer> omitPenalties;
    private List<Integer> repeatPenalties;
    private String name;
    ChordType(String name, Interval[] intervals, Integer[] omitPenalty, Integer[] repeatPenalty)
    {
        this.name = name;
        chordIntervals = Arrays.asList(intervals);
        omitPenalties = Arrays.asList(omitPenalty);
        repeatPenalties = Arrays.asList(repeatPenalty);
    }

    public String getName() {
        return name;
    }

    public List<Interval> getIntervals() {
        return chordIntervals;
    }

    public List<Integer> getOmitPenalties() {
        return omitPenalties;
    }

    public List<Integer> getRepeatPenalties() {
        return repeatPenalties;
    }
}