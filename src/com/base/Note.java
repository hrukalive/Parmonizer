package com.base;

import com.base.interval.Interval;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implemented the concept of a note.
 * Captured the difference of enharmonic notes. Providing functions
 * to transform and compare notes.
 * 
 * Created by NyLP on 6/12/17.
 */

public class Note implements Comparable<Note>
{
    protected int _noteCode;
    protected int _octave;
    protected int _alteration;

    public Note(int noteCode, int octave, int alteration)
    {
        if (noteCode < 0 || noteCode > 6 || alteration < -2 || alteration > 2)
            throw new IllegalArgumentException("Specified parameters is wrong.");
        this._noteCode = noteCode;
        this._octave = octave;
        this._alteration = alteration;
    }

    public Note(Note note)
    {
        this._noteCode = note._noteCode;
        this._octave = note._octave;
        this._alteration = note._alteration;
    }
    
    public static Note parse(String noteName)
    {
        int code = -1;
        int alt = 0;
        Matcher note_matcher = Pattern.compile("[A-G](#|(bb)|x|b)?").matcher(noteName);
        Matcher oct_matcher = Pattern.compile("-?[0-9]+").matcher(noteName);
        if (!note_matcher.find())
            throw new IllegalArgumentException("Unable to build the note name");
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
    
    public Interval interval(Note note)
    {
        if (compareTo(note) > 0)
            return note.interval(this).invert();
        int that_octave = note._octave;
        while (_dist(_noteCode, 0, _alteration, note._noteCode, that_octave, note._alteration) > 12)
            that_octave--;
        int diff = note._noteCode - this._noteCode;
        return Interval.parse((diff <= 0 ? (diff + that_octave * 7) : diff) + 1,
                _dist(_noteCode, 0, _alteration, note._noteCode, that_octave, note._alteration));
    }
    
    public Note interval(Interval intv)
    {
        if (intv.dir().equals(Interval.Dir.Above))
            return _intervalAbove(intv);
        else
            return _intervalBelow(intv);
    }

    public int getCode()
    {
        return _getCode(_noteCode, _octave, _alteration);
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

    public void octaveUp()
    {
        _octave++;
    }

    public void octaveDown()
    {
        _octave--;
    }

    public void octave(int delta)
    {
        _octave += delta;
    }

    public int getOctave() {
        return _octave;
    }

    private Note _intervalAbove(Interval intv)
    {
        int temp_noteCode = _noteCode + (intv.degree() - 1);
        int temp_octave = _octave + temp_noteCode / 7;
        temp_noteCode %= 7;
        int temp_alteration = intv.semitones() - (_getCode(temp_noteCode, temp_octave, 0) - this.getCode());
        return new Note(temp_noteCode, temp_octave, temp_alteration);
    }

    private Note _intervalBelow(Interval intv)
    {
        Interval intv_inv = intv.invert();
        int temp_noteCode = _noteCode + (intv_inv.degree() - 1);
        int temp_octave = _octave + temp_noteCode / 7;
        temp_noteCode %= 7;
        int temp_alteration = intv_inv.semitones() - (_getCode(temp_noteCode, temp_octave, 0) - this.getCode());
        return new Note(temp_noteCode, temp_octave - 1, temp_alteration);
    }
    
    private static int _dist(int this_noteCode, int this_octave, int this_alteration, 
                            int that_noteCode, int that_octave, int that_alteration)
    {
        return _getCode(that_noteCode, that_octave, that_alteration) - _getCode(this_noteCode, this_octave, this_alteration);
    }
    
    protected static int _getCode(int noteCode, int octave, int alteration)
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
    
    public String _toString(boolean includeOct)
    {
        String ret = "";
        switch (_noteCode)
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
        switch (_alteration)
        {
        case -2: ret += "bb"; break;
        case -1: ret += "b";  break;
        case 1 : ret += "#";  break;
        case 2 : ret += "x";  break;
        }
        return ret + (includeOct ? _octave : "");
    }

    @Override public int hashCode()
    {
        return Integer.hashCode(getCode());
    }

    @Override public String toString()
    {
        return _toString(true);
    }

    @Override public boolean equals(Object obj)
    {
        return (obj instanceof Note) &&
                ((Note)obj)._noteCode == this._noteCode &&
                ((Note)obj)._octave == this._octave &&
                ((Note)obj)._alteration == this._alteration;
    }

    @Override public int compareTo(Note o)
    {
        if (o == null) throw new NullPointerException("Null NoteStruct to compare");
        if (getCode() < o.getCode()) return -1;
        if (getCode() > o.getCode()) return 1;
        return 0;
    }
}

