package com.common;

/**
 * Concept of Interval. Providing functions to check the properties
 * and to transform the interval.
 * 
 * Created by NyLP on 6/15/17.
 */

public final class Interval
{
    private enum Type
    { Dim, Min, Maj, Aug, Per }
    public static final Interval P1 = new Interval(1, 0, Type.Per);
    public static final Interval a1 = new Interval(1, 1, Type.Aug);
    public static final Interval m2 = new Interval(2, 1, Type.Min);
    public static final Interval M2 = new Interval(2, 2, Type.Maj);
    public static final Interval d3 = new Interval(3, 2, Type.Dim);
    public static final Interval a2 = new Interval(2, 3, Type.Aug);
    public static final Interval m3 = new Interval(3, 3, Type.Min);
    public static final Interval M3 = new Interval(3, 4, Type.Maj);
    public static final Interval d4 = new Interval(4, 4, Type.Dim);
    public static final Interval a3 = new Interval(3, 5, Type.Aug);
    public static final Interval P4 = new Interval(4, 5, Type.Per);
    public static final Interval a4 = new Interval(4, 6, Type.Aug);
    public static final Interval d5 = new Interval(5, 6, Type.Dim);
    public static final Interval P5 = new Interval(5, 7, Type.Per);
    public static final Interval d6 = new Interval(6, 7, Type.Dim);
    public static final Interval a5 = new Interval(5, 8, Type.Aug);
    public static final Interval m6 = new Interval(6, 8, Type.Min);
    public static final Interval M6 = new Interval(6, 9, Type.Maj);
    public static final Interval d7 = new Interval(7, 9, Type.Dim);
    public static final Interval a6 = new Interval(6, 10, Type.Aug);
    public static final Interval m7 = new Interval(7, 10, Type.Min);
    public static final Interval M7 = new Interval(7, 11, Type.Maj);
    public static final Interval d8 = new Interval(8, 11, Type.Dim);
    public static final Interval a7 = new Interval(7, 12, Type.Aug);
    public static final Interval P8 = new Interval(8, 12, Type.Per);
    public static final Interval m9 = new Interval(9, 13, Type.Min);
    public static final Interval M9 = new Interval(9, 14, Type.Maj);
    public static final Interval m10 = new Interval(10, 15, Type.Min);
    public static final Interval M10 = new Interval(10, 16, Type.Maj);
    public static final Interval P11 = new Interval(11, 17, Type.Per);
    public static final Interval m13 = new Interval(13, 20, Type.Min);
    public static final Interval M13 = new Interval(13, 21, Type.Maj);
    private static final Interval[] inventory = {P1, a1, m2, M2, d3, a2, m3, M3, d4, a3, P4, a4, d5, P5, d6, a5, m6, M6, d7, a6, m7, M7, d8, a7, P8, m9, M9, m10, M10, P11, m13, M13};

    private final int deg;
    private final int dist;
    private final Type type;
    
    private Interval(int deg, int dist, Type type) { this.deg = deg; this.dist = dist; this.type = type; }
    
    public static Interval build(int deg, int dist)
    {
        Type type = Type.Per;
        boolean flag = false;
        for (Interval intv: inventory)
        {
            if (intv.deg == deg && intv.dist == dist)
            {
                type = intv.type;
                flag = true;
                break;
            }
        }
        if (!flag)
            throw new IllegalArgumentException("Cannot determine specific type.");
        return new Interval(deg, dist, type);
    }

    public int semitones()
    {
        return dist;
    }
    public int degree() { return deg - 1; }
    public boolean isDiminished() { return type.equals(Type.Dim); }
    public boolean isMinor() { return type.equals(Type.Min); }
    public boolean isMajor() { return type.equals(Type.Maj); }
    public boolean isAugmented() { return type.equals(Type.Aug); }
    
    public Interval invert()
    {
        if (deg > 8)
            throw new IllegalArgumentException("Compound interval inversion is not supported.");
        
        if (type == Type.Per)
            return new Interval(9 - deg, 12 - dist, Type.Per);
        if (type == Type.Min)
            return new Interval(9 - deg, 12 - dist, Type.Maj);
        if (type == Type.Maj)
            return new Interval(9 - deg, 12 - dist, Type.Min);
        if (type == Type.Dim)
            return new Interval(9 - deg, 12 - dist, Type.Aug);
        if (type == Type.Aug)
            return new Interval(9 - deg, 12 - dist, Type.Dim);
        return null;
    }

    @Override public boolean equals(Object obj)
    {
        if (obj instanceof Interval)
        {
            return ((Interval)obj).deg == deg && ((Interval)obj).dist == dist;
        }
        return false;
    }
}
