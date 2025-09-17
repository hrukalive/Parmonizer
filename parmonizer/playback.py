"""Audio playback helpers for Parmonizer harmonies."""
from __future__ import annotations

import math
from array import array
from typing import Sequence

from .solver import HarmonySolution, Voicing

try:  # pragma: no cover - optional dependency detection
    import simpleaudio as _simpleaudio
except Exception:  # noqa: BLE001 - propagate friendly runtime error later
    _simpleaudio = None


_SAMPLE_RATE = 44_100
_BASE_AMPLITUDE = 28_000
_FADE_DURATION = 0.02
_REST_DURATION = 0.015


def play_progression(solution: HarmonySolution, hold_times: Sequence[float]) -> None:
    """Play a solved harmony progression using simpleaudio."""

    if _simpleaudio is None:
        raise RuntimeError(
            "Audio playback requires the 'simpleaudio' package. Install it with 'pip install simpleaudio'."
        )

    if len(hold_times) != len(solution.voicings):
        raise ValueError("Hold time count must match the number of chords in the solution.")

    frames = array("h")
    for index, (voicing, duration) in enumerate(zip(solution.voicings, hold_times)):
        if duration <= 0:
            raise ValueError(f"Hold times must be positive. Invalid value for chord {index + 1}.")
        frames.extend(_render_voicing(voicing, duration))
        if index < len(solution.voicings) - 1:
            frames.extend(_silence(int(_SAMPLE_RATE * _REST_DURATION)))

    if not frames:
        return

    play_obj = _simpleaudio.play_buffer(frames.tobytes(), 1, 2, _SAMPLE_RATE)
    play_obj.wait_done()


def _render_voicing(voicing: Voicing, duration: float) -> array:
    sample_count = max(1, int(duration * _SAMPLE_RATE))
    samples = array("h", [0] * sample_count)
    amplitude = int(_BASE_AMPLITUDE / max(1, len(voicing.notes)))

    for note in voicing.notes:
        increment = 2.0 * math.pi * note.frequency() / _SAMPLE_RATE
        phase = 0.0
        for idx in range(sample_count):
            samples[idx] += int(round(amplitude * math.sin(phase)))
            phase += increment

    _apply_fade(samples)
    _clamp(samples)
    return samples


def _apply_fade(samples: array) -> None:
    fade_samples = min(int(_FADE_DURATION * _SAMPLE_RATE), len(samples) // 2)
    if fade_samples <= 0:
        return
    for i in range(fade_samples):
        fade_in = (i + 1) / fade_samples
        fade_out = (fade_samples - i) / fade_samples
        samples[i] = int(round(samples[i] * fade_in))
        samples[-1 - i] = int(round(samples[-1 - i] * fade_out))


def _clamp(samples: array) -> None:
    limit = 32_000
    for i, value in enumerate(samples):
        if value > limit:
            samples[i] = limit
        elif value < -limit:
            samples[i] = -limit


def _silence(length: int) -> array:
    if length <= 0:
        return array("h")
    return array("h", [0] * length)


__all__ = ["play_progression"]

