package com.base;

import com.base.realization.ChordVoicing;
import com.base.realization.ChordVoicing.NoteCluster;
import com.base.realization.VoiceLeading;
import com.validation.VoiceLeadingScorer;
import com.validation.VoiceLeadingValidator;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Collection of chords so that under certain restraints, progression
 * can be realized.
 * 
 * Created by NyLP on 6/20/17.
 */

public class Progression
{
    private ArrayList<ChordVoicing> progression = new ArrayList<>();
    private ArrayList<VoiceLeading> collection = new ArrayList<>();

    public void addHarmony(ChordVoicing chord)
    { 
        progression.add(new ChordVoicing(chord));
    }

    public void yield()
    {
        collection.clear();
        yieldHelper(0, null);
        Collections.sort(collection);
    }
    private void yieldHelper(int num, VoiceLeading vl)
    {
        if (num == progression.size())
        {
            collection.add(vl);
        }
        else if (num < progression.size())
        {
            ChordVoicing harmony = progression.get(num);
            harmony.yield();
            ArrayList<NoteCluster> crs = harmony.getRealizations();
            for (int i = 0; i < crs.size(); i++)
            {
                NoteCluster cr = crs.get(i);

                if (num == 0)
                {
                    VoiceLeading tempvl = new VoiceLeading();
                    tempvl.addChord(cr);
                    tempvl.setLoss(tempvl.getLoss() + cr.getLoss());
                    yieldHelper(num + 1, tempvl);
                }
                else
                {
                    VoiceLeadingValidator.VoiceLeadingValidationResult result = VoiceLeadingValidator.validate(vl.lastChord(), cr);
                    if (result.equals(VoiceLeadingValidator.VoiceLeadingValidationResult.SUCCESS))
                    {
                        VoiceLeading vln = vl.addChordNew(cr);
                        vln.setLoss(vl.getLoss() + VoiceLeadingScorer.score(vl.lastChord(), cr) + cr.getLoss());
                        yieldHelper(num + 1, vln);
                    }
//                    else
//                    {
//                        System.err.println(result.name());
//                    }
                }
            }
        }
    }

    public ArrayList<VoiceLeading> getPieces()
    {
        return collection;
    }
}
