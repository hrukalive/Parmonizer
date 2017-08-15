package com.base;

import java.util.ArrayList;

/**
 * Concept of a mode of a scale.
 * There exists two directions for a mode. And negative version is possible.
 * 
 * Created by NyLP on 7/15/17.
 */

public class Mode
{
    private String name = "";
    private Note generator;
    private Interval tonic_distance;
    private Interval.Dir dir = Interval.Dir.Above;
    private ArrayList<Note> scale_tones = new ArrayList<>();

    public Mode(String name, Note generator, Interval tonic_distance, ArrayList<Interval> intervalSteps, ArrayList<Interval> alterations)
    {
        this.name = name;
        this.generator = generator;
        this.tonic_distance = tonic_distance;
        this.dir = intervalSteps.get(0).dir();
        if (alterations != null && intervalSteps.size() != alterations.size())
            throw new IllegalArgumentException("Mode creation failed due to illegal parameters.");
        
        
    }

    public Mode(Mode mode)
    {
        this.name = mode.name;
        mode.scale_tones.forEach(note -> this.scale_tones.add(Note.build(note)));
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public Interval.Dir getDir()
    {
        return dir;
    }

    public Note getScaleTone(int degree)
    {
        return scale_tones.get((degree - 1) % scale_tones.size());
    }
}
