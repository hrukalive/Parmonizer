package com.base.realization;

import com.base.Progression;

import java.util.ArrayList;

public class VoiceLeading implements Comparable<VoiceLeading>
{
    private ArrayList<ChordVoicing.NoteCluster> piece;
    private int loss = 0;

    public VoiceLeading() { piece = new ArrayList<>(); }
    public VoiceLeading(VoiceLeading vl) { piece = new ArrayList<>(vl.piece); loss = vl.loss; }

    public void addChord(ChordVoicing.NoteCluster notes) { piece.add(notes); }
    public VoiceLeading addChordNew(ChordVoicing.NoteCluster notes)
    {
        VoiceLeading ret = new VoiceLeading(this);
        ret.addChord(notes);
        return ret;
    }

    public int getLoss()
    {
        return loss;
    }

    public void setLoss(int loss)
    {
        this.loss = loss;
    }

    public ArrayList<ChordVoicing.NoteCluster> getPiece()
    {
        return piece;
    }

    public ChordVoicing.NoteCluster lastChord() { return piece.get(piece.size() - 1); }

    @Override public int compareTo(VoiceLeading o)
    {
        if (o == null) throw new NullPointerException("Null VoiceLeading to compare");
        if (loss > o.loss) return 1;
        if (loss < o.loss) return -1;
        return 0;
    }

    @Override public String toString()
    {
        String temp = "";
        for (int i = 0; i < piece.size() - 1; i++)
        {
            temp += piece.get(i).toString() + " -> ";
        }
        temp += piece.get(piece.size() - 1).toString();
        return temp;
    }
}
