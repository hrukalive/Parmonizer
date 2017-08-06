package com.parser;

import com.base.Chord;

import java.util.ArrayList;

/**
 * Created by NyLP on 7/16/17.
 */

public class ChordExp implements IParserExp<Chord>
{
    private final Chord chord;
    public ChordExp(ArrayList<Chord.ChordNoteConfig> noteList, ArrayList<Chord.VoiceConfig> voiceList)
    {
        chord = new Chord(noteList, voiceList);
    }
    @Override public Chord eval()
    {
        return chord;
    }
    @Override public String remaining()
    {
        return "";
    }
}
