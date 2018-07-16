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
