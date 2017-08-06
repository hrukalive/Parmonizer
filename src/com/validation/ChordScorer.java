package com.validation;

import com.base.Chord;
import com.base.Note;

import java.util.ArrayList;

/**
 * Scorer to evaluate the 'quality' of a chord configuration.
 * 
 * Created by NyLP on 6/16/17.
 */

public final class ChordScorer
{
    private final ArrayList<Integer> repeatPenalty;
    private final ArrayList<Integer> omitPenalty;
    private final ArrayList<Integer> unisonPenalty;

    public ChordScorer(ArrayList<Integer> repeatPenalty, ArrayList<Integer> omitPenalty, ArrayList<Integer> unisonPenalty)
    {
        this.repeatPenalty = repeatPenalty;
        this.omitPenalty = omitPenalty;
        this.unisonPenalty = unisonPenalty;
    }

    public int score(ArrayList<Note> chordRealization, Chord parent)
    {
        int loss = 0;

        ArrayList<Note> chordTones = parent.getNoteSet();
        int[] counter = new int[chordTones.size()];

        for (int i = 0; i < chordTones.size(); i++)
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
                loss += omitPenalty.get(i);
        }

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
        for (int i = 0; i < counter.length; i++)
            loss += counter[i] * repeatPenalty.get(i);

        for (int i = 0; i < chordRealization.size() - 1; i++)
        {
            if (chordRealization.get(i + 1).getCode() == chordRealization.get(i).getCode())
                loss += unisonPenalty.get(i);
            if (chordRealization.get(i).dist(chordRealization.get(i + 1)) < 3)
                loss += i * 100;
        }

        return loss;
    }
}
