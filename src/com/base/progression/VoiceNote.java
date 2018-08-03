package com.base.progression;

import com.base.Note;

import java.util.ArrayList;
import java.util.HashMap;

public class VoiceNote implements Comparable<VoiceNote>
{
    private Note note;
    private boolean insisted;
    private final ArrayList<Note> tendency;
    private final ArrayList<Note> altTendency;
    private final HashMap<Note, Integer> bonusMap;
    private final ArrayList<Note> prepareReq;

    public VoiceNote(Note note)
    {
        this.note = note;
        this.insisted = false;
        this.tendency = new ArrayList<>();
        this.altTendency = new ArrayList<>();
        this.bonusMap = new HashMap<>();
        this.prepareReq = new ArrayList<>();
    }

    public VoiceNote(VoiceNote note)
    {
        this.note = note.note;
        this.insisted = note.insisted;
        this.tendency = new ArrayList<>(note.tendency);
        this.altTendency = new ArrayList<>(note.altTendency);
        this.bonusMap = new HashMap<>(note.bonusMap);
        this.prepareReq = new ArrayList<>(note.prepareReq);
    }

    public ArrayList<VoiceNote> allInRange(Note lo, Note hi)
    {
        ArrayList<VoiceNote> ret = new ArrayList<>();
        Note tmp = Note.parse(note.getNoteCode(), note.getAlteration(), 0);
        while (lo.compareTo(tmp) > 0) tmp = tmp.octaveUp();
        while (hi.compareTo(tmp) >= 0)
        {
            ret.add(new VoiceNote(tmp));
            tmp = tmp.octaveUp();
        }
        return ret;
    }

    public Note getNote() {
        return note;
    }

    public boolean equalsNoClass(VoiceNote note) { return note != null && this.note.equalsNoClass(note.getNote()); }

    public void setInsisted()
    {
        this.insisted = true;
    }

    public void addTendency(Note note) { tendency.add(note); }

    public void addAltTendency(Note note) { altTendency.add(note); }

    public void addBonus(Note note, int value) { bonusMap.put(note, value); }

    public void addPrepare(Note note) { prepareReq.add(note); }

    public boolean isInsisted() { return insisted; }

    public ArrayList<Note> getTendencies() { return tendency; }

    public ArrayList<Note> getAltTendency() { return altTendency; }

    public HashMap<Note, Integer> getBonus() { return bonusMap; }

    public ArrayList<Note> getPrepare() { return prepareReq; }

    @Override
    public int compareTo(VoiceNote o) {
        if (o == null) throw new NullPointerException("Null NoteStruct to compare");
        return this.note.compareTo(o.note);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Note)
            return ((Note)obj).equals(this.note);
        else if (obj instanceof VoiceNote)
            return ((VoiceNote)obj).getNote().equals(this.note);
        return false;
    }

    @Override
    public String toString() {
        return note.toString();
    }
}
