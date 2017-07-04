package com.base;

import java.util.ArrayList;

/**
 * Broader concept than a chord.
 * 
 * Created by NyLP on 6/20/17.
 */

public class NoteCluster
{
    protected final ArrayList<Note> cluster;

    public NoteCluster() { cluster = new ArrayList<>(); }
    public NoteCluster(NoteCluster cluster) { this.cluster = new ArrayList<>(cluster.getNotes()); }

    public void addVoice(Note note) { cluster.add(note); }
    public NoteCluster addVoiceNew(Note note)
    {
        NoteCluster ret = new NoteCluster(this);
        ret.addVoice(note);
        return ret;
    }

    public Note highestNote() { return cluster.get(cluster.size() - 1); }

    public ArrayList<Note> getNotes()
    {
        return cluster;
    }

    @Override public String toString()
    {
        return cluster.toString();
    }

    @Override public boolean equals(Object obj)
    {
        if (obj instanceof NoteCluster)
        {
            NoteCluster other = (NoteCluster)obj;
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
}
