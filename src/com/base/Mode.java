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
    private ArrayList<Note> scale_tones = new ArrayList<>();
    private ArrayList<Note> neg_scale_tones = new ArrayList<>();

    public Mode(Note root, ArrayList<Interval> intervalSteps, Interval.Dir dir, boolean hasNeg, Interval negIntv)
    {
//        this.scale_tones = new ArrayList<>();
//
//        scale_tones.add(NoteStruct.build(root));
//        NoteStruct tempnote = root;
//        for (int i = 0; i < intervalSteps.size() - 1; i++)
//        {
//            tempnote = tempnote.intervalAbove(intervalSteps.get(i));
//            scale_tones.add(tempnote);
//        }
//
//        if (hasNeg)
//        {
//            neg_scale_tones = new ArrayList<>();
//            neg_gen = neg_gen.intervalAbove(Interval.P8);
//            neg_scale_tones.add(NoteStruct.build(neg_gen));
//            tempnote = neg_gen;
//            for (int i = 0; i < intervalSteps.size() - 1; i++)
//            {
//                tempnote = tempnote.intervalBelow(intervalSteps.get(i));
//                neg_scale_tones.add(tempnote);
//            }
//        }
//        else
//            neg_scale_tones = null;
    }

    public Mode(Mode mode)
    {
        this.name = mode.name;
        mode.scale_tones.forEach(note -> this.scale_tones.add(Note.build(note)));
        mode.neg_scale_tones.forEach(note -> this.neg_scale_tones.add(Note.build(note)));
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
}
