"""Audio playback helpers for Parmonizer harmonies."""
from __future__ import annotations

import math
from array import array
from dataclasses import dataclass
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


@dataclass(frozen=True)
class SynthSettings:
    """Configuration for the simple built-in synthesiser used during playback."""

    waveform: str = "sine"
    cutoff_hz: float = 4_800.0
    resonance: float = 0.707
    attack: float = 0.015
    decay: float = 0.2
    sustain_level: float = 0.8
    release: float = 0.25


def play_progression(
    solution: HarmonySolution,
    hold_times: Sequence[float],
    settings: SynthSettings | None = None,
) -> None:
    """Play a solved harmony progression using simpleaudio."""

    if _simpleaudio is None:
        raise RuntimeError(
            "Audio playback requires the 'simpleaudio' package. Install it with 'pip install simpleaudio'."
        )

    if len(hold_times) != len(solution.voicings):
        raise ValueError("Hold time count must match the number of chords in the solution.")

    config = _normalise_settings(settings)

    frames = array("h")
    for index, (voicing, duration) in enumerate(zip(solution.voicings, hold_times)):
        if duration <= 0:
            raise ValueError(f"Hold times must be positive. Invalid value for chord {index + 1}.")
        frames.extend(_render_voicing(voicing, duration, config))
        if index < len(solution.voicings) - 1:
            frames.extend(_silence(int(_SAMPLE_RATE * _REST_DURATION)))

    if not frames:
        return

    play_obj = _simpleaudio.play_buffer(frames.tobytes(), 1, 2, _SAMPLE_RATE)
    play_obj.wait_done()


def _render_voicing(voicing: Voicing, duration: float, settings: SynthSettings) -> array:
    sample_count = max(1, int(duration * _SAMPLE_RATE))
    amplitude = _BASE_AMPLITUDE / max(1, len(voicing.notes))
    oscillator = _OSCILLATORS[settings.waveform]

    mix = [0.0] * sample_count
    for note in voicing.notes:
        increment = note.frequency() / _SAMPLE_RATE
        phase = 0.0
        for idx in range(sample_count):
            mix[idx] += oscillator(phase)
            phase += increment
            if phase >= 1.0:
                phase -= math.floor(phase)

    envelope = _adsr_envelope(sample_count, duration, settings)
    for idx in range(sample_count):
        mix[idx] = amplitude * mix[idx] * envelope[idx]

    filtered = _low_pass_filter(mix, settings.cutoff_hz, settings.resonance)
    samples = array("h", [0] * sample_count)
    for idx, value in enumerate(filtered):
        samples[idx] = int(round(value))

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


def _normalise_settings(settings: SynthSettings | None) -> SynthSettings:
    base = SynthSettings()
    if settings is None:
        return base

    waveform = (settings.waveform or base.waveform).strip().lower()
    if waveform not in _OSCILLATORS:
        waveform = base.waveform

    cutoff = _coerce_float(settings.cutoff_hz, base.cutoff_hz)
    cutoff = max(40.0, min(cutoff, (_SAMPLE_RATE / 2) - 200.0))

    resonance = _coerce_float(settings.resonance, base.resonance)
    resonance = max(0.1, min(resonance, 25.0))

    attack = max(0.0, _coerce_float(settings.attack, base.attack))
    decay = max(0.0, _coerce_float(settings.decay, base.decay))
    release = max(0.0, _coerce_float(settings.release, base.release))
    sustain_level = _coerce_float(settings.sustain_level, base.sustain_level)
    sustain_level = min(max(sustain_level, 0.0), 1.0)

    return SynthSettings(
        waveform=waveform,
        cutoff_hz=cutoff,
        resonance=resonance,
        attack=attack,
        decay=decay,
        sustain_level=sustain_level,
        release=release,
    )


def _coerce_float(value: float, default: float) -> float:
    try:
        result = float(value)
    except (TypeError, ValueError):  # pragma: no cover - guard against unexpected input
        return default
    if not math.isfinite(result):
        return default
    return result


def _adsr_envelope(sample_count: int, duration: float, settings: SynthSettings) -> list[float]:
    if sample_count <= 0:
        return [0.0]

    attack = max(settings.attack, 0.0)
    decay = max(settings.decay, 0.0)
    release = max(settings.release, 0.0)
    sustain_level = settings.sustain_level

    total = attack + decay + release
    if total > duration and total > 0:
        scale = duration / total
        attack *= scale
        decay *= scale
        release *= scale

    sustain_time = max(duration - (attack + decay + release), 0.0)
    release_start = duration - release

    envelope: list[float] = [0.0] * sample_count
    for index in range(sample_count):
        t = index / _SAMPLE_RATE
        if attack > 0 and t < attack:
            envelope[index] = min(t / attack, 1.0)
            continue
        if attack <= 0 and index == 0:
            envelope[index] = 1.0
            continue

        if t < attack:
            envelope[index] = 1.0
            continue

        if decay > 0 and t < attack + decay:
            progress = (t - attack) / decay
            envelope[index] = 1.0 + (sustain_level - 1.0) * progress
            continue

        if t < attack + decay + sustain_time:
            envelope[index] = sustain_level
            continue

        if release > 0:
            progress = (t - release_start) / release
            envelope[index] = max(sustain_level * (1.0 - progress), 0.0)
        else:
            envelope[index] = 0.0

    if envelope:
        envelope[-1] = 0.0

    return envelope


def _low_pass_filter(samples: list[float], cutoff: float, resonance: float) -> list[float]:
    nyquist = _SAMPLE_RATE / 2
    if cutoff >= nyquist - 10.0:
        return samples

    omega = 2.0 * math.pi * cutoff / _SAMPLE_RATE
    sin_omega = math.sin(omega)
    cos_omega = math.cos(omega)
    alpha = sin_omega / (2.0 * resonance)

    b0 = (1.0 - cos_omega) / 2.0
    b1 = 1.0 - cos_omega
    b2 = (1.0 - cos_omega) / 2.0
    a0 = 1.0 + alpha
    a1 = -2.0 * cos_omega
    a2 = 1.0 - alpha

    b0 /= a0
    b1 /= a0
    b2 /= a0
    a1 /= a0
    a2 /= a0

    output: list[float] = []
    x1 = x2 = y1 = y2 = 0.0
    for x in samples:
        y = b0 * x + b1 * x1 + b2 * x2 - a1 * y1 - a2 * y2
        output.append(y)
        x2 = x1
        x1 = x
        y2 = y1
        y1 = y
    return output


def _sine_wave(phase: float) -> float:
    return math.sin(2.0 * math.pi * phase)


def _triangle_wave(phase: float) -> float:
    return 4.0 * abs((phase % 1.0) - 0.5) - 1.0


def _saw_wave(phase: float) -> float:
    return (2.0 * (phase % 1.0)) - 1.0


def _rectangle_wave(phase: float) -> float:
    return 1.0 if (phase % 1.0) < 0.5 else -1.0


_OSCILLATORS = {
    "sine": _sine_wave,
    "triangle": _triangle_wave,
    "saw": _saw_wave,
    "rectangle": _rectangle_wave,
}


__all__ = ["play_progression", "SynthSettings"]

