package com.validation;

import com.base.Chord;
import com.base.Note;

import java.util.ArrayList;

/**
 * Validator that implements the rules when voicing a chord.
 * 
 * Created by NyLP on 6/16/17.
 */
public final class ChordValidator
{
    private final ArrayList<Boolean> repeatability;
    private final ArrayList<Boolean> omissibility;
    
    public ChordValidator(ArrayList<Boolean> repeatability, ArrayList<Boolean> omissibility)
    {
        this.repeatability = repeatability;
        this.omissibility = omissibility;
    }

    public boolean validate(ArrayList<Note> chordRealization, Chord parent)
    {
        for (int i = 1; i < chordRealization.size() - 1; i++)
        {
            if (chordRealization.get(i + 1).getCode() - chordRealization.get(i).getCode() > 12)
                return false;
        }

        ArrayList<Note> chordTones = parent.getNoteSet();
        for (int i = 0; i < omissibility.size(); i++)
        {
            if (!omissibility.get(i))
            {
                boolean existFlag = false;
                Note notOmitNote = chordTones.get(i);
                for (Note note : chordRealization)
                {
                    if (note.isEnharmonicNoClass(notOmitNote))
                    {
                        existFlag = true;
                        break;
                    }
                }
                if (!existFlag)
                    return false;
            }
        }

        int[] counter = new int[chordTones.size()];
        for (Note note : chordRealization)
        {
            for (int i = 0; i < chordTones.size(); i++)
            {
                if (note.isEnharmonicNoClass(chordTones.get(i)))
                {
                    counter[i]++;
                    break;
                }
            }
        }
        for (int i = 0; i < chordTones.size(); i++)
            if (counter[i] > 1 && !repeatability.get(i))
                return false;

        return true;
    }
}
