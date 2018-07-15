package com.base;

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

    @Override public boolean equals(Object obj)
    {
        return obj instanceof Interval && ((Interval)obj)._degree == _degree && ((Interval)obj)._semitones == _semitones;
    }

    @Override public String toString()
    {
        return _dir.toString() + _quality.toString() + _degree;
    }
}
