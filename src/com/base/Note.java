package com.base;

import com.common.Interval;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implemented the concept of a note.
 * Captured the difference of enharmonic notes. Providing functions
 * to transform and compare notes.
 * 
 * Created by NyLP on 6/12/17.
 */

public final class Note implements Comparable<Note>
{
    private final int noteCode;
    private final int octave;
    private final int alteration;
    private final ArrayList<Note> tendency;
    private final ArrayList<Note> altTendency;
    private final HashMap<Note, Integer> bonusMap;
    private final ArrayList<Note> prepareReq;
    public enum Dir
    { Above, Below }

    private Note(Note note)
    {
        this.noteCode = note.noteCode;
        this.octave = note.octave;
        this.alteration = note.alteration;
        this.tendency = new ArrayList<>(note.tendency);
        this.altTendency = new ArrayList<>(note.altTendency);
        this.bonusMap = new HashMap<>(note.bonusMap);
        this.prepareReq = new ArrayList<>(note.prepareReq);
    }
    private Note(int noteCode, int octave, int alteration)
    {
        this.noteCode = noteCode;
        this.octave = octave;
        this.alteration = alteration;
        this.tendency = new ArrayList<>();
        this.altTendency = new ArrayList<>();
        this.bonusMap = new HashMap<>();
        this.prepareReq = new ArrayList<>();
    }

    public void addTendency(Note note) { tendency.add(note); }
    
    public void addAltTendency(Note note) { altTendency.add(note); }
    
    public void addBonus(Note note, int value) { bonusMap.put(note, value); }
    
    public void addPrepare(Note note) { prepareReq.add(note); }
    
    public ArrayList<Note> getTendencies() { return tendency; }
    
    public ArrayList<Note> getAltTendency() { return altTendency; }
    
    public HashMap<Note, Integer> getBonus() { return bonusMap; }
    
    public ArrayList<Note> getPrepare() { return prepareReq; }

    public static Note build(Note note)
    {
        return new Note(note);
    }
    
    public static Note build(String noteName)
    {
        int code = -1;
        int alt = 0;
        Matcher note_matcher = Pattern.compile("[A-G](#|(bb)|x|b)?").matcher(noteName);
        Matcher oct_matcher = Pattern.compile("-?[0-9]+").matcher(noteName);
        if (!note_matcher.find())
            throw new IllegalArgumentException("Unable to parse the note name");
        String name = noteName.substring(note_matcher.start(), note_matcher.end());
        int oct = oct_matcher.find() ? Integer.parseInt(noteName.substring(oct_matcher.start(), oct_matcher.end())) : 0;

        switch (name.substring(0, 1))
        {
        case "C": code = 0; break;
        case "D": code = 1; break;
        case "E": code = 2; break;
        case "F": code = 3; break;
        case "G": code = 4; break;
        case "A": code = 5; break;
        case "B": code = 6; break;
        }
        switch(name.substring(1))
        {
        case "bb": alt = -2; break;
        case "b" : alt = -1; break;
        case "#" : alt = 1;  break;
        case "x" : alt = 2;  break;
        default  : alt = 0;  break;
        }

        return new Note(code, oct, alt);
    }
    
    public int dist(Note note) { return note.getCode() - this.getCode(); }
    
    private static int dist(int this_noteCode, int this_octave, int this_alteration, int that_noteCode, int that_octave, int that_alteration)
    { return getCode(that_noteCode, that_octave, that_alteration) - getCode(this_noteCode, this_octave, this_alteration); }
    
    public int distNoAlt(Note note)
    {
        return dist(note) - note.alteration;
    }
    
    public Interval interval(Note note)
    {
        if (compareTo(note) > 0)
            return note.interval(this).invert();
        int that_octave = note.octave;
        while (dist(noteCode, 0, alteration, note.noteCode, that_octave, note.alteration) > 12)
            that_octave--;
        int diff = note.noteCode - this.noteCode;
        return Interval.build((diff <= 0 ? (diff + that_octave * 7) : diff) + 1, dist(noteCode, 0, alteration, note.noteCode, that_octave, note.alteration));
    }
    
    public Note intervalAbove(Interval intv)
    {
        int temp_noteCode = noteCode + intv.degree();
        int temp_octave = octave + temp_noteCode / 7;
        temp_noteCode %= 7;
        int temp_alteration = intv.semitones() - (getCode(temp_noteCode, temp_octave, 0) - this.getCode());
        return new Note(temp_noteCode, temp_octave, temp_alteration);
    }
    
    public Note intervalBelow(Interval intv)
    {
        Interval intv_inv = intv.invert();
        int temp_noteCode = noteCode + intv_inv.degree();
        int temp_octave = octave + temp_noteCode / 7;
        temp_noteCode %= 7;
        int temp_alteration = intv_inv.semitones() - (getCode(temp_noteCode, temp_octave, 0) - this.getCode());
        return new Note(temp_noteCode, temp_octave - 1, temp_alteration);
    }

    public ArrayList<Note> allInRange(Note lo, Note hi)
    {
        ArrayList<Note> ret = new ArrayList<>();
        int temp_octave = 0;
        while (lo.compareTo(noteCode, temp_octave, alteration) > 0) temp_octave++;
        while (hi.compareTo(noteCode, temp_octave, alteration) >= 0)
        {
            ret.add(new Note(noteCode, temp_octave, alteration));
            temp_octave++;
        }
        return ret;
    }

    public int getCode()
    {
        return getCode(noteCode, octave, alteration);
    }
    
    private static int getCode(int noteCode, int octave, int alteration)
    {
        int ret = 0;
        switch (noteCode)
        {
        case 0: ret = 0; break;
        case 1: ret = 2; break;
        case 2: ret = 4; break;
        case 3: ret = 5; break;
        case 4: ret = 7; break;
        case 5: ret = 9; break;
        case 6: ret = 11; break;
        }
        ret += octave * 12 + 12;
        ret += alteration;
        return ret;
    }

    @Override public boolean equals(Object obj)
    {
        if (obj instanceof Note)
        {
            return ((Note)obj).noteCode == this.noteCode &&
                    ((Note)obj).octave == this.octave &&
                    ((Note)obj).alteration == this.alteration;
        }
        return false;
    }
    
    public boolean isEnharmonic(Note o)
    {
        return this.getCode() == o.getCode();
    }
    
    public boolean isEnharmonicNoClass(Note o) { return (this.getCode() % 12) == (o.getCode() % 12); }
    
    public boolean isAugmented(Note note)
    {
        if (compareTo(note) > 0)
            return note.isAugmented(this);
        return interval(note).isAugmented();
    }
    
    public boolean isMajor(Note note)
    {
        if (compareTo(note) > 0)
            return note.isMajor(this);
        return interval(note).isMajor();
    }
    
    public boolean isDiminished(Note note)
    {
        if (compareTo(note) > 0)
            return note.isDiminished(this);
        return interval(note).isDiminished();
    }
    
    public boolean isMinor(Note note)
    {
        if (compareTo(note) > 0)
            return note.isMinor(this);
        return interval(note).isMinor();
    }

    @Override public String toString()
    {
        return toString(true);
    }

    public String toString(boolean includeOct)
    {
        String ret = "";
        switch (noteCode)
        {
        case 0:  ret += "C"; break;
        case 1:  ret += "D"; break;
        case 2:  ret += "E"; break;
        case 3:  ret += "F"; break;
        case 4:  ret += "G"; break;
        case 5:  ret += "A"; break;
        case 6:  ret += "B"; break;
        default: throw new InternalError("Invalid note");
        }
        switch (alteration)
        {
        case -2: ret += "bb"; break;
        case -1: ret += "b";  break;
        case 1 : ret += "#";  break;
        case 2 : ret += "x";  break;
        }
        return ret + (includeOct ? octave : "");
    }

    @Override public int hashCode()
    {
        return Integer.hashCode(getCode());
    }

    @Override public int compareTo(Note o)
    {
        if (o == null) throw new NullPointerException("Null Note to compare");
        if (getCode() < o.getCode()) return -1;
        if (getCode() > o.getCode()) return 1;
        return 0;
    }
    private int compareTo(int noteCode, int octave, int alteration)
    {
        if (getCode() < getCode(noteCode, octave, alteration)) return -1;
        if (getCode() > getCode(noteCode, octave, alteration)) return 1;
        return 0;
    }
}
