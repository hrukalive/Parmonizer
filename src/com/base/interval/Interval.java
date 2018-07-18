package com.base.interval;

import com.base.Tuple;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Concept of Interval. Providing functions to check the properties
 * and to transform the interval.
 * 
 * Created by NyLP on 6/15/17.
 */

public final class Interval
{
    public static final Interval P1 = Interval.parse("P1");
    public static final Interval A1 = Interval.parse("A1");
    public static final Interval d2 = Interval.parse("d2");
    public static final Interval m2 = Interval.parse("m2");
    public static final Interval M2 = Interval.parse("M2");
    public static final Interval A2 = Interval.parse("A2");
    public static final Interval d3 = Interval.parse("d3");
    public static final Interval m3 = Interval.parse("m3");
    public static final Interval M3 = Interval.parse("M3");
    public static final Interval A3 = Interval.parse("A3");
    public static final Interval d4 = Interval.parse("d4");
    public static final Interval P4 = Interval.parse("P4");
    public static final Interval A4 = Interval.parse("A4");
    public static final Interval d5 = Interval.parse("d5");
    public static final Interval P5 = Interval.parse("P5");
    public static final Interval A5 = Interval.parse("A5");
    public static final Interval d6 = Interval.parse("d6");
    public static final Interval m6 = Interval.parse("m6");
    public static final Interval M6 = Interval.parse("M6");
    public static final Interval A6 = Interval.parse("A6");
    public static final Interval d7 = Interval.parse("d7");
    public static final Interval m7 = Interval.parse("m7");
    public static final Interval M7 = Interval.parse("M7");
    public static final Interval A7 = Interval.parse("A7");
    public static final Interval d8 = Interval.parse("d8");
    public static final Interval P8 = Interval.parse("P8");
    public static final Interval A8 = Interval.parse("A8");
    public static final Interval d9 = Interval.parse("d9");
    public static final Interval m9 = Interval.parse("m9");
    public static final Interval M9 = Interval.parse("M9");
    public static final Interval A9 = Interval.parse("A9");
    public static final Interval d10 = Interval.parse("d10");
    public static final Interval m10 = Interval.parse("m10");
    public static final Interval M10 = Interval.parse("M10");
    public static final Interval A10 = Interval.parse("A10");
    public static final Interval d11 = Interval.parse("d11");
    public static final Interval P11 = Interval.parse("P11");
    public static final Interval A11 = Interval.parse("A11");
    public static final Interval d12 = Interval.parse("d12");
    public static final Interval P12 = Interval.parse("P12");
    public static final Interval A12 = Interval.parse("A12");
    public static final Interval d13 = Interval.parse("d13");
    public static final Interval m13 = Interval.parse("m13");
    public static final Interval M13 = Interval.parse("M13");
    public static final Interval A13 = Interval.parse("A13");
    /**
     * Interval direction and quality enum
     */
    public enum Dir
    {
        Above, Below;

        public Dir reverse()
        {
            if (this == Above)
                return Below;
            else
                return Above;
        }

        @Override
        public String toString() {
            if (this == Above)
                return "+";
            else
                return "-";
        }
    }

    private enum Quality
    {
        Dim, Min, Maj, Aug, Per;

        @Override
        public String toString() {
            switch (this)
            {
                case Per: return "P";
                case Maj: return "M";
                case Min: return "m";
                case Aug: return "A";
                case Dim: return "d";
                default: throw new InternalError("Quality is wrong.");
            }
        }
    }

    /**
     * Initialization of common intervals
     */
    private static final HashMap<String, Interval> _intv_str_map = new HashMap<>();
    private static final HashMap<Tuple<Integer, Integer>, Interval> _intv_degdist_map = new HashMap<>();

    static
    {
        Interval[] majorIntervals = {
                new Interval(1, 0, Quality.Per),
                new Interval(2, 2, Quality.Maj),
                new Interval(3, 4, Quality.Maj),
                new Interval(4, 5, Quality.Per),
                new Interval(5, 7, Quality.Per),
                new Interval(6, 9, Quality.Maj),
                new Interval(7, 11, Quality.Maj)
        };
        for (Interval interval : majorIntervals)
        {
            int degree = interval._degree;
            int semitones = interval._semitones;
            Quality quality = interval._quality;
            String qualityStr = quality.toString();
            String octStr = "" + (degree + 7);
            _intv_str_map.put(qualityStr + degree, interval);
            _intv_str_map.put(qualityStr + octStr, new Interval(degree + 7, semitones + 12, quality));
            if (quality == Quality.Per)
            {
                if (semitones > 0) {
                    _intv_str_map.put("d" + degree, new Interval(degree, semitones - 1, Quality.Dim));
                    _intv_str_map.put("d" + octStr, new Interval(degree + 7, semitones - 1 + 12, Quality.Dim));
                }
                _intv_str_map.put("A" + degree, new Interval(degree, semitones + 1, Quality.Aug));
                _intv_str_map.put("A" + octStr, new Interval(degree + 7, semitones + 1 + 12, Quality.Aug));
            }
            else if (quality == Quality.Maj)
            {
                _intv_str_map.put("m" + degree, new Interval(degree, semitones - 1, Quality.Min));
                _intv_str_map.put("A" + degree, new Interval(degree, semitones + 1, Quality.Aug));
                _intv_str_map.put("d" + degree, new Interval(degree, semitones - 2, Quality.Dim));
                if (degree < 7) {
                    _intv_str_map.put("d" + octStr, new Interval(degree + 7, semitones - 2 + 12, Quality.Dim));
                    _intv_str_map.put("m" + octStr, new Interval(degree + 7, semitones - 1 + 12, Quality.Min));
                    _intv_str_map.put("A" + octStr, new Interval(degree + 7, semitones + 1 + 12, Quality.Aug));
                }
            }
        }

        for (Interval interval : _intv_str_map.values())
        {
            _intv_degdist_map.put(new Tuple<>(interval._degree, interval._semitones), interval);
        }
    }

    /**
     * Member and construction
     */
    private final int _degree;
    private final int _semitones;
    private Dir _dir = Dir.Above;
    private final Quality _quality;
    
    private Interval(int degree, int semitones, Quality type)
    {
        this._degree = degree;
        this._semitones = semitones;
        this._quality = type;
    }
    
    private Interval(Interval other)
    {
        this(other._degree, other._semitones, other._quality);
    }

    public static Interval parse(String interval)
    {
        Matcher matcher = Pattern.compile("(\\+|-)?((P|M|m|a|A|d|D)(\\d+))").matcher(interval);
        if (matcher.find())
        {
            String nameStr = matcher.group(2);
            if (!_intv_str_map.containsKey(nameStr))
                throw new IllegalArgumentException("Parsing interval failed.");
            Interval ret = new Interval(_intv_str_map.get(nameStr));
            
            String dirStr = matcher.group(1);
            if (dirStr != null)
            {
                if (dirStr.equals("+"))
                    ret.setDir(Dir.Above);
                else if (dirStr.equals("-"))
                    ret.setDir(Dir.Below);
            }
            return ret;
        }
        throw new IllegalArgumentException("Parsing interval failed.");
    }
    
    public static Interval parse(int deg, int dist)
    {
        Tuple<Integer, Integer> tuple = new Tuple<>(deg, dist);
        if (_intv_degdist_map.containsKey(tuple))
            return _intv_degdist_map.get(tuple);
        throw new IllegalArgumentException("Cannot determine specific quality.");
    }

    /**
     * Getters and setters
     */
    public int semitones()
    {
        return _semitones;
    }
    public int degree() { return _degree; }
    public Dir dir()
    {
        return _dir;
    }

    public void setDir(Dir dir)
    {
        this._dir = dir;
    }

    public boolean isDiminished() { return _quality.equals(Quality.Dim); }
    public boolean isMinor() { return _quality.equals(Quality.Min); }
    public boolean isMajor() { return _quality.equals(Quality.Maj); }
    public boolean isAugmented() { return _quality.equals(Quality.Aug); }
    public boolean isPerfect() { return _quality.equals(Quality.Per); }

    /**
     * Operations
     */
    public Interval invert()
    {
        if (_degree > 8)
            throw new IllegalArgumentException("Compound interval inversion is not supported.");
        return parse(9 - _degree, 12 - _semitones);
    }

    public Interval reverse()
    {
        Interval ret = new Interval(this);
        if (ret._dir.equals(Dir.Above))
            ret._dir = Dir.Below;
        else
            ret._dir = Dir.Above;
        return ret;
    }

    @Override public boolean equals(Object obj)
    {
        return obj instanceof Interval && ((Interval)obj)._degree == _degree && ((Interval)obj)._semitones == _semitones;
    }

    @Override public String toString()
    {
        return _dir.toString() + _quality.toString() + _degree;
    }
}
