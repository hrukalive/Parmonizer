package com.base;

import com.common.Interval;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by NyLP on 6/12/17.
 */

public class Note implements Comparable<Note>
{
    private int noteCode;
    private int octave;
    private int alteration;
    private ArrayList<Note> tendency;

    private Note(Note note)
    {
        this.noteCode = note.noteCode;
        this.octave = note.octave;
        this.alteration = note.alteration;
        this.tendency = note.tendency;
    }
    private Note(int noteCode, int octave, int alteration)
    {
        this.noteCode = noteCode;
        this.octave = octave;
        this.alteration = alteration;
        this.tendency = new ArrayList<>();
    }

    public void addTendency(Note note) { tendency.add(note); }
    public ArrayList<Note> getTendencies() { return tendency; }

    public static Note n(String noteName)
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
    public int distNoAlt(Note note)
    {
        Note temp = Note.n(note);
        temp.alteration = 0;
        return dist(temp);
    }
    public Interval interval(Note note)
    {
        if (compareTo(note) > 0)
            return note.interval(this).invert();
        Note cthis = new Note(this);
        Note cthat = new Note(note);
        cthis.octave = 0;
        while (cthis.dist(cthat) > 12)
            cthat.octave--;
        int diff = cthat.noteCode - cthis.noteCode;
        return new Interval((diff <= 0 ? (diff + (cthat.octave - cthis.octave) * 7) : diff) + 1, cthis.dist(cthat));
    }

    public static Note n(Note note)
    {
        return new Note(note);
    }

    public Note intervalAbove(Interval intv)
    {
        Note temp = new Note(this);
        temp.noteCode += intv.degree();
        temp.octave += temp.noteCode / 7;
        temp.noteCode %= 7;
        temp.alteration = intv.semitones() - this.distNoAlt(temp);
        return temp;
    }
    public Note intervalBelow(Interval intv)
    {
        Note temp = intervalAbove(intv.invert());
        temp.octave--;
        return temp;
    }

    public ArrayList<Note> allInRange(Note lo, Note hi)
    {
        ArrayList<Note> ret = new ArrayList<>();
        Note temp = new Note(this);
        while (temp.compareTo(lo) < 0) temp.octave++;
        while (temp.compareTo(hi) < 0)
        {
            ret.add(new Note(temp));
            temp.octave++;
        }
        return ret;
    }

    public int getCode()
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
    public boolean isDiminished(Note note)
    {
        if (compareTo(note) > 0)
            return note.isAugmented(this);
        return interval(note).isDiminished();
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
        return Integer.hashCode(noteCode);
    }

    @Override public int compareTo(Note o)
    {
        if (o == null) throw new NullPointerException("Null Note to compare");
        if (getCode() < o.getCode()) return -1;
        if (getCode() > o.getCode()) return 1;
        return 0;
    }
}
