package com.base;

import com.base.interval.Interval;
import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implemented the concept of a note.
 * Captured the difference of enharmonic notes. Providing functions
 * to transform and compare notes.
 * <p>
 * Created by NyLP on 6/12/17.
 */

public final class Note implements Comparable<Note> {
    private final static Map<Triple<Integer, Integer, Integer>, Note> note_code_map = new HashMap<>();
    private final static Map<String, Note> note_name_map = new HashMap<>();

    public static Note parse(String noteName) {
        int code = -1;
        int alt = 0;
        Matcher note_matcher = Pattern.compile("[A-G](bb|b|#|x)?").matcher(noteName);
        Matcher oct_matcher = Pattern.compile("-?[0-9]+").matcher(noteName);
        if (!note_matcher.find())
            throw new IllegalArgumentException("Unable to build on the note name");
        String name = noteName.substring(note_matcher.start(), note_matcher.end());
        int oct = oct_matcher.find() ? Integer.parseInt(noteName.substring(oct_matcher.start(), oct_matcher.end())) : 0;

        if (note_name_map.containsKey(noteName))
            return note_name_map.get(noteName);
        else if (!oct_matcher.matches() && note_name_map.containsKey(noteName + oct))
            return note_name_map.get(noteName + oct);

        switch (name.substring(0, 1)) {
            case "C":
                code = 0;
                break;
            case "D":
                code = 1;
                break;
            case "E":
                code = 2;
                break;
            case "F":
                code = 3;
                break;
            case "G":
                code = 4;
                break;
            case "A":
                code = 5;
                break;
            case "B":
                code = 6;
                break;
        }
        if (name.substring(1).startsWith("b")) {
            alt = -name.substring(1).length();
        } else if (name.substring(1).startsWith("#")) {
            alt = name.substring(1).length();
        } else if (name.substring(1).equals("x")) {
            alt = 2;
        } else {
            alt = 0;
        }

        Note tmp = new Note(code, oct, alt);
        if (!note_code_map.containsKey(new Triple<>(code, oct, alt))) {
            note_code_map.put(new Triple<>(code, oct, alt), tmp);
            note_name_map.put(tmp.toString(), tmp);
        } else {
            throw new InternalException("State of maps is invalid.");
        }
        return tmp;
    }

    public static Note parse(Note note) {
        return parse(note.getNoteCode(), note.getAlteration(), note.getOctave());
    }

    public static Note parse(int code, int alteration, int octave) {
        if (!note_code_map.containsKey(new Triple<>(code, octave, alteration))) {
            Note tmp = new Note(code, octave, alteration);
            note_code_map.put(new Triple<>(code, octave, alteration), tmp);
            note_name_map.put(tmp.toString(), tmp);
            return tmp;
        } else {
            return note_code_map.get(new Triple<>(code, octave, alteration));
        }
    }

    protected final int _noteCode;
    protected final int _octave;
    protected final int _alteration;

    private Note(int noteCode, int octave, int alteration) {
        if (noteCode < 0 || noteCode > 6)
            throw new IllegalArgumentException("Specified parameters is wrong.");
        this._noteCode = noteCode;
        this._octave = octave;
        this._alteration = alteration;
    }

    public int getNoteCode() {
        return _noteCode;
    }

    public int getOctave() {
        return _octave;
    }

    public int getAlteration() {
        return _alteration;
    }

    public int dist(Note note) {
        return note.getCode() - this.getCode();
    }

    public Note interval(Interval intv) {
        if (intv.dir().equals(Interval.Dir.Above))
            return _intervalAbove(intv);
        else
            return _intervalBelow(intv);
    }

    public int getCode() {
        return _getCode(_noteCode, _octave, _alteration);
    }

    public boolean isEnharmonic(Note o) {
        return o != null && this.getCode() == o.getCode();
    }

    public boolean isEnharmonicNoClass(Note o) {
        return o != null && (this.getCode() % 12) == (o.getCode() % 12);
    }

    public boolean isAugmented(Note note) {
        if (compareTo(note) > 0)
            return note.isAugmented(this);
        return Interval.parse(this, note).isAugmented();
    }

    public boolean isMajor(Note note) {
        if (compareTo(note) > 0)
            return note.isMajor(this);
        return Interval.parse(this, note).isMajor();
    }

    public boolean isDiminished(Note note) {
        if (compareTo(note) > 0)
            return note.isDiminished(this);
        return Interval.parse(this, note).isDiminished();
    }

    public boolean isMinor(Note note) {
        if (compareTo(note) > 0)
            return note.isMinor(this);
        return Interval.parse(this, note).isMinor();
    }

    public Note octaveUp() {
        return octave(1);
    }

    public Note octaveDown() {
        return octave(-1);
    }

    public Note octave(int delta) {
        return parse(_noteCode, _alteration, _octave + delta);
    }

    private Note _intervalAbove(Interval intv) {
        int temp_noteCode = _noteCode + (intv.degree() - 1);
        int temp_octave = _octave + temp_noteCode / 7;
        temp_noteCode %= 7;
        int temp_alteration = intv.semitones() - (_getCode(temp_noteCode, temp_octave, 0) - this.getCode());
        return parse(temp_noteCode, temp_alteration, temp_octave);
    }

    public boolean hasBetterSpell() {
        return Math.abs(_alteration) >= 2 ||
                ((_noteCode == 2 || _noteCode == 6) && _alteration > 0) ||
                ((_noteCode == 3 || _noteCode == 0) && _alteration < 0);
    }
    
    public Note respell() {
        int tmp_noteCode = _noteCode;
        int tmp_octave = _octave;
        int tmp_alteration = _alteration;
        do {
            if ((tmp_noteCode == 2 || tmp_noteCode == 6) && tmp_alteration > 0) {
                tmp_noteCode++;
                tmp_octave += tmp_noteCode / 7;
                tmp_noteCode %= 7;
                tmp_alteration = tmp_alteration - 1;
            } else if ((tmp_noteCode == 3 || tmp_noteCode == 0) && tmp_alteration < 0) {
                tmp_noteCode--;
                tmp_octave += Math.floorDiv(tmp_noteCode, 7);
                tmp_noteCode = (tmp_noteCode + 7) % 7;
                tmp_alteration = tmp_alteration + 1;
            } else if (tmp_alteration > 0) {
                tmp_noteCode++;
                tmp_alteration -= 2;
            } else if (tmp_alteration < 0) {
                tmp_noteCode--;
                tmp_alteration += 2;
            }
        } while (Math.abs(tmp_alteration) >= 2);

        return parse(tmp_noteCode, tmp_alteration, tmp_octave);
    }

    private Note _intervalBelow(Interval intv) {
        Interval intv_inv = intv.invert();
        int temp_noteCode = _noteCode + (intv_inv.degree() - 1);
        int temp_octave = _octave + temp_noteCode / 7;
        temp_noteCode %= 7;
        int temp_alteration = intv_inv.semitones() - (_getCode(temp_noteCode, temp_octave, 0) - this.getCode());
        return parse(temp_noteCode, temp_alteration, temp_octave - 1);
    }

    public static int dist(int this_noteCode, int this_octave, int this_alteration,
                           int that_noteCode, int that_octave, int that_alteration) {
        return _getCode(that_noteCode, that_octave, that_alteration) - _getCode(this_noteCode, this_octave, this_alteration);
    }

    protected static int _getCode(int noteCode, int octave, int alteration) {
        int ret = 0;
        switch (noteCode) {
            case 0:
                ret = 0;
                break;
            case 1:
                ret = 2;
                break;
            case 2:
                ret = 4;
                break;
            case 3:
                ret = 5;
                break;
            case 4:
                ret = 7;
                break;
            case 5:
                ret = 9;
                break;
            case 6:
                ret = 11;
                break;
        }
        ret += octave * 12 + 12;
        ret += alteration;
        return ret;
    }

    public String _toString(boolean includeOct) {
        String ret = "";
        switch (_noteCode) {
            case 0:
                ret += "C";
                break;
            case 1:
                ret += "D";
                break;
            case 2:
                ret += "E";
                break;
            case 3:
                ret += "F";
                break;
            case 4:
                ret += "G";
                break;
            case 5:
                ret += "A";
                break;
            case 6:
                ret += "B";
                break;
            default:
                throw new InternalError("Invalid note");
        }
        if (_alteration == 2) {
            ret += "x";
        } else if (_alteration < 0) {
            ret += String.join("", Collections.nCopies(-_alteration, "b"));
        } else if (_alteration > 0) {
            ret += String.join("", Collections.nCopies(_alteration, "#"));
        }
        return ret + (includeOct ? _octave : "");
    }

    public boolean equalsNoClass(Note note) {
        return note != null && this._noteCode == note._noteCode && this._alteration == note._alteration;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(getCode());
    }

    @Override
    public String toString() {
        return _toString(true);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Note) &&
                ((Note) obj)._noteCode == this._noteCode &&
                ((Note) obj)._octave == this._octave &&
                ((Note) obj)._alteration == this._alteration;
    }

    @Override
    public int compareTo(Note o) {
        if (o == null) throw new NullPointerException("Null NoteStruct to compare");
        return Integer.compare(getCode(), o.getCode());
    }
}

