package com.base.chord;

import com.base.interval.Interval;
import com.base.Note;
import com.base.Tuple;

import java.util.ArrayList;

public class ChordNote extends Note {
    private boolean repeatable = true;
    private int repeatPenalty = 0;
    private boolean omittable = true;
    private int omitPenalty = 0;
    private final ArrayList<Interval> prepareList = new ArrayList<>();
    private final ArrayList<Interval> tendencyList = new ArrayList<>();
    private final ArrayList<Interval> altTendencyList = new ArrayList<>();
    private final ArrayList<Tuple<Interval, Integer>> bonusList = new ArrayList<>();

    public ChordNote(Note note) {
        super(note);
    }

    public ChordNote(ChordNote config) {
        super(config);
        this.prepareList.addAll(config.prepareList);
        this.tendencyList.addAll(config.tendencyList);
        this.altTendencyList.addAll(config.altTendencyList);
        this.bonusList.addAll(config.bonusList);
    }

    public Note getNote() {
        return this;
    }

    public boolean isRepeatable() {
        return repeatable;
    }

    public boolean isOmittable() {
        return omittable;
    }

    public int getRepeatPenalty() {
        return repeatPenalty;
    }

    public int getOmitPenalty() {
        return omitPenalty;
    }

    public void setOmittable(int penalty) {
        this.omittable = true;
        this.omitPenalty = penalty;
    }

    public void setRepeatable(int penalty) {
        this.repeatable = true;
        this.repeatPenalty = penalty;
    }

    public void setNotOmittable() {
        this.omittable = false;
        this.omitPenalty = 0;
    }

    public void setNotRepeatable() {
        this.repeatable = false;
        this.repeatPenalty = 0;
    }

    public void addPreparation(Interval interval) {
        prepareList.add(interval);
    }

    public void addTendency(Interval interval) {
        tendencyList.add(interval);
    }

    public void addAltTendency(Interval interval) {
        altTendencyList.add(interval);
    }

    public void addBonus(Interval interval, int bonus) {
        bonusList.add(new Tuple<>(interval, bonus));
    }

    public ArrayList<Interval> getPrepareList() {
        return prepareList;
    }

    public ArrayList<Interval> getTendencyList() {
        return tendencyList;
    }

    public ArrayList<Interval> getAltTendencyList() {
        return altTendencyList;
    }

    public ArrayList<Tuple<Interval, Integer>> getBonusList() {
        return bonusList;
    }

}
