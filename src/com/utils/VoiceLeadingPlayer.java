package com.utils;

import com.base.Note;
import com.base.progression.VoiceNote;
import com.base.realization.ChordVoicing;
import com.base.realization.VoiceLeading;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

public final class VoiceLeadingPlayer {
    public static void play(VoiceLeading voiceLeading) {
        Synthesizer synth = null;
        try {
            synth = MidiSystem.getSynthesizer();
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
            System.exit(1);
        }
        try {
            synth.open();
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
            System.exit(1);
        }
//            for (Instrument inst : synth.getAvailableInstruments())
//                System.out.println(inst._toString());
        MidiChannel[] channels = synth.getChannels();
        MidiChannel channel = channels[0];
        channel.programChange(48);

        for (ChordVoicing.NoteCluster nc : voiceLeading.getPiece()) {
            for (VoiceNote n : nc.getNotes()) {
                channel.noteOn(n.getNote().getCode(), 50);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            channel.allNotesOff();
        }
        synth.close();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
    }
}
