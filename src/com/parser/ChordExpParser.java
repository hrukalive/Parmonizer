package com.parser;

import com.base.Chord;
import com.base.Tuple;
import com.base.Note;
import com.base.Chord.ChordNoteConfig;
import com.base.Chord.VoiceConfig;
import com.base.Interval;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser for Chord Expression:
 * [(,?*?ChordNote)+(;SlashBass)?][# of voices][(;?range for a voice){# of voices}]
 * [p:(;?Interval(/|\)>voice)*]
 * [t:(;?voice(/|\)>Interval)*]
 * [b:(;?voice(/|\)>Interval(bonus point))*]
 * 
 * [r:(;?F|T(penalty)){# of ChordNotes}]
 * [o:(;?F|T(penalty)){# of ChordNotes}]
 * [u:(;?penalty){# of voice - 1}]
 * 
 * [f:(;?voice=NoteStruct)*]
 * [i:(;?voice=NoteStruct)*]
 * 
 * Created by NyLP on 7/16/17.
 */

public class ChordExpParser implements IParser<Chord>
{
    @Override public ChordExp parse(String expr)
    {
        Pattern pattern_noteset = Pattern.compile("\\[(((,?\\*?[A-G]((bb)|b|#|x)?(-?\\d+)?)+)(;([A-G]((bb)|b|#|x)?(-?\\d+)?))?)\\]");
        Pattern pattern_voices = Pattern.compile("\\[((\\d+))\\]");
        Pattern pattern_ranges = Pattern.compile("\\[(((([A-G]((bb)|b|#|x)?(-?\\d+)):([A-G]((bb)|b|#|x)?(-?\\d)));?)+)\\]");
        Pattern pattern_prepare = Pattern.compile("\\[p:(([PmMad]\\d[/\\\\]>\\d;?)+)?\\]");
        Pattern pattern_tendency = Pattern.compile("\\[t:((((\\d[/\\\\]>[PmMad]\\d)|(\\(\\d[/\\\\]>[PmMad]\\d\\)));?)+)?\\]");
        Pattern pattern_bonus = Pattern.compile("\\[b:((\\d[/\\\\]>[PmMad]\\d\\(\\d+\\);?)+)?\\]");
        Pattern pattern_repeat = Pattern.compile("\\[r:(((F|(T\\(\\d+\\)));?)+)\\]");
        Pattern pattern_omit = Pattern.compile("\\[o:(((F|(T\\(\\d+\\)));?)+)\\]");
        Pattern pattern_unisionPenalty = Pattern.compile("\\[u:(((\\d+);?)+)\\]");
        Pattern pattern_fixClass = Pattern.compile("\\[f:(((\\d=[A-G]((bb)|b|#|x)?);?)+)?\\]");
        Pattern pattern_insistNote = Pattern.compile("\\[i:(((\\d=[A-G]((bb)|b|#|x)?(-?\\d+));?)+)?\\]");
        
        Matcher matcher_noteset = pattern_noteset.matcher(expr);
        Matcher matcher_voices = pattern_voices.matcher(expr);
        Matcher matcher_ranges = pattern_ranges.matcher(expr);
        Matcher matcher_prepares = pattern_prepare.matcher(expr);
        Matcher matcher_tendencies = pattern_tendency.matcher(expr);
        Matcher matcher_bonus = pattern_bonus.matcher(expr);
        Matcher matcher_repeats = pattern_repeat.matcher(expr);
        Matcher matcher_omits = pattern_omit.matcher(expr);
        Matcher matcher_unisonPenalties = pattern_unisionPenalty.matcher(expr);
        Matcher matcher_fixClasses = pattern_fixClass.matcher(expr);
        Matcher matcher_insistNotes = pattern_insistNote.matcher(expr);

        ArrayList<ChordNoteConfig> noteList = new ArrayList<>();
        Note inv_bass = null;
        Note int_bass = null;
        
        if (matcher_noteset.find() && matcher_repeats.find() && matcher_omits.find())
        {
            int_bass = matcher_noteset.group(8) != null ? Note.parse(matcher_noteset.group(8)) : null;
            ArrayList<String> noteStrs = new ArrayList<>(Arrays.asList(matcher_noteset.group(2).split(",")));
            ArrayList<String> repeatStrs = new ArrayList<>(Arrays.asList(matcher_repeats.group(1).split(";")));
            ArrayList<String> omitStrs = new ArrayList<>(Arrays.asList(matcher_omits.group(1).split(";")));
            if (noteStrs.size() == repeatStrs.size() && repeatStrs.size() == omitStrs.size())
            {
                for (int i = 0; i < noteStrs.size(); i++)
                {
                    Pattern pattern_value = Pattern.compile("\\(((\\d)+)\\)");
                    boolean repeatability = false, omissibility = false;
                    int repeatPenalty = 0, omitPenalty = 0;
                    if (repeatStrs.get(i).startsWith("T"))
                    {
                        repeatability = true;
                        Matcher matcher_value = pattern_value.matcher(repeatStrs.get(i));
                        if (matcher_value.find())
                            repeatPenalty = Integer.parseInt(matcher_value.group(1));
                        else
                            throw new IllegalArgumentException("Missing penalty value for repeatable notes.");
                    }
                    if (omitStrs.get(i).startsWith("T"))
                    {
                        omissibility = true;
                        Matcher matcher_value = pattern_value.matcher(omitStrs.get(i));
                        if (matcher_value.find())
                            omitPenalty = Integer.parseInt(matcher_value.group(1));
                        else
                            throw new IllegalArgumentException("Missing penalty value for omissible notes.");
                    }
                    
                    Note note = null;
                    if (noteStrs.get(i).startsWith("*"))
                    {
                        inv_bass = Note.parse(noteStrs.get(i).substring(1));
                        note = Note.build(inv_bass);
                    }
                    else
                        note = Note.parse(noteStrs.get(i));
                    noteList.add(new ChordNoteConfig(note, new Tuple<>(repeatability, repeatPenalty), new Tuple<>(omissibility, omitPenalty)));
                }
            }
            else
                throw new IllegalArgumentException("String is not legal.");
        }
        
        if (matcher_prepares.find() && matcher_prepares.group(1) != null)
        {
            Arrays.asList(matcher_prepares.group(1).split(";")).forEach(
                    prepareStr ->
                    {
                        Matcher matcher_prepare_up = Pattern.compile("([PmMad]\\d)/>((\\d))").matcher(prepareStr);
                        Matcher matcher_prepare_down = Pattern.compile("([PmMad]\\d)\\\\>((\\d))").matcher(prepareStr);
                        if (matcher_prepare_up.find())
                        {
                            int notenum = Integer.parseInt(matcher_prepare_up.group(2)) - 1;
                            if (notenum >= noteList.size())
                                throw new IllegalArgumentException("Specified preparing request number out of bound.");
                            noteList.get(notenum).getPrepareList().add(Interval.parse("+" + matcher_prepare_up.group(1)));
                        }
                        else if (matcher_prepare_down.find())
                        {
                            int notenum = Integer.parseInt(matcher_prepare_down.group(2)) - 1;
                            if (notenum >= noteList.size())
                                throw new IllegalArgumentException("Specified preparing request number out of bound.");
                            noteList.get(notenum).getPrepareList().add(Interval.parse("-" + matcher_prepare_down.group(1)));
                        }
                    });
        }
        
        if (matcher_tendencies.find() && matcher_tendencies.group(1) != null)
        {
            Arrays.asList(matcher_tendencies.group(1).split(";")).forEach(
                    tendencyStr ->
                    {
                        Matcher matcher_tendency_up = Pattern.compile("(\\d)/>(([PmMad]\\d))").matcher(tendencyStr);
                        Matcher matcher_tendency_down = Pattern.compile("(\\d)\\\\>(([PmMad]\\d))").matcher(tendencyStr);
                        Matcher matcher_alt_tendency_up = Pattern.compile("\\((\\d)/>(([PmMad]\\d))\\)").matcher(tendencyStr);
                        Matcher matcher_alt_tendency_down = Pattern.compile("\\((\\d)\\\\>(([PmMad]\\d))\\)").matcher(tendencyStr);
                        if (matcher_alt_tendency_up.find())
                        {
                            int notenum = Integer.parseInt(matcher_alt_tendency_up.group(1)) - 1;
                            if (notenum >= noteList.size())
                                throw new IllegalArgumentException("Specified alternative tendency request number out of bound.");
                            noteList.get(notenum).getAltTendencyList().add(Interval.parse("+" + matcher_alt_tendency_up.group(2)));
                        }
                        else if (matcher_alt_tendency_down.find())
                        {
                            int notenum = Integer.parseInt(matcher_alt_tendency_down.group(1)) - 1;
                            if (notenum >= noteList.size())
                                throw new IllegalArgumentException("Specified alternative tendency request number out of bound.");
                            noteList.get(notenum).getAltTendencyList().add(Interval.parse("-" + matcher_alt_tendency_down.group(2)));
                        }
                        else if (matcher_tendency_up.find())
                        {
                            int notenum = Integer.parseInt(matcher_tendency_up.group(1)) - 1;
                            if (notenum >= noteList.size())
                                throw new IllegalArgumentException("Specified tendency request number out of bound.");
                            noteList.get(notenum).getTendencyList().add(Interval.parse("+" + matcher_tendency_up.group(2)));
                        }
                        else if (matcher_tendency_down.find())
                        {
                            int notenum = Integer.parseInt(matcher_tendency_down.group(1)) - 1;
                            if (notenum >= noteList.size())
                                throw new IllegalArgumentException("Specified tendency request number out of bound.");
                            noteList.get(notenum).getTendencyList().add(Interval.parse("-" + matcher_tendency_down.group(2)));
                        }
                    });
        }
        
        if (matcher_bonus.find() && matcher_bonus.group(1) != null)
        {
            Arrays.asList(matcher_bonus.group(1).split(";")).forEach(
                    bonusStr ->
                    {
                        Matcher matcher_bonus_up = Pattern.compile("(\\d)/>([PmMad]\\d)(\\(((\\d)+)\\))").matcher(bonusStr);
                        Matcher matcher_bonus_down = Pattern.compile("(\\d)\\\\>([PmMad]\\d)(\\(((\\d)+)\\))").matcher(bonusStr);
                        if (matcher_bonus_up.find())
                        {
                            int notenum = Integer.parseInt(matcher_bonus_up.group(1)) - 1;
                            if (notenum >= noteList.size())
                                throw new IllegalArgumentException("Specified preparing request number out of bound.");
                            noteList.get(notenum).getBonusList().add(new Tuple<>(Interval.parse("+" + matcher_bonus_up.group(2)),
                                                                                  Integer.parseInt(matcher_bonus_up.group(4))));
                        }
                        else if (matcher_bonus_down.find())
                        {
                            int notenum = Integer.parseInt(matcher_bonus_down.group(1)) - 1;
                            if (notenum >= noteList.size())
                                throw new IllegalArgumentException("Specified preparing request number out of bound.");
                            noteList.get(notenum).getBonusList().add(new Tuple<>(Interval.parse("-" + matcher_bonus_down.group(2)),
                                                                                  Integer.parseInt(matcher_bonus_down.group(4))));
                        }
                    });
        }

        final int voices;
        if (matcher_voices.find())
            voices = Integer.parseInt(matcher_voices.group(1));
        else
            throw new IllegalArgumentException("#voice should be specified.");
        
        ArrayList<VoiceConfig> voiceList = new ArrayList<>();
        
        if (matcher_ranges.find())
        {
            ArrayList<String> rangeStrs = new ArrayList<>(Arrays.asList(matcher_ranges.group(1).split(";")));
            if (rangeStrs.size() != voices)
                throw new IllegalArgumentException("Number of unison range is not corresponding to voice number.");
            rangeStrs.forEach(
                    rangeStr ->
                    {
                        Matcher matcher_range = Pattern.compile("([A-G]((bb)|b|#|x)?-?\\d+):([A-G]((bb)|b|#|x)?-?\\d)").matcher(rangeStr);
                        if (matcher_range.find())
                            voiceList.add(new VoiceConfig(Note.parse(matcher_range.group(1)), Note.parse(matcher_range.group(4))));
                    });
        }
        
        if (matcher_unisonPenalties.find())
        {
            ArrayList<String> unisonStrs = new ArrayList<>(Arrays.asList(matcher_unisonPenalties.group(1).split(";")));
            if (unisonStrs.size() != voices - 1)
                throw new IllegalArgumentException("Number of unison penalty is not corresponding to voice number.");
            for (int i = 0; i < unisonStrs.size(); i++)
                voiceList.get(i).setUnisonPenalty(Integer.parseInt(unisonStrs.get(i)));
        }
        
        if (matcher_fixClasses.find())
        {
            ArrayList<String> fixClassStrs = new ArrayList<>();
            if (matcher_fixClasses.group(1) != null)
            {
                fixClassStrs.addAll(Arrays.asList(matcher_fixClasses.group(1).split(";")));
                if (fixClassStrs.stream().anyMatch(s -> s.startsWith("1=")))
                    throw new IllegalArgumentException("Cannot specify fixed class for voice 1.");
            }
            if (int_bass == null)
            {
                if (inv_bass == null)
                    fixClassStrs.add("1=" + noteList.get(0).getNote()._toString(false));
                else
                    fixClassStrs.add("1=" + inv_bass._toString(false));
            }
            else
            {
                if (inv_bass == null)
                    fixClassStrs.add("1=" + int_bass._toString(false));
                else
                {
                    if (fixClassStrs.stream().anyMatch(s -> s.startsWith("2=")))
                        throw new IllegalArgumentException("Specified voice 2 does not match for the chord configuration.");
                    
                    fixClassStrs.add("1=" + int_bass._toString(false));
                    fixClassStrs.add("2=" + inv_bass._toString(false));
                }
            }
            fixClassStrs.forEach(
                    fixClassStr ->
                    {
                        Matcher matcher_fixClass = Pattern.compile("(\\d)=([A-G]((bb)|b|#|x)?)").matcher(fixClassStr);
                        if (matcher_fixClass.find())
                        {
                            int voice = Integer.parseInt(matcher_fixClass.group(1)) - 1;
                            if (voice >= voices)
                                throw new IllegalArgumentException("Specified fixed class must be one of the voice.");
                            
                            Note fixNoteClass = Note.parse(matcher_fixClass.group(2));
                            
                            ChordNoteConfig nc = null;
                            for (ChordNoteConfig cnc : noteList)
                            {
                                if (cnc.getNote().isEnharmonicNoClass(fixNoteClass))
                                {
                                    nc = new ChordNoteConfig(cnc);
                                    break;
                                }
                            }
                            if (voice == 0)
                            {
                                if (nc == null)
                                    nc = new ChordNoteConfig(fixNoteClass, null, null);
                                else
                                {
                                    nc.getPrepareList().clear();
                                    nc.getTendencyList().clear();
                                    nc.getAltTendencyList().clear();
                                    nc.getBonusList().clear();
                                }
                            }
                            
                            if (nc != null)
                                voiceList.get(voice).setFixClassNote(nc);
                            else
                                throw new IllegalArgumentException("Specified fixed class must be a chord tone.");
                        }
                    });
        }
        
        if (matcher_insistNotes.find() && matcher_insistNotes.group(1) != null)
        {
            Arrays.asList(matcher_insistNotes.group(1).split(";")).forEach(
                    insistStr ->
                    {
                        Matcher matcher_insistNote = Pattern.compile("(\\d)=([A-G]((bb)|b|#|x)?(-?\\d+))").matcher(insistStr);
                        if (matcher_insistNote.find())
                        {
                            int voice = Integer.parseInt(matcher_insistNote.group(1)) - 1;
                            if (voice >= voices)
                                throw new IllegalArgumentException("Specified insisted note must be one of the voice.");
                            voiceList.get(voice).setInsistNote(Note.parse(matcher_insistNote.group(2)));
                        }
                    });
        }
        
        return new ChordExp(noteList, voiceList);
    }
}
