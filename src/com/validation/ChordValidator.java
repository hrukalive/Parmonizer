package com.validation;

import com.base.Chord;
import com.base.Note;
import com.base.NoteCluster;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Validator that implements the rules when voicing a chord.
 * 
 * Created by NyLP on 6/16/17.
 */
public final class ChordValidator
{
    private final boolean[] repeatability;
    private final boolean[] omitability;
    
    public ChordValidator(boolean[] repeatability, boolean[] omitability)
    {
        this.repeatability = repeatability;
        this.omitability = omitability;
    }
    
    public boolean withinOctave(ArrayList<Note> chordNotes)
    {
        for (int i = 1; i < chordNotes.size() - 1; i++)
        {
            if (chordNotes.get(i + 1).getCode() - chordNotes.get(i).getCode() > 12)
                return false;
        }
        return true;
    }

    public boolean validate(Chord.ChordRealization chordRealization, Chord parent)
    {
        ArrayList<Note> chordNotes = chordRealization.getNotes();
        if (!withinOctave(chordNotes))
            return false;

        for (int i = 0; i < omitability.length; i++)
        {
            if (!omitability[i])
            {
                boolean existFlag = false;
                Note notOmitNote = (Note)parent.getNoteSet().get(i);
                for (Note note : chordNotes)
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

        int[] counter = new int[chordNotes.size()];
        ArrayList<Note> chord = parent.getNoteSet();
        for (Note note : chordNotes)
        {
            for (int i = 0; i < chord.size(); i++)
            {
                if (note.isEnharmonicNoClass(chord.get(i)))
                {
                    counter[i]++;
                    break;
                }
            }
        }
        for (int i = 0; i < counter.length; i++)
            if (counter[i] > 1 && !repeatability[i])
                return false;

        return true;
    }
}
