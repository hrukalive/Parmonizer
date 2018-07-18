package com.base.scale;

import com.base.interval.Interval;
import com.base.Note;
import com.base.chord.Chord;

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
    private final Interval negativeGenInterval;
    private final ArrayList<Note> tones = new ArrayList<>();
    private final ArrayList<Note> negativeTones = new ArrayList<>();

    public Mode(Note tonic, int index, ArrayList<Interval> intervalSteps, boolean allowRespell, Interval negativeGenInterval)
    {
        this.tonic = tonic;
        this.generator = tonic.interval(negativeGenInterval);
        this.negativeGenInterval = negativeGenInterval;

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
    }

    public Mode(Mode mode)
    {
        this.tonic = mode.tonic;
        this.generator = mode.generator;
        this.negativeGenInterval = mode.negativeGenInterval;
    }

    public Note getNote(int index) {
        Note ret = new Note(tones.get(index % tones.size()));
        ret.octave(Math.floorDiv(index, tones.size()));
        return ret;
    }

    public Note getNegativeNote(int index) {
        Note ret = new Note(negativeTones.get(index % negativeTones.size()));
        ret.octave(-Math.floorDiv(index, negativeTones.size()));
        return ret;
    }

    public Chord chord(int index, int interval, int stackNum)
    {
        if (index < 0 || index >= tones.size())
            throw new IllegalArgumentException("Chord required not based on scale note.");
        if (interval < 1 || interval > tones.size())
            throw new IllegalArgumentException("Interval must be greater than P1.");
        if (stackNum < 3 || stackNum > tones.size())
            throw new IllegalArgumentException("Cannot stack more than the note collection.");
        int len = tones.size();
        Chord.Builder builder = new Chord.Builder(getNote(index));
        interval--;
        int scaleIndex = index + interval;
        for (int i = 0; i < stackNum - 1; i++, scaleIndex += interval)
            builder.addNote(getNote(scaleIndex));
        return builder.build();
    }

    public Chord negativeChord(int index, int interval, int stackNum)
    {
        if (index < 0 || index >= tones.size())
            throw new IllegalArgumentException("Chord required not based on scale note.");
        if (interval < 1 || interval > tones.size())
            throw new IllegalArgumentException("Interval must be greater than P1.");
        if (stackNum < 3 || stackNum > tones.size())
            throw new IllegalArgumentException("Cannot stack more than the note collection.");
        Chord.Builder builder = new Chord.Builder(getNegativeNote(index));
        interval--;
        int scaleIndex = index + interval;
        for (int i = 0; i < stackNum - 1; i++, scaleIndex += interval)
            builder.addNote(getNegativeNote(scaleIndex));
        builder.setRoot(getNegativeNote(index).interval(negativeGenInterval.reverse()));
        return builder.build();
    }
}
