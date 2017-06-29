package com.validation;

import com.base.Chord;
import com.base.Note;
import com.base.NoteCluster;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by NyLP on 6/16/17.
 */

public class ChordScorer
{
    private final int[] repeatPenalty;
    private final int[] omitPenalty;
    private final int[] unisonPenalty;

    public ChordScorer(int[] repeatPenalty, int[] omitPenalty, int[] unisonPenalty)
    {
        this.repeatPenalty = repeatPenalty;
        this.omitPenalty = omitPenalty;
        this.unisonPenalty = unisonPenalty;
    }

    private int score(ArrayList<Note> chordNotes, Chord parent, int[] repeatPenalty, int[] omitPenalty, int[] unisonPenalty)
    {
        int loss = 0;

        ArrayList<Note> chord = parent.getNoteSet();
        int[] counter = new int[chord.size()];

        for (int i = 0; i < chord.size(); i++)
        {
            boolean existFlag = false;
            Note notOmitNote = chord.get(i);
            for (Note note : chordNotes)
            {
                if (note.isEnharmonicNoClass(notOmitNote))
                {
                    existFlag = true;
                    break;
                }
            }
            if (!existFlag)
                loss += omitPenalty[i];
        }

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
            loss += counter[i] * repeatPenalty[i];

        for (int i = 0; i < chordNotes.size() - 1; i++)
        {
            if (chordNotes.get(i + 1).getCode() == chordNotes.get(i).getCode())
                loss += unisonPenalty[i];
            if (chordNotes.get(i).dist(chordNotes.get(i + 1)) < 3)
                loss += i * 100;
        }

        return loss;
    }

    public void score(Chord chord)
    {
        Iterator it = chord.getRealizations().iterator();
        while (it.hasNext())
        {
            Chord.ChordRealization realization = (Chord.ChordRealization)(it.next());
            realization.setLoss(score(realization.getNotes(), chord, repeatPenalty, omitPenalty, unisonPenalty));
        }
    }
}
