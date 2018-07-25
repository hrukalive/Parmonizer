package com.base.chord;

import com.base.interval.Interval;

public class DiatonicChordProperty {
    public static void apply(Chord.Builder builder, Interval interval) {
        int noteNumber = builder.getNoteNumber();
        int inversion = builder.getInversion();
        ChordType type = builder.getType();
        if (interval == null)
            return;

        if (noteNumber == 3) {
            if (interval.equals(Interval.P5) && type.isMajor()) {
                builder.tendency(1, Interval.m2)
                        .altTendency(1, Interval.M3.reverse());
            } else if (interval.equals(Interval.M7) && type.isDiminished()) {
                builder.tendency(0, Interval.m2)
                        .tendency(1, Interval.M2)
                        .altTendency(1, Interval.M2.reverse())
                        .tendency(2, Interval.m2.reverse());
            }
        }
        if (noteNumber > 3) {
            if (type.isDominant()) {
                builder.tendency(1, Interval.m2)
                        .altTendency(1, Interval.M3.reverse())
                        .tendency(3, Interval.m2.reverse())
                        .tendency(3, Interval.M2.reverse());
            } else if (interval.equals(Interval.M2) && (type.isMinor() || type.isHalfDiminished())) {
                builder.preparedBy(3, Interval.P1)
                        .preparedBy(3, Interval.m2)
                        .preparedBy(3, Interval.M2)
                        .tendency(3, Interval.m2.reverse())
                        .tendency(3, Interval.M2.reverse());
            } else if (interval.equals(Interval.P4) && (type.isMajor() || type.isMinor())) {
                builder.preparedBy(3, Interval.P1)
                        .preparedBy(3, Interval.m2)
                        .preparedBy(3, Interval.M2)
                        .altTendency(3, Interval.m2.reverse())
                        .altTendency(3, Interval.M2.reverse());
            } else if (interval.equals(Interval.M7) && (type.isHalfDiminished() || type.isFullyDiminished())) {
                builder.preparedBy(3, Interval.P1)
                        .preparedBy(3, Interval.m2)
                        .preparedBy(3, Interval.M2)
                        .preparedBy(3, Interval.m2.reverse())
                        .preparedBy(3, Interval.M2.reverse())
                        .tendency(0, Interval.m2)
                        .tendency(1, Interval.M2)
                        .altTendency(1, Interval.M2.reverse())
                        .tendency(2, Interval.m2.reverse())
                        .tendency(3, Interval.m2.reverse());
            }
        }
    }
}
