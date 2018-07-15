package com.base.progression;

import com.base.Note;

import java.util.ArrayList;
import java.util.HashMap;

public class VoiceNote extends Note
{
    private boolean insisted;
    private final ArrayList<Note> tendency;
    private final ArrayList<Note> altTendency;
    private final HashMap<Note, Integer> bonusMap;
    private final ArrayList<Note> prepareReq;

    public VoiceNote(int noteCode, int octave, int alteration)
    {
        super(noteCode, octave, alteration);
        this.insisted = false;
        this.tendency = new ArrayList<>();
        this.altTendency = new ArrayList<>();
        this.bonusMap = new HashMap<>();
        this.prepareReq = new ArrayList<>();
    }

    public VoiceNote(Note note)
    {
        super(note);
        this.insisted = false;
        this.tendency = new ArrayList<>();
        this.altTendency = new ArrayList<>();
        this.bonusMap = new HashMap<>();
        this.prepareReq = new ArrayList<>();
    }

    public VoiceNote(VoiceNote note)
    {
        super(note._noteCode, note._octave, note._alteration);
        this.insisted = note.insisted;
        this.tendency = new ArrayList<>(note.tendency);
        this.altTendency = new ArrayList<>(note.altTendency);
        this.bonusMap = new HashMap<>(note.bonusMap);
        this.prepareReq = new ArrayList<>(note.prepareReq);
    }

    public ArrayList<VoiceNote> allInRange(Note lo, Note hi)
    {
        ArrayList<VoiceNote> ret = new ArrayList<>();
        int temp_octave = 0;
        while (lo.getCode() > _getCode(_noteCode, temp_octave, _alteration)) temp_octave++;
        while (hi.getCode() >= _getCode(_noteCode, temp_octave, _alteration))
        {
            ret.add(new VoiceNote(_noteCode, temp_octave, _alteration));
            temp_octave++;
        }
        return ret;
    }

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
}
