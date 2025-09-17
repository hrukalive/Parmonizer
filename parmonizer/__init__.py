"""Parmonizer package providing harmony generation tools."""

from .music import Note, VoiceRange, Chord, build_default_voice_ranges
from .playback import play_progression
from .solver import HarmonyPreferences, HarmonySolver, HarmonySolution

__all__ = [
    "Note",
    "VoiceRange",
    "Chord",
    "build_default_voice_ranges",
    "HarmonyPreferences",
    "HarmonySolver",
    "HarmonySolution",
    "play_progression",
]
