package com.base.chord;

import com.base.interval.Interval;
import com.base.Note;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Implemented the concept of a chord, with the functionality
 * to generate all possible configurations under certain restraints.
 * <p>
 * Created by NyLP on 6/12/17.
 */

public class Chord {

    public static class Builder {
        private Note generator;
        private ChordNote root;
        private ArrayList<ChordNote> notes = new ArrayList<>();

        public Builder(Note note) {
            this.generator = note;
            notes.add(new ChordNote(note));
            this.root = notes.get(notes.size() - 1);
        }

        public Builder setRoot(Note root) {
            ChordNote tmp = null;
            for (ChordNote note : notes) {
                if (note.isEnharmonicNoClass(root)) {
                    tmp = note;
                    break;
                }
            }
            if (tmp == null)
                throw new IllegalArgumentException("Specified root note is not within the chord");
            else
                this.root = tmp;
            return this;
        }

        public Builder stackA(Interval interval) {
            return stackA(interval, false);
        }

        public Builder stackA(Interval interval, boolean isRoot) {
            notes.add(new ChordNote(notes.get(notes.size() - 1).interval(interval)));
            if (isRoot)
                root = notes.get(notes.size() - 1);
            return this;
        }

        public Builder stackFromGenerator(Interval interval) {
            return stackFromGenerator(interval, false);
        }

        public Builder stackFromGenerator(Interval interval, boolean isRoot) {
            notes.add(new ChordNote(generator.interval(interval)));
            if (isRoot)
                root = notes.get(notes.size() - 1);
            return this;
        }

        public Builder addNote(Note note) {
            return addNote(note, false);
        }

        public Builder addNote(Note note, boolean isRoot) {
            notes.add(new ChordNote(note));
            if (isRoot)
                root = notes.get(notes.size() - 1);
            return this;
        }

        public Builder notOmittable() {
            return notOmittable(notes.size() - 1);
        }

        public Builder omittable(int penalty) {
            return omittable(notes.size() - 1, penalty);
        }

        public Builder unrepeatable() {
            return unrepeatable(notes.size() - 1);
        }

        public Builder repeatable(int penalty) {
            return repeatable(notes.size() - 1, penalty);
        }

        public Builder tendency(Interval interval) {
            return tendency(notes.size() - 1, interval);
        }

        public Builder altTendency(Interval interval) {
            return altTendency(notes.size() - 1, interval);
        }

        public Builder preparedBy(Interval interval) {
            return preparedBy(notes.size() - 1, interval);
        }

        public Builder bonus(Interval interval, int bonus) {
            return bonus(notes.size() - 1, interval, bonus);
        }

        public Builder notOmittable(int index) {
            notes.get(index).setNotOmittable();
            return this;
        }

        public Builder omittable(int index, int penalty) {
            notes.get(index).setOmittable(penalty);
            return this;
        }

        public Builder unrepeatable(int index) {
            notes.get(index).setNotRepeatable();
            return this;
        }

        public Builder repeatable(int index, int penalty) {
            notes.get(index).setRepeatable(penalty);
            return this;
        }

        public Builder tendency(int index, Interval interval) {
            notes.get(index).addTendency(interval);
            return this;
        }

        public Builder altTendency(int index, Interval interval) {

            notes.get(index).addAltTendency(interval);
            return this;
        }

        public Builder preparedBy(int index, Interval interval) {

            notes.get(index).addPreparation(interval);
            return this;
        }

        public Builder bonus(int index, Interval interval, int bonus) {
            notes.get(index).addBonus(interval, bonus);
            return this;
        }

        public Chord build() {
            Collections.sort(notes);
            for (ChordNote note : notes) {
                if (!note.equals(root)) {
                    while (note.compareTo(root) < 0)
                        note.octaveUp();
                }
            }
            Collections.sort(notes);
            int oct = -root.getOctave();
            for (ChordNote note : notes)
                note.octave(oct);
            root.setNotOmittable();
            return new Chord(this);
        }
    }

    private ArrayList<ChordNote> noteList;
    private int inversion = 0;

    private Chord(Builder builder) {
        this.noteList = builder.notes;
    }

    public Chord(ArrayList<ChordNote> noteList) {
        this.noteList = new ArrayList<>(noteList);
    }

    public Chord(Chord chord) {
        this(chord.noteList);
    }

    public ArrayList<ChordNote> getChordNotes() {
        return noteList;
    }

    public ArrayList<Note> getNoteSet() {
        return (ArrayList<Note>) noteList.stream().map(ChordNote::getNote).collect(Collectors.toList());
    }

    public void setInversion(int inversion) {
        if (inversion < 0 || inversion >= noteList.size())
            throw new IllegalArgumentException("Inversion specified is impossible.");
        this.inversion = inversion;
    }

    public int getInversion() {
        return inversion;
    }

    public ChordNote getBass() {
        return noteList.get(inversion);
    }

    @Override
    public String toString() {
        return noteList.toString();
    }
}
