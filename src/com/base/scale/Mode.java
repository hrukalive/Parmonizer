package com.base.scale;

import com.base.Interval;
import com.base.Note;

import java.util.ArrayList;

/**
 * Concept of a mode of a scale.
 * There exists two directions for a mode. And negative version is possible.
 * 
 * Created by NyLP on 7/15/17.
 */

public class Mode
{
    private final Note tonic;
    private final Note generator;
    private final ArrayList<Note> tones = new ArrayList<>();
    private final ArrayList<Note> negativeTones = new ArrayList<>();

    public Mode(Note tonic, int index, ArrayList<Interval> intervalSteps, boolean allowRespell, Interval negativeGenInterval)
    {
        this.tonic = tonic;
        this.generator = tonic.interval(negativeGenInterval);

        Note tempnote = tonic;
        tones.add(new Note(tonic));
        for (int i = 0; i < intervalSteps.size() - 1; i++)
        {
            tempnote = tempnote.interval(intervalSteps.get((i + index) % intervalSteps.size()));
            tones.add(tempnote);
        }

        tempnote = generator;
        negativeTones.add(new Note(generator));
        for (int i = 0; i < intervalSteps.size() - 1; i++)
        {
            tempnote = tempnote.interval(intervalSteps.get((i + index) % intervalSteps.size()).reverse());
            negativeTones.add(tempnote);
        }

        System.out.println(tones);
        System.out.println(negativeTones);
    }

    public Mode(Mode mode)
    {
        this.tonic = mode.tonic;
        this.generator = mode.generator;
    }
}
