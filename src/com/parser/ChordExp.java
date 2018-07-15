package com.parser;

import com.base.chord.Chord;
import com.base.chord.ChordNote;
import com.base.progression.VoiceConfig;
import com.base.realization.ChordVoicing;

import java.util.ArrayList;

/**
 * Created by NyLP on 7/16/17.
 */

public class ChordExp implements IParserExp<ChordVoicing>
{
    private final ChordVoicing chord;
    public ChordExp(ArrayList<ChordNote> noteList, ArrayList<VoiceConfig> voiceList)
    {
        chord = new ChordVoicing(new Chord(noteList), voiceList);
    }
    @Override public ChordVoicing eval()
    {
        return chord;
    }
    @Override public String remaining()
    {
        return "";
    }
}
