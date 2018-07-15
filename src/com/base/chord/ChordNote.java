package com.base.chord;

import com.base.Interval;
import com.base.Note;
import com.base.Tuple;

import java.util.ArrayList;

public class ChordNote
{
    private final Note note;
    private final Tuple<Boolean, Integer> repeatConfig;
    private final Tuple<Boolean, Integer> omitConfig;
    private final ArrayList<Interval> prepareList = new ArrayList<>();
    private final ArrayList<Interval> tendencyList = new ArrayList<>();
    private final ArrayList<Interval> altTendencyList = new ArrayList<>();
    private final ArrayList<Tuple<Interval, Integer>> bonusList = new ArrayList<>();

    public ChordNote(Note note)
    {
        this.note = note;
        this.repeatConfig = new Tuple<>(false, 0);
        this.omitConfig = new Tuple<>(false, 0);
    }

    public ChordNote(Note note, Tuple<Boolean, Integer> repeatConfig, Tuple<Boolean, Integer> omitConfig)
    {
        this.note = note;
        this.repeatConfig = repeatConfig;
        this.omitConfig = omitConfig;
    }

    public ChordNote(ChordNote config)
    {
        this.note = config.note;
        this.repeatConfig = config.repeatConfig;
        this.omitConfig = config.omitConfig;
        this.prepareList.addAll(config.prepareList);
        this.tendencyList.addAll(config.tendencyList);
        this.altTendencyList.addAll(config.altTendencyList);
        this.bonusList.addAll(config.bonusList);
    }

    public Note getNote()
    {
        return note;
    }

    public Tuple<Boolean, Integer> getRepeatConfig()
    {
        return repeatConfig;
    }

    public Tuple<Boolean, Integer> getOmitConfig()
    {
        return omitConfig;
    }

    public ArrayList<Interval> getPrepareList()
    {
        return prepareList;
    }

    public ArrayList<Interval> getTendencyList()
    {
        return tendencyList;
    }

    public ArrayList<Interval> getAltTendencyList()
    {
        return altTendencyList;
    }

    public ArrayList<Tuple<Interval, Integer>> getBonusList()
    {
        return bonusList;
    }
}
