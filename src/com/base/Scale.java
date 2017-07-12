package com.base;

import com.chord.ChordBuilder;
import com.common.Interval;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Implement the concept of scale. (Need rethinking because of 
 * the altered note in some chords, differentiating upward and 
 * downward noteSet, generator difference in negative harmony).
 *
 * Created by NyLP on 7/4/17.
 */

interface IMode
{
    ChordBuilder getTriad(int num, int inv, int voice);
    ChordBuilder getSeventh(int num, int inv, int voice);
    void setName(String name);
}

public class Scale
{
    public class Mode implements IMode
    {
        protected String name = "";
        protected ArrayList<Note> scale_tones;
        protected ArrayList<Chord.Builder> triads = new ArrayList<>();
        protected ArrayList<Chord.Builder> sevenths = new ArrayList<>();

        public Mode(ArrayList<Note> scale_tones)
        {
            this.scale_tones = new ArrayList<>(scale_tones);
            buildChords();
        }
        
        public Mode(Mode mode)
        {
            this.name = mode.name;
            mode.scale_tones.forEach(note -> this.scale_tones.add(Note.build(note)));
            mode.triads.forEach(triad -> this.triads.add(new Chord.Builder(triad)));
            mode.sevenths.forEach(seventh -> this.sevenths.add(new Chord.Builder(seventh)));
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

        public ArrayList<Note> getScaletones()
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

        public ChordBuilder getTriad(int num) { return getTriad(num, 0, 4); }

        public ChordBuilder getTriad(int num, int inv)
        {
            return getTriad(num, inv,4);
        }

        public ChordBuilder getTriad(int num, int inv, int voice)
        {
            if (num < 1 || num > triads.size())
                throw new IllegalArgumentException("Required chord does not exist.");
            return new ChordBuilder(3, voice).chord(triads.get(num - 1));
        }

        public ChordBuilder getSeventh(int num) { return getSeventh(num, 0, 4); }
        
        public ChordBuilder getSeventh(int num, int inv) { return getSeventh(num, inv, 4); }

        public ChordBuilder getSeventh(int num, int inv, int voice)
        {
            if (num < 1 || num > sevenths.size())
                throw new IllegalArgumentException("Required chord does not exist.");
            return new ChordBuilder(4, voice).chord(sevenths.get(num - 1));
        }
    }
    protected final Interval[] intervalSteps;
    protected final Note root;
    protected final HashMap<String, IMode> modes = new HashMap<>();
    
    // intervalSteps requires the last interval that leads back to root
    public Scale(Note root, Interval[] intervalSteps)
    {
        this.root = root;
        this.intervalSteps = intervalSteps;
        build();
    }
    
    protected void build()
    {
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
            modes.put(i + "", new Mode(temptones));
        }
    }

    public IMode getMode(String id)
    {
        return modes.get(id);
    }
}
