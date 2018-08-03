package com.base.chord;

import com.base.interval.Interval;
import com.base.Note;
import com.base.Tuple;

import java.util.ArrayList;

public class ChordNote implements Comparable<ChordNote> {
    private Note note = null;
    private boolean repeatable = true;
    private int repeatPenalty = 0;
    private boolean omittable = true;
    private int omitPenalty = 0;
    private final ArrayList<Interval> prepareList = new ArrayList<>();
    private final ArrayList<Interval> tendencyList = new ArrayList<>();
    private final ArrayList<Interval> altTendencyList = new ArrayList<>();
    private final ArrayList<Tuple<Interval, Integer>> bonusList = new ArrayList<>();

    public ChordNote(Note note) {
        this.note = note;
    }

    public ChordNote(ChordNote chordNote) {
        this.note = chordNote.note;
        this.repeatable = chordNote.repeatable;
        this.repeatPenalty = chordNote.repeatPenalty;
        this.omittable = chordNote.omittable;
        this.omitPenalty = chordNote.omitPenalty;
        this.prepareList.addAll(chordNote.prepareList);
        this.tendencyList.addAll(chordNote.tendencyList);
        this.altTendencyList.addAll(chordNote.altTendencyList);
        this.bonusList.addAll(chordNote.bonusList);
    }

    public Note getNote() {
        return note;
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

    public boolean equalsNoClass(ChordNote note) { return note != null && this.note.equalsNoClass(note.getNote()); }

    public void octave(int delta) { note = note.octave(delta); }

    public int getOctave() { return note.getOctave(); }

    public ChordNote interval(Interval interval) {
        ChordNote ret = new ChordNote(this);
        ret.note = ret.note.interval(interval);
        return ret;
    }

    @Override
    public int compareTo(ChordNote o) {
        if (o == null) throw new NullPointerException("Null NoteStruct to compare");
        return this.note.compareTo(o.note);
    }
    @Override
    public String toString() {
        return note.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Note)
            return ((Note)obj).equals(this.note);
        else if (obj instanceof ChordNote)
            return ((ChordNote)obj).getNote().equals(this.note);
        return false;
    }
}
