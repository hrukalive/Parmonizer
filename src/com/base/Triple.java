package com.base;

/**
 * Data Structure for a Triple.
 * 
 * Created by NyLP on 7/18/17.
 */

public class Triple<T, U, V>
{
    private final T first;
    private final U second;
    private final V third;

    public Triple(T first, U second, V third)
    {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public T getFirst()
    {
        return first;
    }

    public U getSecond()
    {
        return second;
    }
    
    public V getThird()
    {
        return third;
    }

    @Override public int hashCode()
    {
        return 29 * 29 * first.hashCode() + 29 * second.hashCode() + third.hashCode();
    }
    
    @Override public boolean equals(Object obj)
    {
        if (obj instanceof Triple)
        {
            Triple other = (Triple)obj;
            return first.equals(other.first) && second.equals(other.second) && third.equals(other.third);
        }
        return false;
    }
}
