package com.base.realization;

import com.base.Note;
import com.base.chord.Chord;
import com.base.chord.ChordNote;
import com.base.progression.VoiceConfig;
import com.base.progression.VoiceNote;
import com.validation.ChordScorer;
import com.validation.ChordValidator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class ChordVoicing {

    public class NoteCluster implements Comparable<NoteCluster> {
        private final ArrayList<VoiceNote> cluster;
        private int loss = 0;

        public NoteCluster() {
            cluster = new ArrayList<>();
        }

        private NoteCluster(NoteCluster cluster) {
            this.cluster = new ArrayList<>(cluster.getNotes());
            this.loss = cluster.loss;
        }

        private void addVoice(VoiceNote note) {
            cluster.add(note);
        }

        private NoteCluster addVoiceNew(VoiceNote note) {
            NoteCluster ret = new NoteCluster(this);
            ret.addVoice(note);
            return ret;
        }

        private Note highestNote() {
            return cluster.size() == 0 ? null : cluster.get(cluster.size() - 1);
        }

        public ArrayList<VoiceNote> getNotes() {
            return cluster;
        }

        public int getLoss() {
            return loss;
        }

        private void setLoss(int loss) {
            this.loss = loss;
        }

        private void replace(int index, VoiceNote note) {
            cluster.remove(index);
            cluster.add(index, new VoiceNote(note));
        }

        @Override
        public int compareTo(NoteCluster o) {
            if (o == null) throw new NullPointerException("Null Chord to compare");
            if (loss > o.loss) return 1;
            if (loss < o.loss) return -1;
            return 0;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof NoteCluster) {
                NoteCluster other = (NoteCluster) obj;
                if (cluster.size() == other.cluster.size()) {
                    for (int i = 0; i < cluster.size(); i++) {
                        if (!cluster.get(i).equals(other.cluster.get(i)))
                            return false;
                    }
                } else
                    return false;
                return true;
            }
            return false;
        }

        @Override
        public String toString() {
            return cluster.toString();
        }
    }

    private Chord chord;
    private ArrayList<ChordNote> noteList;
    private ArrayList<VoiceConfig> voiceList;

    private ChordValidator validator;
    private ChordScorer scorer;

    private ArrayList<NoteCluster> realizations = new ArrayList<>();

    public ChordVoicing(Chord chord, ArrayList<VoiceConfig> voiceList) {
        this.chord = new Chord(chord);
        this.noteList = new ArrayList<>(chord.getChordNotes());
        this.voiceList = new ArrayList<>(voiceList);
        ArrayList<Integer> unisonPenalty = new ArrayList<>();
        for (int i = 0; i < voiceList.size() - 1; i++)
            unisonPenalty.add(voiceList.get(i).getUnisonPenalty());
        validator = new ChordValidator((ArrayList<Boolean>) noteList.stream().map(n -> n.getRepeatConfig().getFirst()).collect(Collectors.toList()),
                (ArrayList<Boolean>) noteList.stream().map(n -> n.getOmitConfig().getFirst()).collect(Collectors.toList()));
        scorer = new ChordScorer((ArrayList<Integer>) noteList.stream().map(n -> n.getRepeatConfig().getSecond()).collect(Collectors.toList()),
                (ArrayList<Integer>) noteList.stream().map(n -> n.getOmitConfig().getSecond()).collect(Collectors.toList()),
                unisonPenalty);
    }
    public ChordVoicing(ChordVoicing other) {
        this(other.chord, other.voiceList);
    }

    public void yield() {
        realizations.clear();
        NoteCluster temp = new NoteCluster();
        yieldHelper(0, temp, voiceList.size());
    }

    private void yieldHelper(int num, NoteCluster accum, int voices) {
        if (num == voices) {
            if (validator.validate(accum.getNotes(), this)) {
                accum.setLoss(scorer.score(accum.getNotes(), this));

                if (realizations.indexOf(accum) == -1)
                    realizations.add(accum);
            }
        } else if (num < voices) {
            VoiceConfig voiceConfig = voiceList.get(num);
            if (voiceConfig.getInsistNote() != null) {
                VoiceNote note = new VoiceNote(voiceConfig.getInsistNote());
                note.setInsisted();
                yieldHelper(num + 1, accum.addVoiceNew(note), voices);
            } else if (voiceConfig.getFixClassNote() != null)
                for (VoiceNote note : new VoiceNote(voiceConfig.getFixClassNote().getNote()).allInRange(voiceConfig.getLow(), voiceConfig.getHigh()))
                    yieldHelper(num + 1, accum.addVoiceNew(note), voices);
            else {
                for (ChordNote noteConfig : noteList) {
                    for (VoiceNote note : new VoiceNote(noteConfig.getNote()).allInRange(voiceConfig.getLow(), voiceConfig.getHigh())) {
                        noteConfig.getPrepareList().forEach(intv -> note.addPrepare(note.interval(intv)));
                        noteConfig.getTendencyList().forEach(intv -> note.addTendency(note.interval(intv)));
                        noteConfig.getAltTendencyList().forEach(intv -> note.addAltTendency(note.interval(intv)));
                        noteConfig.getBonusList().forEach(tuple -> note.addBonus(note.interval(tuple.getFirst()), tuple.getSecond()));

                        if (accum.highestNote() == null || note.compareTo(accum.highestNote()) >= 0)
                            yieldHelper(num + 1, accum.addVoiceNew(note), voices);
                    }
                }
            }
        }
    }

    public ArrayList<Note> getNoteSet() {
        return chord.getNoteSet();
    }

    public ArrayList<NoteCluster> getRealizations() {
        Collections.sort(realizations);
        return realizations;
    }

    public Note getBass() {
        return voiceList.get(0).getFixClassNote().getNote();
    }
}
