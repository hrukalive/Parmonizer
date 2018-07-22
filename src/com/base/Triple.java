package com.base;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

    public static <T, U, V> List<Triple<T, U, V>> zip(List<T> l1, List<U> l2, List<V> l3) {
        Iterator<T> it1 = l1.iterator();
        Iterator<U> it2 = l2.iterator();
        Iterator<V> it3 = l3.iterator();
        List<Triple<T, U, V>> ret = new ArrayList<>();
        while (it1.hasNext() && it2.hasNext() && it3.hasNext()) {
            ret.add(new Triple<>(it1.next(), it2.next(), it3.next()));
        }
        return ret;
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
