package com.base;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Implement the concept of scale. (Need rethinking because of 
 * the altered note in some chords, differentiating upward and 
 * downward noteSet, generator difference in negative harmony).
 *
 * Created by NyLP on 7/4/17.
 */

public class Scale
{
    private final ArrayList<Interval> intervalSteps;
    private final Note generator;
    private final HashMap<String, Mode> modes = new HashMap<>();
    
    // intervalSteps requires the last interval that leads back to generator
    public Scale(Note generator, ArrayList<Interval> intervalSteps)
    {
        this.generator = generator;
        this.intervalSteps = intervalSteps;
        build();
    }
    
    protected void build()
    {
//        NoteStruct temproot = generator;
//        for (int i = 0; i < intervalSteps.size(); i++)
//        {
//            ArrayList<NoteStruct> temptones = new ArrayList<>();
//            temptones.add(NoteStruct.build(temproot));
//            NoteStruct tempnote = temproot;
//            for (int j = 0; j < intervalSteps.size() - 1; j++)
//            {
//                tempnote = tempnote.intervalAbove(intervalSteps.get((i + j) % intervalSteps.size()));
//                temptones.add(tempnote);
//            }
//            modes.put(i + "", new Mode(temptones));
//        }
    }
    
    public void putMode(String name, Mode mode) { modes.put(name, mode); }

    public Mode getMode(String name)
    {
        return modes.get(name);
    }
}
