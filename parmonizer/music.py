"""Core musical primitives used by the Parmonizer solver."""
from __future__ import annotations

from dataclasses import dataclass
import math
import re
from typing import Dict, List, Optional, Sequence, Set


_SHARP_NAMES = [
    "C",
    "C#",
    "D",
    "D#",
    "E",
    "F",
    "F#",
    "G",
    "G#",
    "A",
    "A#",
    "B",
]
_FLAT_NAMES = [
    "C",
    "Db",
    "D",
    "Eb",
    "E",
    "F",
    "Gb",
    "G",
    "Ab",
    "A",
    "Bb",
    "B",
]

_NOTE_BASES = {
    "C": 0,
    "D": 2,
    "E": 4,
    "F": 5,
    "G": 7,
    "A": 9,
    "B": 11,
}

_ACCIDENTAL_VALUES = {
    "": 0,
    "#": 1,
    "♯": 1,
    "##": 2,
    "x": 2,
    "𝄪": 2,
    "b": -1,
    "♭": -1,
    "bb": -2,
    "𝄫": -2,
}

_NOTE_REGEX = re.compile(r"^([A-Ga-g])([#bx♯♭𝄪𝄫]{0,2})(-?\d+)?$")


@dataclass(frozen=True)
class Note:
    """Represents a pitch as a MIDI number."""

    midi: int

    def __post_init__(self) -> None:
        if not 0 <= self.midi <= 127:
            raise ValueError(f"MIDI pitch {self.midi} is outside of the supported range (0-127).")

    @classmethod
    def from_midi(cls, midi: int) -> "Note":
        return cls(int(midi))

    @classmethod
    def from_string(cls, token: str, default_octave: int = 4) -> "Note":
        token = token.strip()
        match = _NOTE_REGEX.match(token)
        if not match:
            raise ValueError(f"Could not parse note name '{token}'.")
        letter = match.group(1).upper()
        accidental = match.group(2) or ""
        octave_part = match.group(3)

        accidental = accidental.replace("♯", "#").replace("♭", "b").replace("𝄪", "##").replace("𝄫", "bb")
        if accidental.lower() == "x":
            accidental = "##"
        if accidental not in _ACCIDENTAL_VALUES:
            raise ValueError(f"Unsupported accidental pattern '{accidental}' in note '{token}'.")

        semitone = _NOTE_BASES[letter] + _ACCIDENTAL_VALUES[accidental]
        octave = int(octave_part) if octave_part is not None else default_octave
        midi = 12 * (octave + 1) + semitone
        return cls(midi)

    @property
    def pitch_class(self) -> int:
        return self.midi % 12

    @property
    def octave(self) -> int:
        return self.midi // 12 - 1

    def transpose(self, semitones: int) -> "Note":
        return Note.from_midi(self.midi + semitones)

    def distance_to(self, other: "Note") -> int:
        return other.midi - self.midi

    def frequency(self, a4: float = 440.0) -> float:
        """Return the fundamental frequency for the note in hertz."""

        return float(a4 * (2 ** ((self.midi - 69) / 12)))

    def as_string(self, prefer_sharps: bool = False) -> str:
        names = _SHARP_NAMES if prefer_sharps else _FLAT_NAMES
        return f"{names[self.pitch_class]}{self.octave}"

    def __str__(self) -> str:  # pragma: no cover - trivial
        return self.as_string()


@dataclass(frozen=True)
class VoiceRange:
    """Represents the allowed range for a single voice/part."""

    name: str
    low: Note
    high: Note

    def contains(self, note: Note) -> bool:
        return self.low.midi <= note.midi <= self.high.midi


@dataclass(frozen=True)
class ChordQuality:
    """Represents interval content and requirements for a chord quality."""

    intervals: Sequence[int]
    required: Set[int]
    preferred: Set[int]


class Chord:
    """Parsed chord symbol that exposes pitch-class information."""

    def __init__(self, symbol: str, root: Note, quality_key: str, quality: ChordQuality, bass: Optional[Note] = None):
        self.symbol = symbol
        self.root = root
        self.quality_key = quality_key
        self.intervals = list(quality.intervals)
        self.required_intervals = set(quality.required)
        self.preferred_intervals = set(quality.preferred)
        self.bass = bass

        self.pitch_classes = { (self.root.pitch_class + interval) % 12 for interval in self.intervals }
        self.required_pitch_classes = { (self.root.pitch_class + interval) % 12 for interval in self.required_intervals }
        self.preferred_pitch_classes = { (self.root.pitch_class + interval) % 12 for interval in self.preferred_intervals }
        self.bass_pitch_class = self.bass.pitch_class if self.bass else None

    def __str__(self) -> str:  # pragma: no cover - trivial
        return self.symbol


_QUALITY_LIBRARY: Dict[str, ChordQuality] = {
    "maj": ChordQuality(intervals=[0, 4, 7], required={0, 4}, preferred={7}),
    "min": ChordQuality(intervals=[0, 3, 7], required={0, 3}, preferred={7}),
    "aug": ChordQuality(intervals=[0, 4, 8], required={0, 4}, preferred={8}),
    "dim": ChordQuality(intervals=[0, 3, 6], required={0, 3, 6}, preferred=set()),
    "dom7": ChordQuality(intervals=[0, 4, 7, 10], required={0, 4}, preferred={7, 10}),
    "maj7": ChordQuality(intervals=[0, 4, 7, 11], required={0, 4}, preferred={7, 11}),
    "min7": ChordQuality(intervals=[0, 3, 7, 10], required={0, 3}, preferred={7, 10}),
    "minmaj7": ChordQuality(intervals=[0, 3, 7, 11], required={0, 3}, preferred={7, 11}),
    "dim7": ChordQuality(intervals=[0, 3, 6, 9], required={0, 3, 6}, preferred={9}),
    "m7b5": ChordQuality(intervals=[0, 3, 6, 10], required={0, 3, 6}, preferred={10}),
    "6": ChordQuality(intervals=[0, 4, 7, 9], required={0, 4}, preferred={7, 9}),
    "m6": ChordQuality(intervals=[0, 3, 7, 9], required={0, 3}, preferred={7, 9}),
    "sus4": ChordQuality(intervals=[0, 5, 7], required={0, 5}, preferred={7}),
    "sus2": ChordQuality(intervals=[0, 2, 7], required={0, 2}, preferred={7}),
    "add9": ChordQuality(intervals=[0, 4, 7, 14], required={0, 4}, preferred={7, 14}),
    "madd9": ChordQuality(intervals=[0, 3, 7, 14], required={0, 3}, preferred={7, 14}),
    "maj9": ChordQuality(intervals=[0, 4, 7, 11, 14], required={0, 4}, preferred={7, 11, 14}),
    "min9": ChordQuality(intervals=[0, 3, 7, 10, 14], required={0, 3}, preferred={7, 10, 14}),
    "dom9": ChordQuality(intervals=[0, 4, 7, 10, 14], required={0, 4}, preferred={7, 10, 14}),
    "11": ChordQuality(intervals=[0, 4, 7, 10, 14, 17], required={0, 4}, preferred={7, 10, 14, 17}),
    "13": ChordQuality(intervals=[0, 4, 7, 10, 14, 17, 21], required={0, 4}, preferred={7, 10, 14, 17, 21}),
}

_QUALITY_ALIASES = {
    "": "maj",
    " ": "maj",
    "maj": "maj",
    "major": "maj",
    "m": "min",
    "min": "min",
    "minor": "min",
    "-": "min",
    "aug": "aug",
    "+": "aug",
    "dim": "dim",
    "o": "dim",
    "°": "dim",
    "7": "dom7",
    "dom7": "dom7",
    "dom": "dom7",
    "maj7": "maj7",
    "m7": "min7",
    "min7": "min7",
    "m9": "min9",
    "maj9": "maj9",
    "9": "dom9",
    "11": "11",
    "13": "13",
    "m7b5": "m7b5",
    "ø": "m7b5",
    "ø7": "m7b5",
    "halfdim": "m7b5",
    "halfdim7": "m7b5",
    "dim7": "dim7",
    "o7": "dim7",
    "m6": "m6",
    "min6": "m6",
    "maj6": "6",
    "6": "6",
    "sus": "sus4",
    "sus4": "sus4",
    "sus2": "sus2",
    "add9": "add9",
    "madd9": "madd9",
    "minmaj7": "minmaj7",
}


def _canonical_quality(quality: str) -> str:
    simplified = quality.strip()
    simplified = simplified.replace("Δ", "maj").replace("–", "-").replace("♭", "b").replace("♯", "#")
    simplified = simplified.lower()
    if simplified in _QUALITY_ALIASES:
        simplified = _QUALITY_ALIASES[simplified]
    if simplified not in _QUALITY_LIBRARY:
        raise ValueError(f"Unsupported chord quality '{quality}'.")
    return simplified


def parse_chord_symbol(symbol: str) -> Chord:
    symbol = symbol.strip()
    if not symbol:
        raise ValueError("Chord symbol cannot be empty.")
    match = re.match(r"^([A-Ga-g])([#bx♯♭𝄪𝄫]{0,2})([^/]*)?(?:/([A-Ga-g][#bx♯♭𝄪𝄫]{0,2}))?$", symbol)
    if not match:
        raise ValueError(f"Could not parse chord symbol '{symbol}'.")
    root_letter = match.group(1)
    accidental = match.group(2) or ""
    quality_part = match.group(3) or ""
    bass_part = match.group(4)

    root = Note.from_string(root_letter + accidental, default_octave=4)
    quality_key = _canonical_quality(quality_part)
    quality = _QUALITY_LIBRARY[quality_key]
    bass = Note.from_string(bass_part, default_octave=3) if bass_part else None
    return Chord(symbol=symbol, root=root, quality_key=quality_key, quality=quality, bass=bass)


def build_default_voice_ranges(voices: int) -> List[VoiceRange]:
    if voices <= 0:
        raise ValueError("Number of voices must be positive.")
    if voices == 4:
        return [
            VoiceRange("Bass", Note.from_string("E2"), Note.from_string("C4")),
            VoiceRange("Tenor", Note.from_string("C3"), Note.from_string("G4")),
            VoiceRange("Alto", Note.from_string("G3"), Note.from_string("D5")),
            VoiceRange("Soprano", Note.from_string("C4"), Note.from_string("G5")),
        ]
    # For other voice counts, build overlapping ranges spanning C2-C6.
    low = Note.from_string("C2").midi
    high = Note.from_string("C6").midi
    span = high - low
    step = max(6, span // voices)
    ranges: List[VoiceRange] = []
    voice_labels = [
        "Bass",
        "Baritone",
        "Tenor",
        "Countertenor",
        "Alto",
        "Mezzo",
        "Soprano",
        "High Voice",
    ]
    for index in range(voices):
        start = low + index * step
        end = start + step + 6
        if index == voices - 1:
            end = high
        label = voice_labels[index] if index < len(voice_labels) else f"Voice {index + 1}"
        ranges.append(VoiceRange(label, Note.from_midi(max(low, start)), Note.from_midi(min(high, end))))
    return ranges


__all__ = [
    "Note",
    "VoiceRange",
    "Chord",
    "ChordQuality",
    "build_default_voice_ranges",
    "parse_chord_symbol",
]
