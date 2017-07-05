package com.base;

import com.common.Interval;

import java.util.ArrayList;

/**
 * Implement the concept of scale.
 * 
 * Created by NyLP on 7/4/17.
 */

public final class Scale
{
    public class Mode
    {
        private String name = "";
        private ArrayList<Note> scale_tones;
        private ArrayList<Chord.Builder> triads = new ArrayList<>();
        private ArrayList<Chord.Builder> sevenths = new ArrayList<>();
        public Mode(ArrayList<Note> scale_tones)
        {
            this.scale_tones = new ArrayList<>(scale_tones);
            buildChords();
        }
        private void buildChords()
        {
            ArrayList<Note> scale_tones_2oct = new ArrayList<>(scale_tones);
            scale_tones.forEach(elem -> scale_tones_2oct.add(elem.intervalAbove(Interval.P8)));
            for (int i = 0; i < scale_tones.size(); i++)
            {
                Interval[] t_intvs = new Interval[2];
                Interval[] s_intvs = new Interval[3];
                t_intvs[0] = s_intvs[0] = scale_tones_2oct.get(i).interval(scale_tones_2oct.get(i + 2));
                t_intvs[1] = s_intvs[1] = scale_tones_2oct.get(i).interval(scale_tones_2oct.get(i + 4));
                s_intvs[2] = scale_tones_2oct.get(i).interval(scale_tones_2oct.get(i + 6));
                triads.add(new Chord.Builder(scale_tones.get(i), t_intvs));
                sevenths.add(new Chord.Builder(scale_tones.get(i), s_intvs));
            }
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public String getName()
        {
            return name;
        }

        public ArrayList<Note> getScale_tones()
        {
            return scale_tones;
        }

        public ArrayList<Chord.Builder> getTriads()
        {
            return triads;
        }

        public ArrayList<Chord.Builder> getSevenths()
        {
            return sevenths;
        }
    }
    
    private final Interval[] intervalSteps;
    private final Note root;
    private final ArrayList<Mode> modes = new ArrayList<>();
    
    // intervalSteps requires the last interval that leads back to root
    public Scale(Note root, Interval[] intervalSteps)
    {
        this.root = root;
        this.intervalSteps = intervalSteps;
        
        Note temproot = root;
        for (int i = 0; i < intervalSteps.length; i++)
        {
            ArrayList<Note> temptones = new ArrayList<>();
            temptones.add(Note.build(temproot));
            Note tempnote = temproot;
            for (int j = 0; j < intervalSteps.length - 1; j++)
            {
                tempnote = tempnote.intervalAbove(intervalSteps[(i + j) % intervalSteps.length]);
                temptones.add(tempnote);
            }
            modes.add(new Mode(temptones));
        }
    }
}
