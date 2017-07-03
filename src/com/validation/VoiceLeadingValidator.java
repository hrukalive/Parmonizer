package com.validation;

import com.base.Note;
import com.base.NoteCluster;
import com.common.Interval;

import java.util.ArrayList;

/**
 * Validator that implements the rules for voice-leading.
 * 
 * Created by NyLP on 6/20/17.
 */

public class VoiceLeadingValidator
{
    public static boolean validate(NoteCluster nc1, NoteCluster nc2)
    {
        ArrayList<Note> nc1n = nc1.getNotes();
        ArrayList<Note> nc2n = nc2.getNotes();

        for (int i = 0; i < nc1n.size(); i++)
        {
            // No Voice-overlap
            if (i > 0 && i < nc1n.size() - 1 && !(nc2n.get(i).compareTo(nc1n.get(i - 1)) > 0 && nc2n.get(i).compareTo(nc1n.get(i + 1)) < 0))
                return false;
            else if (i == 0 && !(nc2n.get(i).compareTo(nc1n.get(i + 1)) < 0))
                return false;
            else if (i == nc1n.size() - 1 && !(nc2n.get(i).compareTo(nc1n.get(i - 1)) > 0))
                return false;

            // No leap for larger than P5
            if (Math.abs(nc1n.get(i).dist(nc2n.get(i))) > Interval.P5.semitones())
                return false;

            // No Aug or Dim interval
            if (nc1n.get(i).isAugmented(nc2n.get(i)) || nc1n.get(i).isDiminished(nc2n.get(i)))
                return false;
            if (nc2n.get(i).isAugmented(nc1n.get(i)) || nc2n.get(i).isDiminished(nc1n.get(i)))
                return false;

            // Tendency tone must be resolved
            if (nc1n.get(i).getTendencies().size() > 0 && nc1n.get(i).getTendencies().indexOf(nc2n.get(i)) == -1 &&
                    nc1n.get(i).getAltTendency().size() > 0 && nc1n.get(i).getAltTendency().indexOf(nc2n.get(i)) == -1)
                return false;
        }

        // No parallel perfect intervals
        for (int i = 0; i < nc1n.size() - 1; i++)
        {
            for (int j = i + 1; j < nc1n.size(); j++)
            {
                if (!nc1n.get(i).equals(nc2n.get(i)) && nc1n.get(i).interval(nc1n.get(j)).equals(Interval.P1) && nc2n.get(i).interval(nc2n.get(j)).equals(Interval.P1))
                    return false;
                if (!nc1n.get(i).equals(nc2n.get(i)) && nc1n.get(i).interval(nc1n.get(j)).equals(Interval.P4) && nc2n.get(i).interval(nc2n.get(j)).equals(Interval.P4))
                    return false;
                if (!nc1n.get(i).equals(nc2n.get(i)) && nc1n.get(i).interval(nc1n.get(j)).equals(Interval.P5) && nc2n.get(i).interval(nc2n.get(j)).equals(Interval.P5))
                    return false;
                if (!nc1n.get(i).equals(nc2n.get(i)) && nc1n.get(i).interval(nc1n.get(j)).equals(Interval.P8) && nc2n.get(i).interval(nc2n.get(j)).equals(Interval.P8))
                    return false;
            }
        }

        // No contrary perfect intervals between outer voices
        if (nc1n.get(0).compareTo(nc2n.get(0)) < 0 && nc1n.get(nc1n.size() - 1).compareTo(nc2n.get(nc1n.size() - 1)) > 0 && 
                Math.abs(nc1n.get(nc1n.size() - 1).dist(nc2n.get(nc1n.size() - 1))) > Interval.M2.semitones())
        {
            if (nc2n.get(0).interval(nc2n.get(nc1n.size() - 1)).equals(Interval.P1))
                return false;
            if (nc2n.get(0).interval(nc2n.get(nc1n.size() - 1)).equals(Interval.P4))
                return false;
            if (nc2n.get(0).interval(nc2n.get(nc1n.size() - 1)).equals(Interval.P5))
                return false;
            if (nc2n.get(0).interval(nc2n.get(nc1n.size() - 1)).equals(Interval.P8))
                return false;
        }
        return true;
    }
}
