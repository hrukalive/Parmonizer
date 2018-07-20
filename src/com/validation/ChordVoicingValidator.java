package com.validation;

import com.base.chord.Chord;
import com.base.Note;
import com.base.progression.VoiceNote;
import com.base.realization.ChordVoicing;

import java.util.ArrayList;

/**
 * Validator that implements the rules when voicing a chord.
 * <p>
 * Created by NyLP on 6/16/17.
 */
public final class ChordVoicingValidator {
    private final ArrayList<Boolean> repeatability;
    private final ArrayList<Boolean> omissibility;

    public ChordVoicingValidator(ArrayList<Boolean> repeatability, ArrayList<Boolean> omissibility) {
        this.repeatability = repeatability;
        this.omissibility = omissibility;
    }

    public boolean validate(ArrayList<VoiceNote> chordRealization, ChordVoicing parent) {
        boolean isChord = false;
        for (int i = 0; i < chordRealization.size() - 1; i++) {
            if (!chordRealization.get(i + 1).isEnharmonicNoClass(chordRealization.get(i))) {
                isChord = true;
                break;
            }
        }
        if (!isChord)
            return false;

        for (int i = 1; i < chordRealization.size() - 1; i++) {
            if (chordRealization.get(i + 1).getCode() - chordRealization.get(i).getCode() > 12)
                return false;
        }

        ArrayList<Note> chordTones = parent.getNoteSet();
        for (int i = 0; i < omissibility.size(); i++) {
            if (!omissibility.get(i)) {
                boolean existFlag = false;
                Note notOmitNote = chordTones.get(i);
                for (Note note : chordRealization) {
                    if (note.isEnharmonicNoClass(notOmitNote)) {
                        existFlag = true;
                        break;
                    }
                }
                if (!existFlag)
                    return false;
            }
        }

        int[] counter = new int[chordTones.size()];
        for (Note note : chordRealization) {
            for (int i = 0; i < chordTones.size(); i++) {
                if (note.isEnharmonicNoClass(chordTones.get(i))) {
                    counter[i]++;
                    break;
                }
            }
        }
        for (int i = 0; i < chordTones.size(); i++)
            if (counter[i] > 1 && !repeatability.get(i))
                return false;

        return true;
    }
}