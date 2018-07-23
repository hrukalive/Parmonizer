package com.base.scale;

import com.base.interval.Interval;
import com.base.Note;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Implement the concept of scale. (Need rethinking because of
 * the altered note in some chords, differentiating upward and
 * downward noteSet, generator difference in negative harmony).
 * <p>
 * Created by NyLP on 7/4/17.
 */

public class Scale {
    private static final Interval[] MAJOR = { Interval.M2, Interval.M2, Interval.m2, Interval.M2, Interval.M2, Interval.M2, Interval.m2 };
    private static final Interval[] HARMONIC_MINOR = { Interval.M2, Interval.m2, Interval.M2, Interval.M2, Interval.m2, Interval.A2, Interval.m2 };
    private static final Interval[] MELODIC_MINOR = { Interval.M2, Interval.m2, Interval.M2, Interval.M2, Interval.M2, Interval.M2, Interval.m2 };
    private static final Interval[] HARMONIC_MAJOR = { Interval.M2, Interval.M2, Interval.m2, Interval.M2, Interval.m2, Interval.A2, Interval.m2 };
    private static final Interval[] PENTATONIC = { Interval.M2, Interval.M2, Interval.m3, Interval.M2, Interval.m3 };

    private final Note tonic;
    private final List<Interval> intervalSteps;
    private Interval negativeGenInterval = Interval.parse("P5");

    private boolean allowRespell = false;

    private final ArrayList<Mode> modes = new ArrayList<>();
    private final HashMap<String, Integer> modeAliasMap = new HashMap<>();

    public Scale(Note tonic, Interval... intervalSteps) {
        this.tonic = tonic;
        this.intervalSteps = Arrays.asList(intervalSteps);
        build();
    }

    public Scale(Note tonic, Interval negativeGenInterval, Interval... intervalSteps) {
        this(tonic, intervalSteps);
        this.negativeGenInterval = negativeGenInterval;
        build();
    }

    private void build() {
        for (int i = 0; i < intervalSteps.size(); i++) {
            Mode mode = new Mode(tonic, i, intervalSteps, allowRespell, negativeGenInterval);
            modes.add(mode);
        }
    }

    public void allowRespell() {
        allowRespell = true;
    }

    public void setModeName(int index, String name) {
        if (index < 0 || index > modes.size())
            throw new IllegalArgumentException("Selected mode does not exist.");
        modeAliasMap.put(name, index);
    }

    public Mode getScale() {
        return modes.get(0);
    }

    public Mode getMode(int index) {
        return modes.get(index);
    }

    public Mode getMode(String name) {
        return modes.get(modeAliasMap.get(name));
    }
}
