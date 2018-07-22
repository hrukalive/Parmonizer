package com.validation;

import com.base.interval.Interval;
import com.base.realization.ChordVoicing.NoteCluster;
import com.base.progression.VoiceNote;

import java.util.ArrayList;

/**
 * Validator that implements the rules for voice-leading.
 * <p>
 * Created by NyLP on 6/20/17.
 */

public class VoiceLeadingValidator {
    public enum VoiceLeadingValidationResult {SUCCESS, VOICE_OVERLAP, LARGE_LEAP, AUG_DIM_INTERVAL, TENDENCY_NOT_RESOLVED, PARALLEL_PERFECT_INTERVAL, CONTRARY_PERFECTION_INTERVAL, NOT_PREPARED}

    public static VoiceLeadingValidationResult validate(NoteCluster nc1, NoteCluster nc2) {
        ArrayList<VoiceNote> nc1n = nc1.getNotes();
        ArrayList<VoiceNote> nc2n = nc2.getNotes();

        for (int i = 0; i < nc1n.size(); i++) {
            if (!nc2n.get(i).isInsisted()) {
                // No Voice-overlap
                if (i > 0 && i < nc1n.size() - 1 && !(nc2n.get(i).compareTo(nc1n.get(i - 1)) > 0 && nc2n.get(i).compareTo(nc1n.get(i + 1)) < 0))
                    return VoiceLeadingValidationResult.VOICE_OVERLAP;
                else if (i == 0 && !(nc2n.get(i).compareTo(nc1n.get(i + 1)) < 0))
                    return VoiceLeadingValidationResult.VOICE_OVERLAP;
                else if (i == nc1n.size() - 1 && !(nc2n.get(i).compareTo(nc1n.get(i - 1)) > 0))
                    return VoiceLeadingValidationResult.VOICE_OVERLAP;

                // No leap for larger than P4
                if (i > 0 && Math.abs(nc1n.get(i).dist(nc2n.get(i))) > Interval.parse("P4").semitones())
                    return VoiceLeadingValidationResult.LARGE_LEAP;

                // No Aug or Dim interval
                if ((nc1n.get(i).dist(nc2n.get(i)) > Interval.parse("M2").semitones() && nc1n.get(i).isAugmented(nc2n.get(i))) ||
                        (nc1n.get(i).dist(nc2n.get(i)) > Interval.parse("m2").semitones() && nc1n.get(i).isDiminished(nc2n.get(i))))
                    return VoiceLeadingValidationResult.AUG_DIM_INTERVAL;

                // Tendency tone must be resolved in the outer voices
                // Alternative resolution can happen with inner voices
                if (!nc1n.get(i).getTendencies().isEmpty() && nc1n.get(i).getTendencies().indexOf(nc2n.get(i)) == -1) {
                    if (i == 0 || i == nc1n.size() - 1)
                        return VoiceLeadingValidationResult.TENDENCY_NOT_RESOLVED;
                    if (nc1n.get(i).getAltTendency().isEmpty())
                        return VoiceLeadingValidationResult.TENDENCY_NOT_RESOLVED;
                    if (!nc1n.get(i).getAltTendency().isEmpty() && nc1n.get(i).getAltTendency().indexOf(nc2n.get(i)) == -1)
                        return VoiceLeadingValidationResult.TENDENCY_NOT_RESOLVED;
                }
            }
        }

        // No parallel perfect intervals
        for (int i = 0; i < nc1n.size() - 1 && !nc2n.get(i).isInsisted(); i++) {
            for (int j = i + 1; j < nc1n.size() && !nc2n.get(j).isInsisted(); j++) {
                if (!nc1n.get(i).equals(nc2n.get(i)) && nc1n.get(i).interval(nc1n.get(j)).equals(Interval.parse("P1")) &&
                        nc2n.get(i).interval(nc2n.get(j)).equals(Interval.parse("P1")))
                    return VoiceLeadingValidationResult.PARALLEL_PERFECT_INTERVAL;
                if (!nc1n.get(i).equals(nc2n.get(i)) && nc1n.get(i).interval(nc1n.get(j)).equals(Interval.parse("P4")) &&
                        nc2n.get(i).interval(nc2n.get(j)).equals(Interval.parse("P4")))
                    return VoiceLeadingValidationResult.PARALLEL_PERFECT_INTERVAL;
                if (!nc1n.get(i).equals(nc2n.get(i)) && nc1n.get(i).interval(nc1n.get(j)).equals(Interval.parse("P5")) &&
                        nc2n.get(i).interval(nc2n.get(j)).equals(Interval.parse("P5")))
                    return VoiceLeadingValidationResult.PARALLEL_PERFECT_INTERVAL;
                if (!nc1n.get(i).equals(nc2n.get(i)) && nc1n.get(i).interval(nc1n.get(j)).equals(Interval.parse("P8")) &&
                        nc2n.get(i).interval(nc2n.get(j)).equals(Interval.parse("P8")))
                    return VoiceLeadingValidationResult.PARALLEL_PERFECT_INTERVAL;
            }
        }

        // No contrary perfect intervals between outer voices
        if (!nc2n.get(nc1n.size() - 1).isInsisted() &&
                nc1n.get(0).compareTo(nc2n.get(0)) < 0 && nc1n.get(nc1n.size() - 1).compareTo(nc2n.get(nc1n.size() - 1)) > 0 &&
                Math.abs(nc1n.get(nc1n.size() - 1).dist(nc2n.get(nc1n.size() - 1))) > Interval.parse("M2").semitones()) {
            if (nc2n.get(0).interval(nc2n.get(nc1n.size() - 1)).equals(Interval.parse("P1")))
                return VoiceLeadingValidationResult.CONTRARY_PERFECTION_INTERVAL;
            if (nc2n.get(0).interval(nc2n.get(nc1n.size() - 1)).equals(Interval.parse("P4")))
                return VoiceLeadingValidationResult.CONTRARY_PERFECTION_INTERVAL;
            if (nc2n.get(0).interval(nc2n.get(nc1n.size() - 1)).equals(Interval.parse("P5")))
                return VoiceLeadingValidationResult.CONTRARY_PERFECTION_INTERVAL;
            if (nc2n.get(0).interval(nc2n.get(nc1n.size() - 1)).equals(Interval.parse("P8")))
                return VoiceLeadingValidationResult.CONTRARY_PERFECTION_INTERVAL;
        }

        for (int i = 0; i < nc2n.size(); i++) {
            if (!nc2n.get(i).isInsisted() && !nc2n.get(i).getPrepare().isEmpty() && nc2n.get(i).getPrepare().indexOf(nc1n.get(i)) == -1)
                return VoiceLeadingValidationResult.NOT_PREPARED;
        }
        return VoiceLeadingValidationResult.SUCCESS;
    }
}
