package com.base.realization;

import com.base.Note;
import com.base.chord.Chord;
import com.base.chord.ChordNote;
import com.base.progression.VoiceConfig;
import com.base.progression.VoiceNote;
import com.validation.ChordVoicingScorer;
import com.validation.ChordVoicingValidator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class ChordVoicing {

    public class NoteCluster implements Comparable<NoteCluster> {
        private ArrayList<VoiceNote> cluster = new ArrayList<>();
        private ArrayList<VoiceNote> distinctNotes = new ArrayList<>();
        private int loss = 0;

        public NoteCluster() {
        }

        private NoteCluster(NoteCluster cluster) {
            this.cluster = new ArrayList<>(cluster.cluster);
            this.distinctNotes = new ArrayList<>(cluster.distinctNotes);
            this.loss = cluster.loss;
        }

        private void addVoice(VoiceNote note) {
            cluster.add(note);
            for (Note n : distinctNotes) {
                if (!n.isEnharmonicNoClass(note))
                    distinctNotes.add(note);
            }
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

        @Override
        public int compareTo(NoteCluster o) {
            if (o == null) throw new NullPointerException("Null Chord to compare");
            if (distinctNotes.size() > o.distinctNotes.size()) return 0;
            if (distinctNotes.size() < o.distinctNotes.size()) return 1;
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

    private ChordVoicingValidator validator;
    private ChordVoicingScorer scorer;

    private ArrayList<NoteCluster> realizations = new ArrayList<>();

    public ChordVoicing(Chord chord, ArrayList<VoiceConfig> voiceList) {
        this.chord = new Chord(chord);
        this.noteList = new ArrayList<>(chord.getChordNotes());
        this.voiceList = new ArrayList<>(voiceList);
        ArrayList<Integer> unisonPenalty = new ArrayList<>();
        for (int i = 0; i < voiceList.size() - 1; i++)
            unisonPenalty.add(voiceList.get(i).getUnisonPenalty());
        validator = new ChordVoicingValidator((ArrayList<Boolean>) noteList.stream().map(ChordNote::isRepeatable).collect(Collectors.toList()),
                (ArrayList<Boolean>) noteList.stream().map(ChordNote::isOmittable).collect(Collectors.toList()));
        scorer = new ChordVoicingScorer((ArrayList<Integer>) noteList.stream().map(ChordNote::getRepeatPenalty).collect(Collectors.toList()),
                (ArrayList<Integer>) noteList.stream().map(ChordNote::getOmitPenalty).collect(Collectors.toList()),
                unisonPenalty);
    }
    public ChordVoicing(ChordVoicing other) {
        this(other.chord, other.voiceList);
    }

    public void yield() {
        realizations.clear();
        NoteCluster temp = new NoteCluster();
        yieldHelper(0, temp, voiceList.size());
        Collections.sort(realizations);
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
                for (VoiceNote note : new VoiceNote(voiceConfig.getFixClassNote()).allInRange(voiceConfig.getLow(), voiceConfig.getHigh()))
                    yieldHelper(num + 1, accum.addVoiceNew(note), voices);
            else {
                for (ChordNote noteConfig : noteList) {
                    for (VoiceNote note : new VoiceNote(noteConfig).allInRange(voiceConfig.getLow(), voiceConfig.getHigh())) {
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
        return voiceList.get(0).getFixClassNote();
    }
}
