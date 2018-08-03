package com.base.scale;

import com.base.interval.Interval;
import com.base.Note;
import com.base.chord.Chord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Concept of a mode of a scale.
 * There exists two directions for a mode. And negative version is possible.
 * 
 * Created by NyLP on 7/15/17.
 */

public class Mode
{
    private final int index;
    private final Interval negativeGenInterval;
    private final List<Interval> intervalSteps;
    private final boolean allowRespell;
    private final List<Note> tones = new ArrayList<>();
    private final List<Note> negativeTones = new ArrayList<>();

    public Mode(int index, List<Interval> intervalSteps, boolean allowRespell, Interval negativeGenInterval)
    {
        this.index = index;
        this.intervalSteps = intervalSteps;
        this.negativeGenInterval = negativeGenInterval;
        this.allowRespell = allowRespell;
    }

    public void build(Note tonic) {
        Note tempnote = Note.parse(tonic);
        if (allowRespell && tempnote.hasBetterSpell())
            tempnote = tempnote.respell();
        tones.add(tempnote);
        for (int i = 0; i < intervalSteps.size() - 1; i++)
        {
            tempnote = tempnote.interval(intervalSteps.get((i + index) % intervalSteps.size()));
            if (allowRespell && tempnote.hasBetterSpell())
                tempnote = tempnote.respell();
            tones.add(tempnote);
        }

        tempnote = tonic.interval(negativeGenInterval);
        if (allowRespell && tempnote.hasBetterSpell())
            tempnote = tempnote.respell();
        negativeTones.add(tempnote);
        for (int i = 0; i < intervalSteps.size() - 1; i++)
        {
            tempnote = tempnote.interval(intervalSteps.get((i + index) % intervalSteps.size()).reverse());
            if (allowRespell && tempnote.hasBetterSpell())
                tempnote = tempnote.respell();
            negativeTones.add(tempnote);
        }
    }

    public Note getNote(int index) {
        Note ret = Note.parse(tones.get(index % tones.size()));
        ret.octave(Math.floorDiv(index, tones.size()));
        return ret;
    }

    public Note getNegativeNote(int index) {
        Note ret = Note.parse(negativeTones.get(index % negativeTones.size()));
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

    public boolean containChord(Chord chord) {
        return false;
    }

    @Override
    public String toString() {
        return "Scale: " + tones.stream().map(n -> n._toString(false)).collect(Collectors.joining(" ")) + "\n" +
                "Negative: " +  negativeTones.stream().map(n -> n._toString(false)).collect(Collectors.joining(" "));
    }
}
