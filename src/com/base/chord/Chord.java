package com.base.chord;

import com.base.Interval;
import com.base.Note;
import com.base.progression.VoiceConfig;
import com.base.progression.VoiceNote;
import com.validation.ChordScorer;
import com.validation.ChordValidator;

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

    public class Builder {
        private Note generator;
        private Note root;
        private ArrayList<Note> notes = new ArrayList<>();

        public void setGenerator(Note note) {
            this.generator = note;
        }

        public void setRoot(Note note) {
            this.root = note;
        }

        public void stackA(Interval interval) {

        }

        public void stackFromRoot(Interval interval) {

        }

        public void addNote(Note note) {

        }

        public void omitability(int index, boolean omitable) {

        }

        public void repeatability(int index, boolean repeatable) {

        }

        public void omitPenalty(int index, int penalty) {

        }

        public void repeatPenalty(int index, int penalty) {

        }

        public void tendency(int index, Interval interval) {

        }

        public void altTendency(int index, Interval interval) {

        }

        public void preparedBy(int index, Interval interval) {

        }

        public void bonus(int index, Interval interval, int bonus) {
        }

        public Chord build() {
            return new Chord(new ArrayList<>());
        }
    }

    private ArrayList<ChordNote> noteList;

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

    @Override
    public String toString() {
        return noteList.toString();
    }
}
