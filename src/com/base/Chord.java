package com.base;

import com.validation.ChordScorer;
import com.validation.ChordValidator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Implemented the concept of a chord, with the functionality
 * to generate all possible configurations under certain restraints.
 * 
 * Created by NyLP on 6/12/17.
 */

public class Chord
{
    public static final class ChordNoteConfig
    {
        private final Note note;
        private final Tuple<Boolean, Integer> repeatConfig;
        private final Tuple<Boolean, Integer> omitConfig;
        private final ArrayList<Interval> prepareList = new ArrayList<>();
        private final ArrayList<Interval> tendencyList = new ArrayList<>();
        private final ArrayList<Interval> altTendencyList = new ArrayList<>();
        private final ArrayList<Tuple<Interval, Integer>> bonusList = new ArrayList<>();

        public ChordNoteConfig(Note note, Tuple<Boolean, Integer> repeatConfig, Tuple<Boolean, Integer> omitConfig)
        {
            this.note = note;
            this.repeatConfig = repeatConfig;
            this.omitConfig = omitConfig;
        }
        public ChordNoteConfig(ChordNoteConfig config)
        {
            this.note = Note.build(config.note);
            this.repeatConfig = config.repeatConfig;
            this.omitConfig = config.omitConfig;
            this.prepareList.addAll(config.prepareList);
            this.tendencyList.addAll(config.tendencyList);
            this.altTendencyList.addAll(config.altTendencyList);
            this.bonusList.addAll(config.bonusList);
        }

        public Note getNote()
        {
            return note;
        }

        public Tuple<Boolean, Integer> getRepeatConfig()
        {
            return repeatConfig;
        }

        public Tuple<Boolean, Integer> getOmitConfig()
        {
            return omitConfig;
        }

        public ArrayList<Interval> getPrepareList()
        {
            return prepareList;
        }

        public ArrayList<Interval> getTendencyList()
        {
            return tendencyList;
        }

        public ArrayList<Interval> getAltTendencyList()
        {
            return altTendencyList;
        }

        public ArrayList<Tuple<Interval, Integer>> getBonusList()
        {
            return bonusList;
        }
    }

    public static final class VoiceConfig
    {
        private final Note low;
        private final Note high;
        private ChordNoteConfig fixClassNote;
        private Note insistNote;
        private int unisonPenalty;

        public VoiceConfig(Note low, Note high)
        {
            this(low, high, null, null, 0);
        }
        public VoiceConfig(Note low, Note high, ChordNoteConfig fixClassNote, Note insistNote, int unisonPenalty)
        {
            this.low = low;
            this.high = high;
            this.fixClassNote = fixClassNote;
            this.insistNote = insistNote;
            this.unisonPenalty = unisonPenalty;
        }

        public Note getLow()
        {
            return low;
        }

        public Note getHigh()
        {
            return high;
        }

        public ChordNoteConfig getFixClassNote()
        {
            return fixClassNote;
        }

        public void setFixClassNote(ChordNoteConfig fixClassNote)
        {
            this.fixClassNote = fixClassNote;
        }

        public Note getInsistNote()
        {
            return insistNote;
        }

        public void setInsistNote(Note insistNote)
        {
            this.insistNote = insistNote;
        }

        public int getUnisonPenalty()
        {
            return unisonPenalty;
        }

        public void setUnisonPenalty(int unisonPenalty)
        {
            this.unisonPenalty = unisonPenalty;
        }
    }
    
    public class NoteCluster implements Comparable<NoteCluster>
    {
        private final ArrayList<Note> cluster;
        private int loss = 0;

        private NoteCluster() { cluster = new ArrayList<>(); }
        private NoteCluster(com.base.Chord.NoteCluster cluster)
        {
            this.cluster = new ArrayList<>(cluster.getNotes());
            this.loss = cluster.loss;
        }

        private void addVoice(Note note) { cluster.add(note); }

        private NoteCluster addVoiceNew(Note note)
        {
            NoteCluster ret = new NoteCluster(this);
            ret.addVoice(note);
            return ret;
        }

        private Note highestNote() { return cluster.size() == 0 ? null : cluster.get(cluster.size() - 1); }

        public ArrayList<Note> getNotes()
        {
            return cluster;
        }

        public int getLoss()
        {
            return loss;
        }

        private void setLoss(int loss)
        {
            this.loss = loss;
        }

        private void replace(int index, Note note)
        {
            cluster.remove(index);
            cluster.add(index, Note.build(note));
        }

        @Override public int compareTo(com.base.Chord.NoteCluster o)
        {
            if (o == null) throw new NullPointerException("Null Chord to compare");
            if (loss > o.loss) return 1;
            if (loss < o.loss) return -1;
            return 0;
        }

        @Override public boolean equals(Object obj)
        {
            if (obj instanceof com.base.Chord.NoteCluster)
            {
                com.base.Chord.NoteCluster other = (com.base.Chord.NoteCluster)obj;
                if (cluster.size() == other.cluster.size())
                {
                    for (int i = 0; i < cluster.size(); i++)
                    {
                        if (!cluster.get(i).equals(other.cluster.get(i)))
                            return false;
                    }
                }
                else
                    return false;
                return true;
            }
            return false;
        }

        @Override public String toString()
        {
            return cluster.toString();
        }
    }

    private ArrayList<ChordNoteConfig> noteList;
    private ArrayList<VoiceConfig> voiceList;
    
    private ChordValidator validator;
    private ChordScorer scorer;

    private ArrayList<NoteCluster> realizations = new ArrayList<>();

    public Chord(ArrayList<ChordNoteConfig> noteList, ArrayList<VoiceConfig> voiceList)
    {
        this.noteList = new ArrayList<>(noteList);
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
    public Chord(Chord chord)
    {
        this(chord.noteList, chord.voiceList);
    }

    public void yield()
    {
        realizations.clear();
        NoteCluster temp = new NoteCluster();
        yieldHelper(0, temp, voiceList.size());
    }
    
    private void yieldHelper(int num, NoteCluster accum, int voices)
    {
        if (num == voices)
        {
            if (validator.validate(accum.getNotes(), this))
            {
                accum.setLoss(scorer.score(accum.getNotes(), this));
                
                if (realizations.indexOf(accum) == -1)
                    realizations.add(accum);
            }
        }
        else if (num < voices)
        {
            VoiceConfig voiceConfig = voiceList.get(num);
            if (voiceConfig.getInsistNote() != null)
            {
                Note note = voiceConfig.getInsistNote();
                note.setInsisted();
                yieldHelper(num + 1, accum.addVoiceNew(note), voices);
            }
            else if (voiceConfig.getFixClassNote() != null)
                for (Note note : voiceConfig.getFixClassNote().getNote().allInRange(voiceConfig.getLow(), voiceConfig.getHigh()))
                    yieldHelper(num + 1, accum.addVoiceNew(note), voices);
            else
            {
                for (ChordNoteConfig noteConfig : noteList)
                {
                    for (Note note : noteConfig.getNote().allInRange(voiceConfig.getLow(), voiceConfig.getHigh()))
                    {
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

    public ArrayList<Note> getNoteSet()
    {
        return (ArrayList<Note>)noteList.stream().map(ChordNoteConfig::getNote).collect(Collectors.toList());
    }

    public ArrayList<NoteCluster> getRealizations()
    {
        Collections.sort(realizations);
        return realizations;
    }

    public Note getBass()
    {
        return voiceList.get(0).getFixClassNote().getNote();
    }

    @Override public String toString()
    {
        return noteList.toString();
    }
}
