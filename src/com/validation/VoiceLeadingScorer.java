package com.validation;

import com.base.Chord;
import com.base.Note;
import com.base.NoteCluster;

import java.util.ArrayList;

/**
 * Created by NyLP on 6/20/17.
 */

public class VoiceLeadingScorer
{
    public static int score(NoteCluster nc1, NoteCluster nc2)
    {
        ArrayList<Note> nc1n = nc1.getNotes();
        ArrayList<Note> nc2n = nc2.getNotes();
        int accum = 0;
        for (int i = 0; i < nc1n.size(); i++)
            accum += 50 * Math.abs(nc1n.get(i).dist(nc2n.get(i)));
        return accum;
    }
}
