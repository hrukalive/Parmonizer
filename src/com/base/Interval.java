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
    public enum Dir
    {
        Above, Below;

        public Dir reverse()
        {
            if (this == Above)
                return Below;
            else if (this == Below)
                return Above;
            return null;
        }
    }

    private enum _Type
    {
        Dim, Min, Maj, Aug, Per
    }
    
    private enum _Interval
    {
        P1(new Interval("P1", 1, 0, _Type.Per)),
        A1(new Interval("A1", 1, 1, _Type.Aug)),

        d2(new Interval("d2", 2, 0, _Type.Dim)),
        m2(new Interval("m2", 2, 1, _Type.Min)),
        M2(new Interval("M2", 2, 2, _Type.Maj)),
        A2(new Interval("A2", 2, 3, _Type.Aug)),
        
        d3(new Interval("d3", 3, 2, _Type.Dim)),
        m3(new Interval("m3", 3, 3, _Type.Min)),
        M3(new Interval("M3", 3, 4, _Type.Maj)),
        A3(new Interval("A3", 3, 5, _Type.Aug)),
        
        d4(new Interval("d4", 4, 4, _Type.Dim)),
        P4(new Interval("P4", 4, 5, _Type.Per)),
        A4(new Interval("A4", 4, 6, _Type.Aug)),
        
        d5(new Interval("d5", 5, 6, _Type.Dim)),
        P5(new Interval("P5", 5, 7, _Type.Per)),
        A5(new Interval("A5", 5, 8, _Type.Aug)),
        
        d6(new Interval("d6", 6, 7, _Type.Dim)),
        m6(new Interval("m6", 6, 8, _Type.Min)),
        M6(new Interval("M6", 6, 9, _Type.Maj)),
        A6(new Interval("A6", 6, 10, _Type.Aug)),
        
        d7(new Interval("d7", 7, 9, _Type.Dim)),
        m7(new Interval("m7", 7, 10, _Type.Min)),
        M7(new Interval("M7", 7, 11, _Type.Maj)),
        A7(new Interval("A7", 7, 12, _Type.Aug)),
        
        d8(new Interval("d8", 8, 11, _Type.Dim)),
        P8(new Interval("P8", 8, 12, _Type.Per)),
        
        m9(new Interval("m9", 9, 13, _Type.Min)),
        M9(new Interval("M9", 9, 14, _Type.Maj)),
        m10(new Interval("m10", 10, 15, _Type.Min)),
        M10(new Interval("M10", 10, 16, _Type.Maj)),
        P11(new Interval("P11", 11, 17, _Type.Per)),
        m13(new Interval("m13", 13, 20, _Type.Min)),
        M13(new Interval("M13", 13, 21, _Type.Maj));
        
        private final Interval _intv;
        _Interval(Interval _intv)
        {
            this._intv = _intv;
        }
    }
    
    private static final HashMap<String, Interval> _intv_str_map = new HashMap<>();
    private static final HashMap<Tuple<Integer, Integer>, Interval> _intv_degdist_map = new HashMap<>();

    static
    {
        for (_Interval interval : _Interval.values())
        {
            _intv_str_map.put(interval._intv.getName(), interval._intv);
            _intv_degdist_map.put(new Tuple<>(interval._intv._degree, interval._intv._semitones), interval._intv);
        }
    }

    private final String _name;
    private final int _degree;
    private final int _semitones;
    private Dir _dir = Dir.Above;
    private final _Type _type;

    private Interval(String name, int degree, int semitones, _Type type)
    {
        this._name = name;
        this._degree = degree;
        this._semitones = semitones;
        this._type = type;
    }
    
    private Interval(int degree, int semitones, _Type type)
    {
        this._degree = degree;
        this._semitones = semitones;
        this._type = type;
        this._name = toString().substring(1);
    }
    
    private Interval(Interval other)
    {
        this(other._name, other._degree, other._semitones, other._type);
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
    
    public static Interval get(int deg, int dist)
    {
        Tuple<Integer, Integer> tuple = new Tuple<>(deg, dist);
        if (_intv_degdist_map.containsKey(tuple))
            return _intv_degdist_map.get(tuple);
        throw new IllegalArgumentException("Cannot determine specific _type.");
    }

    public int semitones()
    {
        return _semitones;
    }
    public int degree() { return _degree - 1; }
    public Dir dir()
    {
        return _dir;
    }
    
    public String getName()
    {
        return _name;
    }
    
    public void setDir(Dir dir)
    {
        this._dir = dir;
    }

    
    public boolean isDiminished() { return _type.equals(_Type.Dim); }
    public boolean isMinor() { return _type.equals(_Type.Min); }
    public boolean isMajor() { return _type.equals(_Type.Maj); }
    public boolean isAugmented() { return _type.equals(_Type.Aug); }
    
    public Interval invert()
    {
        if (_degree > 8)
            throw new IllegalArgumentException("Compound interval inversion is not supported.");
        return get(9 - _degree, 12 - _semitones);
    }

    @Override public boolean equals(Object obj)
    {
        return obj instanceof Interval && ((Interval)obj)._degree == _degree && ((Interval)obj)._semitones == _semitones;
    }

    @Override public String toString()
    {
        String ret = "";
        if (_dir == Dir.Above)
            ret = "+";
        else if (_dir == Dir.Below)
            ret = "-";
        switch (_type)
        {
        case Per: ret += "P"; break;
        case Maj: ret += "M"; break;
        case Min: ret += "m"; break;
        case Aug: ret += "A"; break;
        case Dim: ret += "d"; break;
        default: throw new InternalError("_Type is wrong.");
        }
        ret += _degree;
        return ret;
    }
}
