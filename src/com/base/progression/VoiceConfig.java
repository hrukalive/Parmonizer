package com.base.progression;

import com.base.Note;
import com.base.chord.ChordNote;

public final class VoiceConfig {
    private final Note low;
    private final Note high;
    private ChordNote fixClassNote;
    private Note insistNote;
    private int unisonPenalty;

    public VoiceConfig(Note low, Note high) {
        this(low, high, null, null, 0);
    }

    public VoiceConfig(Note low, Note high, ChordNote fixClassNote, Note insistNote, int unisonPenalty) {
        this.low = low;
        this.high = high;
        this.fixClassNote = fixClassNote;
        this.insistNote = insistNote;
        this.unisonPenalty = unisonPenalty;
    }

    public Note getLow() {
        return low;
    }

    public Note getHigh() {
        return high;
    }

    public ChordNote getFixClassNote() {
        return fixClassNote;
    }

    public void setFixClassNote(ChordNote fixClassNote) {
        this.fixClassNote = fixClassNote;
    }

    public Note getInsistNote() {
        return insistNote;
    }

    public void setInsistNote(Note insistNote) {
        this.insistNote = insistNote;
    }

    public int getUnisonPenalty() {
        return unisonPenalty;
    }

    public void setUnisonPenalty(int unisonPenalty) {
        this.unisonPenalty = unisonPenalty;
    }
}
