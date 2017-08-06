package com.base;

/**
 * Data Structure for a Tuple.
 * 
 * Created by NyLP on 7/17/17.
 */

public class Tuple<T, U>
{
    private final T first;
    private final U second;

    public Tuple(T first, U second)
    {
        this.first = first;
        this.second = second;
    }

    public T getFirst()
    {
        return first;
    }

    public U getSecond()
    {
        return second;
    }

    @Override public int hashCode()
    {
        return 29 * first.hashCode() + second.hashCode();
    }

    @Override public boolean equals(Object obj)
    {
        if (obj instanceof Tuple)
        {
            Tuple other = (Tuple)obj;
            return first.equals(other.first) && second.equals(other.second);
        }
        return false;
    }
}

