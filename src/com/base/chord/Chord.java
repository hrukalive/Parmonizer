package com.base.chord;

import com.base.Triple;
import com.base.Tuple;
import com.base.interval.Interval;
import com.base.Note;
import com.base.realization.ChordVoicing;
import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Implemented the concept of a chord, with the functionality
 * to generate all possible configurations under certain restraints.
 * <p>
 * Created by NyLP on 6/12/17.
 */

public class Chord {
    public static class Builder {
        private Note generator;
        private ChordType type;
        private ChordNote root;
        private ChordNote bass = null;
        private int inversion = 0;
        private ArrayList<ChordNote> notes = new ArrayList<>();
        private Note tonic = null;

        public Builder(Note note) {
            this.generator = note;
            notes.add(new ChordNote(note));
            this.root = notes.get(notes.size() - 1);
        }

        public Builder setRoot(Note root) {
            ChordNote tmp = null;
            for (ChordNote note : notes) {
                if (note.equalsNoClass(root)) {
                    tmp = note;
                    break;
                }
            }
            if (tmp == null)
                throw new IllegalArgumentException("Specified root note is not within the chord");
            else
                this.root = tmp;
            return this;
        }

        public Builder setTonic(Note tonic) {
            this.tonic = tonic;
            return this;
        }

        public Builder setType(ChordType type) {
            this.type = type;
            return this;
        }

        public Builder setInversion(int inv) {
            return setBass(notes.get(inv));
        }

        public Builder setBass(Note bass) {
            if (bass == null)
                return this;
            for (int i = 0; i < notes.size(); i++) {
                if (notes.get(i).equalsNoClass(bass)) {
                    inversion = i;
                    this.bass = null;
                    return this;
                }
            }
            this.inversion = 0;
            this.bass = new ChordNote(bass);
            this.bass.setNotOmittable();
            this.bass.setNotRepeatable();
            return this;
        }

        public Builder stackA(Interval interval) {
            return stackA(interval, false);
        }

        public Builder stackA(Interval interval, boolean isRoot) {
            notes.add(new ChordNote(notes.get(notes.size() - 1).interval(interval)));
            if (isRoot)
                root = notes.get(notes.size() - 1);
            return this;
        }

        public Builder stackFromGenerator(Interval interval) {
            return stackFromGenerator(interval, false);
        }

        public Builder stackFromGenerator(Interval interval, boolean isRoot) {
            notes.add(new ChordNote(generator.interval(interval)));
            if (isRoot)
                root = notes.get(notes.size() - 1);
            return this;
        }

        public Builder addNote(Note note) {
            return addNote(note, false);
        }

        public Builder addNote(Note note, boolean isRoot) {
            notes.add(new ChordNote(note));
            if (isRoot)
                root = notes.get(notes.size() - 1);
            return this;
        }

        public Builder notOmittable() {
            return notOmittable(notes.size() - 1);
        }

        public Builder omittable(int penalty) {
            return omittable(notes.size() - 1, penalty);
        }

        public Builder unrepeatable() {
            return unrepeatable(notes.size() - 1);
        }

        public Builder repeatable(int penalty) {
            return repeatable(notes.size() - 1, penalty);
        }

        public Builder tendency(Interval interval) {
            return tendency(notes.size() - 1, interval);
        }

        public Builder altTendency(Interval interval) {
            return altTendency(notes.size() - 1, interval);
        }

        public Builder preparedBy(Interval interval) {
            return preparedBy(notes.size() - 1, interval);
        }

        public Builder bonus(Interval interval, int bonus) {
            return bonus(notes.size() - 1, interval, bonus);
        }

        public Builder notOmittable(int index) {
            notes.get(index).setNotOmittable();
            return this;
        }

        public Builder omittable(int index, int penalty) {
            notes.get(index).setOmittable(penalty);
            return this;
        }

        public Builder unrepeatable(int index) {
            notes.get(index).setNotRepeatable();
            return this;
        }

        public Builder repeatable(int index, int penalty) {
            notes.get(index).setRepeatable(penalty);
            return this;
        }

        public Builder tendency(int index, Interval interval) {
            notes.get(index).addTendency(interval);
            return this;
        }

        public Builder altTendency(int index, Interval interval) {

            notes.get(index).addAltTendency(interval);
            return this;
        }

        public Builder preparedBy(int index, Interval interval) {

            notes.get(index).addPreparation(interval);
            return this;
        }

        public Builder bonus(int index, Interval interval, int bonus) {
            notes.get(index).addBonus(interval, bonus);
            return this;
        }

        public int getInversion() {
            return inversion;
        }

        public int getNoteNumber() {
            return notes.size();
        }

        public ChordType getType() {
            return type;
        }

        public Builder negativeBuilder() {
            return negativeBuilder(Interval.P5);
        }

        public Builder negativeBuilder(Interval generatorInterval) {
            if (tonic == null) {
                throw new IllegalArgumentException("Tonic is not defined.");
            }
            Note gen = tonic.interval(generatorInterval).interval(tonic.interval(root).reverse());
            Builder builder = new Builder(gen);
            for (int i = 1; i < notes.size(); i++) {
                ChordNote note = notes.get(i);
                builder.stackFromGenerator(generator.interval(note).reverse());
                if (note.isOmittable())
                    builder.omittable(note.getOmitPenalty());
                else
                    builder.notOmittable();
                if (note.isRepeatable())
                    builder.repeatable(note.getRepeatPenalty());
                else
                    builder.unrepeatable();
                for (Interval intv : note.getTendencyList()) {
                    builder.tendency(intv.reverse());
                }
                for (Interval intv : note.getAltTendencyList()) {
                    builder.altTendency(intv.reverse());
                }
                for (Interval intv : note.getPrepareList()) {
                    builder.preparedBy(intv.reverse());
                }
                for (Tuple<Interval, Integer> tuple : note.getBonusList()) {
                    builder.bonus(tuple.getFirst().reverse(), tuple.getSecond());
                }
            }
            int deg = generatorInterval.degree();
            int semitone = generatorInterval.semitones();
            if (builder.notes.contains(gen.interval(generatorInterval.reverse())))
                builder.setRoot(gen.interval(generatorInterval.reverse()));
            else if (builder.notes.contains(gen.interval(Interval.parse(deg, semitone - 1).reverse())))
                builder.setRoot(gen.interval(Interval.parse(deg, semitone - 1).reverse()));
            else if (builder.notes.contains(gen.interval(Interval.parse(deg, semitone + 1).reverse())))
                builder.setRoot(gen.interval(Interval.parse(deg, semitone + 1).reverse()));
            else
                throw new InternalException("Cannot set root for negative harmony.");
            if (this.bass != null)
                builder.setBass(gen.interval(this.root.interval(this.bass).reverse()));
            else
                builder.setInversion(this.inversion);
            return builder;
        }

        public Chord build() {
            Collections.sort(notes);
            for (ChordNote note : notes) {
                if (!note.equals(root)) {
                    while (note.compareTo(root) < 0)
                        note.octaveUp();
                }
            }
            Collections.sort(notes);
            int oct = -root.getOctave();
            for (ChordNote note : notes)
                note.octave(oct);
            root.setNotOmittable();
            return new Chord(this);
        }
    }


    private static final Map<String, ChordType> chordTypes = new HashMap<>();

    static {
        for (ChordType type : ChordType.values()) {
            chordTypes.put(type.getName(), type);
        }
    }

    public static Chord.Builder parse(String chordSymbol) {
        return parse(chordSymbol, null, null);
    }

    public static Chord.Builder parse(String chordSymbol, Note tonic) {
        return parse(chordSymbol, tonic, null);
    }

    public static Chord.Builder parse(String chordSymbol, Note tonic, Interval intv) {
        Pattern pattern_note = Pattern.compile("(-?)([A-G](bb|b|#|x)?)");
        Pattern pattern_quality = Pattern.compile("(5|(69|6|add6|add13|2|add2|add9|maj(7#5|7b6|7|9|13|#4|#11)?)|(min(7b5|7b6|7|Maj7|6|9|11|13)?)|(dim7|dim|aug)|(7b9|7#9|7#4|7#11|7sus4|7alt|7|9|13|11sus)|(sus4|sus2|sus\\(b9\\)))");
        Matcher matcher_note = pattern_note.matcher(chordSymbol);
        Matcher matcher_quality = pattern_quality.matcher(chordSymbol);

        if (matcher_note.find() && matcher_quality.find() && matcher_quality.groupCount() >= 3) {
            boolean isNegative = matcher_note.group(1).equals("-");
            Note generator = Note.parse(matcher_note.group(2));
            Note bass = null;
            if (matcher_note.find())
                bass = Note.parse(matcher_note.group(2));
            String quality = matcher_quality.group(1);
            if (chordTypes.containsKey(quality)) {
                ChordType type = chordTypes.get(quality);
                final Chord.Builder builder = new Chord.Builder(generator).setType(type).setTonic(tonic);
                for (Interval interval : type.getIntervals())
                    builder.stackFromGenerator(interval);
                for (int i = 0; i < type.getOmitPenalties().size(); i++) {
                    if (type.getOmitPenalties().get(i) < 0)
                        builder.notOmittable(i + 1);
                    else
                        builder.omittable(i + 1, type.getOmitPenalties().get(i));
                }
                for (int i = 0; i < type.getRepeatPenalties().size(); i++) {
                    if (type.getRepeatPenalties().get(i) < 0)
                        builder.unrepeatable(i);
                    else
                        builder.repeatable(i, type.getRepeatPenalties().get(i));
                }
                builder.setBass(bass);
                DiatonicChordProperty.apply(builder, intv);
                return isNegative ? builder.negativeBuilder() : builder;
            } else {
                throw new IllegalArgumentException("Chord type not recognized.");
            }
        } else {
            throw new IllegalArgumentException("Unrecognized expression.");
        }
    }

    private ChordType type;
    private ArrayList<ChordNote> noteList;
    private int inversion = 0;
    private ChordNote bass = null;

    private Chord(Builder builder) {
        this(builder.notes, builder.inversion, builder.bass, builder.type);
    }

    private Chord(ArrayList<ChordNote> noteList, ChordType type) {
        this(noteList, 0, type);
    }

    private Chord(ArrayList<ChordNote> noteList, int inversion, ChordType type) {
        this(noteList, inversion, null, type);
    }

    private Chord(ArrayList<ChordNote> noteList, int inversion, ChordNote bass, ChordType type) {
        if (inversion < 0 || inversion >= noteList.size())
            throw new IllegalArgumentException("Inversion is impossible.");
        for (Note note : noteList) {
            if (note.equalsNoClass(bass)) {
                throw new IllegalArgumentException("Please use inversion.");
            }
        }
        this.noteList = new ArrayList<>(noteList);
        this.inversion = inversion;
        this.bass = bass;
        this.type = type;
    }

    public Chord(Chord chord) {
        this(chord.noteList, chord.inversion, chord.bass, chord.type);
    }

    public ArrayList<ChordNote> getChordNotes() {
        return noteList;
    }

    public ArrayList<Note> getNoteSet() {
        return (ArrayList<Note>) noteList.stream().map(ChordNote::getNote).collect(Collectors.toList());
    }

    public void setInversion(int inversion) {
        if (inversion < 0 || inversion >= noteList.size())
            throw new IllegalArgumentException("Inversion specified is impossible.");
        this.inversion = inversion;
    }

    public int getInversion() {
        return inversion;
    }

    public Note getRoot() {
        return noteList.get(0);
    }

    public boolean isBassInChord() {
        return bass == null;
    }

    public ChordNote getBass() {
        return bass == null ? noteList.get(inversion) : bass;
    }

    @Override
    public String toString() {
        return noteList.toString();
    }
}
