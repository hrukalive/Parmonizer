package com.validation;

import com.base.Chord;
import com.base.Note;
import com.base.Interval;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Scorer to evaluate the chord to chord transition.
 * 
 * Created by NyLP on 6/20/17.
 */

public class VoiceLeadingScorer
{
    public static int score(Chord.NoteCluster nc1, Chord.NoteCluster nc2)
    {
        ArrayList<Note> nc1n = nc1.getNotes();
        ArrayList<Note> nc2n = nc2.getNotes();
        int accum = 0;
        
        accum += 1000 * Math.abs(nc1n.get(0).dist(nc2n.get(0)));
        for (int i = 1; i < nc1n.size(); i++)
            accum += 50 * Math.abs(nc1n.get(i).dist(nc2n.get(i)));

        for (int i = 0; i < nc1n.size() - 1; i++)
        {
            for (int j = i + 1; j < nc1n.size(); j++)
            {
                if (nc1n.get(i).compareTo(nc2n.get(i)) < 0 && nc1n.get(j).compareTo(nc2n.get(j)) > 0 && 
                        Math.abs(nc1n.get(j).dist(nc2n.get(j))) > Interval.parse("M2").semitones())
                {
                    if (nc2n.get(i).interval(nc2n.get(j)).equals(Interval.parse("P1")))
                        accum += 2000;
                    if (nc2n.get(i).interval(nc2n.get(j)).equals(Interval.parse("P4")))
                        accum += 2000;
                    if (nc2n.get(i).interval(nc2n.get(j)).equals(Interval.parse("P5")))
                        accum += 2000;
                    if (nc2n.get(i).interval(nc2n.get(j)).equals(Interval.parse("P8")))
                        accum += 2000;
                }
            }
            if (!nc1n.get(i).getAltTendency().isEmpty() && nc1n.get(i).getAltTendency().indexOf(nc2n.get(i)) != -1)
                accum += 50000;
            HashMap<Note, Integer> bonusMap = nc1n.get(i).getBonus();
            if (bonusMap.containsKey(nc2n.get(i)))
                accum -= bonusMap.get(nc2n.get(i));
        }
        
        return accum;
    }
}
