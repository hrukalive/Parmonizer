"""Constraint-based harmony solver for SATB or multi-part writing."""
from __future__ import annotations

from dataclasses import dataclass
import math
from typing import List, Optional, Sequence, Tuple
import collections

from .music import Chord, Note, VoiceRange, build_default_voice_ranges


@dataclass(frozen=True)
class Voicing:
    """Represents the notes assigned to each voice for a chord."""

    notes: Tuple[Note, ...]

    def pitch_classes(self) -> set[int]:
        return {note.pitch_class for note in self.notes}

    def span(self) -> int:
        return self.notes[-1].midi - self.notes[0].midi if self.notes else 0

    def __str__(self) -> str:  # pragma: no cover - trivial
        return " | ".join(note.as_string() for note in self.notes)


@dataclass
class HarmonyPreferences:
    """Weights describing how the solver evaluates solutions."""

    missing_preferred_penalty: float = 1.0
    duplicate_pitch_penalty: float = 0.3
    wide_spacing_threshold: int = 17
    wide_spacing_penalty: float = 0.3
    large_leap_threshold: int = 7
    max_leap: int = 12
    large_leap_weight: float = 0.9
    common_tone_bonus: float = 0.25
    parallel_fifth_penalty: float = 3.0
    parallel_octave_penalty: float = 2.0
    hold_tone_bonus: float = 0.15
    stepwise_weight: float = 0.15

    def voicing_penalty(self, chord: Chord, voicing: Voicing) -> float:
        pcs = voicing.pitch_classes()
        if not chord.required_pitch_classes.issubset(pcs):
            return math.inf

        penalty = 0.0
        for preferred in chord.preferred_pitch_classes:
            if preferred not in pcs:
                penalty += self.missing_preferred_penalty

        counts = collections.Counter(note.pitch_class for note in voicing.notes)
        for count in counts.values():
            if count > 1:
                penalty += (count - 1) * self.duplicate_pitch_penalty

        for low, high in zip(voicing.notes, voicing.notes[1:]):
            interval = high.midi - low.midi
            if interval > self.wide_spacing_threshold:
                penalty += (interval - self.wide_spacing_threshold) * self.wide_spacing_penalty

        return penalty

    def transition_penalty(self, prev: Voicing, curr: Voicing) -> float:
        if len(prev.notes) != len(curr.notes):
            raise ValueError("Voicings must contain the same number of voices.")

        penalty = 0.0
        directions: List[int] = []
        for previous, current in zip(prev.notes, curr.notes):
            diff = current.midi - previous.midi
            abs_diff = abs(diff)
            if abs_diff > self.max_leap:
                return math.inf
            if abs_diff > self.large_leap_threshold:
                penalty += (abs_diff - self.large_leap_threshold) * self.large_leap_weight
            elif abs_diff > 2:
                penalty += (abs_diff - 2) * self.stepwise_weight
            elif abs_diff == 0:
                penalty -= self.hold_tone_bonus
            else:  # small step of one or two semitones
                penalty += 0.5 * self.stepwise_weight
            if abs_diff == 0:
                penalty -= self.common_tone_bonus
            directions.append(0 if diff == 0 else (1 if diff > 0 else -1))

        for i in range(len(prev.notes)):
            for j in range(i + 1, len(prev.notes)):
                interval_prev = abs(prev.notes[j].midi - prev.notes[i].midi) % 12
                interval_curr = abs(curr.notes[j].midi - curr.notes[i].midi) % 12
                same_direction = directions[i] == directions[j] and directions[i] != 0
                if interval_prev == 7 and interval_curr == 7 and same_direction:
                    penalty += self.parallel_fifth_penalty
                if interval_prev == 0 and interval_curr == 0 and same_direction:
                    penalty += self.parallel_octave_penalty

        return max(0.0, penalty)


@dataclass
class HarmonySolution:
    """Represents a solved harmony progression."""

    cost: float
    voicings: List[Voicing]
    chords: Sequence[Chord]
    voices: Sequence[VoiceRange]

    def format(self) -> str:
        header = " | ".join(chord.symbol for chord in self.chords)
        lines = [f"Solution cost: {self.cost:.2f}", f"Chords: {header}"]
        for voice_index, voice in enumerate(self.voices):
            note_names = [voicing.notes[voice_index].as_string() for voicing in self.voicings]
            lines.append(f"{voice.name:>10}: {'  '.join(note_names)}")
        return "\n".join(lines)


@dataclass
class _PartialSolution:
    voicings: List[Voicing]
    cost: float

    @property
    def last_voicing(self) -> Voicing:
        return self.voicings[-1]


class HarmonySolver:
    """Generates harmonisations for a chord progression."""

    def __init__(
        self,
        voices: Optional[Sequence[VoiceRange]] = None,
        preferences: Optional[HarmonyPreferences] = None,
        max_adjacent_spacing: int = 14,
        max_voicings_per_chord: int = 250,
        beam_width: int = 80,
    ) -> None:
        self.voices = list(voices) if voices is not None else build_default_voice_ranges(4)
        self.preferences = preferences or HarmonyPreferences()
        self.max_adjacent_spacing = max_adjacent_spacing
        self.max_voicings_per_chord = max_voicings_per_chord
        self.beam_width = beam_width

    def solve(self, chords: Sequence[Chord], max_results: int = 3) -> List[HarmonySolution]:
        if not chords:
            raise ValueError("Chord progression cannot be empty.")
        if any(len(self.voices) == 0 for _ in chords):
            raise ValueError("No voices configured for solver.")

        voicing_space = [self._generate_voicings(chord) for chord in chords]
        for chord, space in zip(chords, voicing_space):
            if not space:
                raise ValueError(f"No valid voicings found for chord '{chord.symbol}'.")

        partials: List[_PartialSolution] = [
            _PartialSolution([voicing], base_penalty)
            for voicing, base_penalty in voicing_space[0]
        ]
        partials.sort(key=lambda sol: sol.cost)
        partials = partials[: self.beam_width]

        for chord_index in range(1, len(chords)):
            candidates: List[_PartialSolution] = []
            for solution in partials:
                prev_voicing = solution.last_voicing
                for voicing, base_penalty in voicing_space[chord_index]:
                    transition = self.preferences.transition_penalty(prev_voicing, voicing)
                    if math.isinf(transition):
                        continue
                    total_cost = solution.cost + transition + base_penalty
                    candidates.append(_PartialSolution(solution.voicings + [voicing], total_cost))
            if not candidates:
                return []
            candidates.sort(key=lambda sol: sol.cost)
            partials = candidates[: self.beam_width]

        solutions = [
            HarmonySolution(cost=sol.cost, voicings=sol.voicings, chords=chords, voices=self.voices)
            for sol in partials[:max_results]
        ]
        return solutions

    def _generate_voicings(self, chord: Chord) -> List[Tuple[Voicing, float]]:
        options: List[List[Note]] = []
        for index, voice in enumerate(self.voices):
            require_pitch_class = chord.bass_pitch_class if index == 0 and chord.bass_pitch_class is not None else None
            options.append(self._notes_for_voice(chord, voice, require_pitch_class))
            if not options[-1]:
                return []

        generated: List[Tuple[Voicing, float]] = []
        stack: List[Note] = []

        def backtrack(voice_index: int) -> None:
            if voice_index == len(self.voices):
                voicing = Voicing(tuple(stack))
                penalty = self.preferences.voicing_penalty(chord, voicing)
                if not math.isinf(penalty):
                    generated.append((voicing, penalty))
                return

            previous_note = stack[-1] if stack else None
            for note in options[voice_index]:
                if previous_note and note.midi < previous_note.midi:
                    continue
                if previous_note and note.midi - previous_note.midi > self.max_adjacent_spacing:
                    continue
                stack.append(note)
                backtrack(voice_index + 1)
                stack.pop()

        backtrack(0)
        generated.sort(key=lambda item: item[1])
        return generated[: self.max_voicings_per_chord]

    def _notes_for_voice(
        self,
        chord: Chord,
        voice: VoiceRange,
        require_pitch_class: Optional[int] = None,
    ) -> List[Note]:
        notes: List[Note] = []
        seen = set()
        for interval in chord.intervals:
            base = chord.root.midi + interval
            while base > voice.high.midi:
                base -= 12
            while base < voice.low.midi:
                base += 12
            while base - 12 >= voice.low.midi:
                base -= 12
            pitch = base
            while voice.low.midi <= pitch <= voice.high.midi:
                if require_pitch_class is None or pitch % 12 == require_pitch_class:
                    if pitch not in seen:
                        notes.append(Note.from_midi(pitch))
                        seen.add(pitch)
                pitch += 12
        notes.sort(key=lambda note: note.midi)
        return notes


__all__ = [
    "HarmonyPreferences",
    "HarmonySolver",
    "HarmonySolution",
    "Voicing",
]
