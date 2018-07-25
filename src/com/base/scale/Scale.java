package com.base.scale;

import com.base.interval.Interval;
import com.base.Note;

import java.util.*;

/**
 * Implement the concept of scale. (Need rethinking because of
 * the altered note in some chords, differentiating upward and
 * downward noteSet, generator difference in negative harmony).
 * <p>
 * Created by NyLP on 7/4/17.
 */

public class Scale {
    private static final Interval[] MAJOR_INTV = { Interval.M2, Interval.M2, Interval.m2, Interval.M2, Interval.M2, Interval.M2, Interval.m2 };
    private static final Interval[] HARMONIC_MINOR_INTV = { Interval.M2, Interval.m2, Interval.M2, Interval.M2, Interval.m2, Interval.A2, Interval.m2 };
    private static final Interval[] MELODIC_MINOR_INTV = { Interval.M2, Interval.m2, Interval.M2, Interval.M2, Interval.M2, Interval.M2, Interval.m2 };
    private static final Interval[] HARMONIC_MAJOR_INTV = { Interval.M2, Interval.M2, Interval.m2, Interval.M2, Interval.m2, Interval.A2, Interval.m2 };
    private static final Interval[] PENTATONIC_INTV = { Interval.M2, Interval.M2, Interval.m3, Interval.M2, Interval.m3 };
    private static final Interval[] WHOLETONE_INTV = { Interval.M2, Interval.M2, Interval.M2, Interval.M2, Interval.M2, Interval.M2 };

    private static final Map<String, Mode> mode_name_map = new HashMap<>();

    static {
        Scale tmp = new Scale(MAJOR_INTV);
        mode_name_map.put("Ionian", tmp.getMode(0));
        mode_name_map.put("Dorian", tmp.getMode(1));
        mode_name_map.put("Phrygian", tmp.getMode(2));
        mode_name_map.put("Lydian", tmp.getMode(3));
        mode_name_map.put("Mixolydian", tmp.getMode(4));
        mode_name_map.put("Aeolian", tmp.getMode(5));
        mode_name_map.put("Locrian", tmp.getMode(6));

        tmp = new Scale(HARMONIC_MINOR_INTV);
        mode_name_map.put("Harmonic Minor", tmp.getMode(0));
        mode_name_map.put("Locrian #6", tmp.getMode(1));
        mode_name_map.put("Ionian #5", tmp.getMode(2));
        mode_name_map.put("Dorian #4", tmp.getMode(3));
        mode_name_map.put("Phrygian Dom", tmp.getMode(4));
        mode_name_map.put("Lydian #2", tmp.getMode(5));
        mode_name_map.put("Superlocrian", tmp.getMode(6));

        tmp = new Scale(MELODIC_MINOR_INTV);
        mode_name_map.put("Melodic Minor", tmp.getMode(0));
        mode_name_map.put("Phrygian #6", tmp.getMode(1));
        mode_name_map.put("Lydian Aug", tmp.getMode(2));
        mode_name_map.put("Overtone", tmp.getMode(3));
        mode_name_map.put("Mixolydian b6", tmp.getMode(4));
        mode_name_map.put("Locrian #2", tmp.getMode(5));
        mode_name_map.put("Altered", tmp.getMode(6));

        tmp = new Scale(HARMONIC_MAJOR_INTV);
        mode_name_map.put("Harmonic Major", tmp.getMode(0));
        mode_name_map.put("Dorian b5", tmp.getMode(1));
        mode_name_map.put("Phrygian b4", tmp.getMode(2));
        mode_name_map.put("Lydian b3", tmp.getMode(3));
        mode_name_map.put("Mixolydian b9", tmp.getMode(4));
        mode_name_map.put("Lydian Aug #2", tmp.getMode(5));
        mode_name_map.put("Locrian bb7", tmp.getMode(6));

        tmp = new Scale(PENTATONIC_INTV);
        mode_name_map.put("Major Pentatonic", tmp.getMode(0));
        mode_name_map.put("Shang", tmp.getMode(1));
        mode_name_map.put("Jue", tmp.getMode(2));
        mode_name_map.put("Zhi", tmp.getMode(3));
        mode_name_map.put("Minor Pentatonic", tmp.getMode(4));

        tmp = new Scale(true, WHOLETONE_INTV);
        mode_name_map.put("Wholetone", tmp.getMode(0));
    }

    public static final Mode IONIAN = Scale.parse("Ionian");
    public static final Mode DORIAN = Scale.parse("Dorian");
    public static final Mode PHRYGIAN = Scale.parse("Phrygian");
    public static final Mode LYDIAN = Scale.parse("Lydian");
    public static final Mode MIXOLYDIAN = Scale.parse("Mixolydian");
    public static final Mode AEOLIAN = Scale.parse("Aeolian");
    public static final Mode LOCRIAN = Scale.parse("Locrian");

    public static final Mode HARMONIC_MINOR = Scale.parse("Harmonic Minor");
    public static final Mode LOCRIANa6 = Scale.parse("Locrian #6");
    public static final Mode IONIANa5 = Scale.parse("Ionian #5");
    public static final Mode DORIANa4 = Scale.parse("Dorian #4");
    public static final Mode PHRYGIAN_DOM = Scale.parse("Phrygian Dom");
    public static final Mode LYDIANa2 = Scale.parse("Lydian #2");
    public static final Mode SUPERLOCRIAN = Scale.parse("Superlocrian");

    public static final Mode MELODIC_MINOR = Scale.parse("Melodic Minor");
    public static final Mode PHRYGIANa6 = Scale.parse("Phrygian #6");
    public static final Mode LYDIAN_AUG = Scale.parse("Lydian Aug");
    public static final Mode OVERTONE = Scale.parse("Overtone");
    public static final Mode MIXOLYDIANb6 = Scale.parse("Mixolydian b6");
    public static final Mode LOCRIANa2 = Scale.parse("Locrian #2");
    public static final Mode ALTERED = Scale.parse("Altered");

    public static final Mode HARMONIC_MAJOR = Scale.parse("Harmonic Major");
    public static final Mode DORIANb5 = Scale.parse("Dorian b5");
    public static final Mode PHRYGIANb4 = Scale.parse("Phrygian b4");
    public static final Mode LYDIANb3 = Scale.parse("Lydian b3");
    public static final Mode MIXOLYDIANb9 = Scale.parse("Mixolydian b9");
    public static final Mode LYDIAN_AUGa2 = Scale.parse("Lydian Aug #2");
    public static final Mode LOCRIANbb7 = Scale.parse("Locrian bb7");

    public static final Mode MAJOR_PENTA = Scale.parse("Major Pentatonic");
    public static final Mode SHANG = Scale.parse("Shang");
    public static final Mode JUE = Scale.parse("Jue");
    public static final Mode ZHI = Scale.parse("Zhi");
    public static final Mode MINOR_PENTA = Scale.parse("Minor Pentatonic");

    public static final Mode WHOLE_TONE = Scale.parse("Wholetone");

    public static Mode parse(String scaleName) {
        if (!mode_name_map.containsKey(scaleName)) {
            throw new IllegalArgumentException("Scale name not found.");
        }
        return mode_name_map.get(scaleName);
    }


    private final List<Interval> intervalSteps;
    private Interval negativeGenInterval = Interval.parse("P5");

    private boolean allowRespell = false;

    private final ArrayList<Mode> modes = new ArrayList<>();
    private final HashMap<String, Integer> modeAliasMap = new HashMap<>();

    public Scale(Interval... intervalSteps) {
        this.intervalSteps = Arrays.asList(intervalSteps);
        build();
    }

    public Scale(boolean allowRespell, Interval... intervalSteps) {
        this.allowRespell = allowRespell;
        this.intervalSteps = Arrays.asList(intervalSteps);
        build();
    }

    public Scale(boolean allowRespell, Interval negativeGenInterval, Interval... intervalSteps) {
        this.allowRespell = allowRespell;
        this.intervalSteps = Arrays.asList(intervalSteps);
        this.negativeGenInterval = negativeGenInterval;
        build();
    }

    private void build() {
        for (int i = 0; i < intervalSteps.size(); i++) {
            Mode mode = new Mode(i, intervalSteps, allowRespell, negativeGenInterval);
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

    public Mode getMode(int index) {
        return modes.get(index);
    }

    public Mode getMode(String name) {
        return modes.get(modeAliasMap.get(name));
    }
}
