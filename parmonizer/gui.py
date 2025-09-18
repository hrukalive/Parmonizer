"""Tkinter based interface for the Parmonizer harmony solver."""
from __future__ import annotations

import math
import tkinter as tk
from tkinter import messagebox, ttk
import re
from typing import List, Sequence

from .music import build_default_voice_ranges, parse_chord_symbol
from .playback import SynthSettings, play_progression
from .solver import HarmonyPreferences, HarmonySolution, HarmonySolver


_INPUT_INSTRUCTIONS = (
    "Enter chord symbols separated by spaces or newlines.\n"
    "Optional directives: voices=<number>.\n"
    "Example: voices=4 Cmaj7 Fmaj7 G7 Cmaj9"
)


class HarmonyApp(tk.Tk):
    """Simple text-based GUI for entering progressions and viewing solutions."""

    def __init__(self) -> None:
        super().__init__()
        self.title("Parmonizer")
        self.geometry("720x520")
        self.resizable(True, True)

        self.preferences = HarmonyPreferences()
        self.solutions: List[HarmonySolution] = []
        self.current_chords: List[str] = []
        self.hold_time_entries: List[tk.Entry] = []
        self.solution_choice_var = tk.StringVar(value="")
        self.solution_count_var = tk.StringVar(value="3")
        default_synth = SynthSettings()
        self.waveform_var = tk.StringVar(value=default_synth.waveform)
        self.cutoff_var = tk.StringVar(value=str(default_synth.cutoff_hz))
        self.resonance_var = tk.StringVar(value=str(default_synth.resonance))
        self.attack_var = tk.StringVar(value=str(default_synth.attack))
        self.decay_var = tk.StringVar(value=str(default_synth.decay))
        self.sustain_var = tk.StringVar(value=str(default_synth.sustain_level))
        self.release_var = tk.StringVar(value=str(default_synth.release))

        self.columnconfigure(0, weight=1)
        self.rowconfigure(1, weight=1)
        self.rowconfigure(5, weight=1)

        instruction = tk.Label(self, text=_INPUT_INSTRUCTIONS, justify=tk.LEFT, anchor="w")
        instruction.grid(row=0, column=0, sticky="ew", padx=10, pady=(10, 0))

        self.input_text = tk.Text(self, height=10, wrap=tk.WORD)
        self.input_text.grid(row=1, column=0, sticky="nsew", padx=10, pady=10)

        self.hold_time_frame = tk.LabelFrame(self, text="Chord hold times (seconds)")
        self.hold_time_frame.grid(row=2, column=0, sticky="ew", padx=10)
        self.hold_time_frame.columnconfigure(1, weight=1)

        self.synth_frame = tk.LabelFrame(self, text="Playback settings")
        self.synth_frame.grid(row=3, column=0, sticky="ew", padx=10, pady=(5, 0))
        for column in range(8):
            self.synth_frame.columnconfigure(column, weight=0)
        self.synth_frame.columnconfigure(7, weight=1)

        waveform_label = tk.Label(self.synth_frame, text="Source:")
        waveform_label.grid(row=0, column=0, sticky="w", padx=(8, 4), pady=4)
        waveform_dropdown = ttk.Combobox(
            self.synth_frame,
            textvariable=self.waveform_var,
            state="readonly",
            values=("sine", "triangle", "saw", "rectangle"),
            width=10,
        )
        waveform_dropdown.grid(row=0, column=1, sticky="w", pady=4)

        cutoff_label = tk.Label(self.synth_frame, text="Cutoff (Hz):")
        cutoff_label.grid(row=0, column=2, sticky="w", padx=(12, 4), pady=4)
        self.cutoff_entry = tk.Entry(self.synth_frame, width=8, textvariable=self.cutoff_var)
        self.cutoff_entry.grid(row=0, column=3, sticky="w", pady=4)

        resonance_label = tk.Label(self.synth_frame, text="Q:")
        resonance_label.grid(row=0, column=4, sticky="w", padx=(12, 4), pady=4)
        self.resonance_entry = tk.Entry(self.synth_frame, width=6, textvariable=self.resonance_var)
        self.resonance_entry.grid(row=0, column=5, sticky="w", pady=4)

        attack_label = tk.Label(self.synth_frame, text="Attack (s):")
        attack_label.grid(row=1, column=0, sticky="w", padx=(8, 4), pady=(0, 6))
        self.attack_entry = tk.Entry(self.synth_frame, width=8, textvariable=self.attack_var)
        self.attack_entry.grid(row=1, column=1, sticky="w", pady=(0, 6))

        decay_label = tk.Label(self.synth_frame, text="Decay (s):")
        decay_label.grid(row=1, column=2, sticky="w", padx=(12, 4), pady=(0, 6))
        self.decay_entry = tk.Entry(self.synth_frame, width=8, textvariable=self.decay_var)
        self.decay_entry.grid(row=1, column=3, sticky="w", pady=(0, 6))

        sustain_label = tk.Label(self.synth_frame, text="Sustain (0-1):")
        sustain_label.grid(row=1, column=4, sticky="w", padx=(12, 4), pady=(0, 6))
        self.sustain_entry = tk.Entry(self.synth_frame, width=6, textvariable=self.sustain_var)
        self.sustain_entry.grid(row=1, column=5, sticky="w", pady=(0, 6))

        release_label = tk.Label(self.synth_frame, text="Release (s):")
        release_label.grid(row=1, column=6, sticky="w", padx=(12, 4), pady=(0, 6))
        self.release_entry = tk.Entry(self.synth_frame, width=8, textvariable=self.release_var)
        self.release_entry.grid(row=1, column=7, sticky="w", pady=(0, 6))

        controls = tk.Frame(self)
        controls.grid(row=4, column=0, sticky="ew", padx=10, pady=(5, 0))
        controls.columnconfigure(0, weight=0)
        controls.columnconfigure(1, weight=1)
        controls.columnconfigure(2, weight=0)
        controls.columnconfigure(3, weight=1)

        count_container = tk.Frame(controls)
        count_container.grid(row=0, column=0, sticky="w", padx=(0, 10))
        count_label = tk.Label(count_container, text="Solutions:")
        count_label.pack(side=tk.LEFT)
        self.solution_count_spinbox = tk.Spinbox(
            count_container,
            from_=1,
            to=12,
            width=4,
            textvariable=self.solution_count_var,
        )
        self.solution_count_spinbox.pack(side=tk.LEFT, padx=(4, 0))

        generate_button = tk.Button(controls, text="Generate Harmony", command=self.generate_harmony)
        generate_button.grid(row=0, column=1, sticky="ew", padx=(0, 5))

        selection_container = tk.Frame(controls)
        selection_container.grid(row=0, column=2, sticky="e", padx=(10, 5))
        selection_label = tk.Label(selection_container, text="Play solution:")
        selection_label.pack(side=tk.LEFT)
        self.solution_dropdown = ttk.Combobox(
            selection_container,
            textvariable=self.solution_choice_var,
            state="disabled",
            width=12,
            values=(),
        )
        self.solution_dropdown.pack(side=tk.LEFT, padx=(4, 0))

        self.play_button = tk.Button(controls, text="Play", state=tk.DISABLED, command=self.play_solution)
        self.play_button.grid(row=0, column=3, sticky="ew")

        self.output_text = tk.Text(self, state=tk.DISABLED, height=12, wrap=tk.WORD)
        self.output_text.grid(row=5, column=0, sticky="nsew", padx=10, pady=(10, 10))

        self.update_hold_time_fields([])

    def generate_harmony(self) -> None:
        raw_input = self.input_text.get("1.0", tk.END)
        self.play_button.configure(state=tk.DISABLED)
        self.solution_dropdown.configure(state="disabled")
        self.solution_dropdown["values"] = ()
        self.solution_choice_var.set("")
        self.solutions = []
        try:
            voice_count, chord_symbols = parse_user_input(raw_input)
            if not chord_symbols:
                raise ValueError("Please enter at least one chord symbol.")
            chords = [parse_chord_symbol(symbol) for symbol in chord_symbols]
        except Exception as exc:  # noqa: BLE001 - show the message to the user
            messagebox.showerror("Parmonizer", str(exc))
            return

        self.update_hold_time_fields(chord_symbols)

        try:
            voices = build_default_voice_ranges(voice_count)
            solver = HarmonySolver(voices=voices, preferences=self.preferences)
            try:
                solution_count = int(self.solution_count_var.get())
            except ValueError as exc:  # noqa: PERF203 - keep message precise
                raise ValueError("Number of solutions must be an integer greater than zero.") from exc
            if solution_count <= 0:
                raise ValueError("Number of solutions must be greater than zero.")
            solutions = solver.solve(chords, max_results=solution_count)
        except Exception as exc:  # noqa: BLE001 - show the message to the user
            messagebox.showerror("Parmonizer", str(exc))
            return

        self.output_text.configure(state=tk.NORMAL)
        self.output_text.delete("1.0", tk.END)
        if not solutions:
            self.output_text.insert(tk.END, "No solutions could be generated with the current constraints.\n")
            self.play_button.configure(state=tk.DISABLED)
        else:
            for index, solution in enumerate(solutions, start=1):
                self.output_text.insert(tk.END, f"Solution {index}\n")
                self.output_text.insert(tk.END, solution.format() + "\n\n")
            self.solutions = solutions
            values = [str(i + 1) for i in range(len(solutions))]
            self.solution_dropdown["values"] = values
            self.solution_choice_var.set(values[0])
            self.solution_dropdown.configure(state="readonly")
            self.play_button.configure(state=tk.NORMAL)
        self.output_text.configure(state=tk.DISABLED)

    def play_solution(self) -> None:
        if not self.solutions:
            messagebox.showinfo("Parmonizer", "Generate a harmony before attempting playback.")
            return

        selection = self.solution_choice_var.get().strip()
        if not selection:
            index = 0
        else:
            try:
                index = int(selection) - 1
            except ValueError:
                messagebox.showerror("Parmonizer", "Please select a solution to play from the dropdown.")
                return
        if not 0 <= index < len(self.solutions):
            messagebox.showerror("Parmonizer", "Selected solution is no longer available.")
            return
        solution = self.solutions[index]

        try:
            hold_times = self.collect_hold_times(len(solution.voicings))
            synth_settings = self.collect_synth_settings()
        except ValueError as exc:
            messagebox.showerror("Parmonizer", str(exc))
            return

        try:
            play_progression(solution, hold_times, settings=synth_settings)
        except RuntimeError as exc:
            messagebox.showerror("Parmonizer", str(exc))
        except Exception as exc:  # noqa: BLE001 - surface error to user
            messagebox.showerror("Parmonizer", f"Unable to play harmony: {exc}")

    def collect_hold_times(self, expected_count: int) -> List[float]:
        if expected_count != len(self.hold_time_entries):
            raise ValueError("Hold time entries do not match the number of chords in the solution.")

        durations: List[float] = []
        for index, entry in enumerate(self.hold_time_entries):
            text = entry.get().strip()
            if not text:
                text = "1.0"
                entry.delete(0, tk.END)
                entry.insert(0, text)
            try:
                value = float(text)
            except ValueError as exc:
                chord_name = self.current_chords[index] if index < len(self.current_chords) else str(index + 1)
                raise ValueError(f"Hold time for chord {index + 1} ('{chord_name}') is not a valid number.") from exc
            if value <= 0:
                chord_name = self.current_chords[index] if index < len(self.current_chords) else str(index + 1)
                raise ValueError(f"Hold time for chord {index + 1} ('{chord_name}') must be greater than zero.")
            durations.append(value)
        return durations

    def update_hold_time_fields(self, chord_symbols: Sequence[str]) -> None:
        if chord_symbols == self.current_chords and self.hold_time_entries:
            return

        for widget in self.hold_time_frame.winfo_children():
            widget.destroy()

        self.hold_time_entries = []
        self.current_chords = list(chord_symbols)

        if not chord_symbols:
            message = tk.Label(
                self.hold_time_frame,
                text="Hold time controls will appear after entering chord symbols.",
                anchor="w",
                justify=tk.LEFT,
            )
            message.grid(row=0, column=0, columnspan=2, sticky="w", padx=8, pady=6)
            return

        for index, symbol in enumerate(chord_symbols):
            label = tk.Label(self.hold_time_frame, text=f"{symbol}:", anchor="w")
            label.grid(row=index, column=0, sticky="w", padx=(8, 4), pady=3)
            entry = tk.Entry(self.hold_time_frame, width=8)
            entry.insert(0, "1.5")
            entry.grid(row=index, column=1, sticky="ew", padx=(0, 8), pady=3)
            self.hold_time_entries.append(entry)

    def collect_synth_settings(self) -> SynthSettings:
        defaults = SynthSettings()

        waveform = self.waveform_var.get().strip().lower() or defaults.waveform
        if waveform not in {"sine", "triangle", "saw", "rectangle"}:
            waveform = defaults.waveform
            self.waveform_var.set(waveform)

        cutoff_text = self.cutoff_var.get().strip()
        if not cutoff_text:
            cutoff = defaults.cutoff_hz
            self.cutoff_var.set(self._format_number(cutoff))
        else:
            try:
                cutoff = float(cutoff_text)
            except ValueError as exc:
                raise ValueError("Cutoff frequency must be a number.") from exc
            if not math.isfinite(cutoff) or cutoff <= 0:
                raise ValueError("Cutoff frequency must be greater than zero.")

        resonance_text = self.resonance_var.get().strip()
        if not resonance_text:
            resonance = defaults.resonance
            self.resonance_var.set(self._format_number(resonance))
        else:
            try:
                resonance = float(resonance_text)
            except ValueError as exc:
                raise ValueError("Filter Q must be a number.") from exc
            if not math.isfinite(resonance) or resonance <= 0:
                raise ValueError("Filter Q must be greater than zero.")

        attack = self._parse_time_value(self.attack_var, "Attack", defaults.attack)
        decay = self._parse_time_value(self.decay_var, "Decay", defaults.decay)
        release = self._parse_time_value(self.release_var, "Release", defaults.release)

        sustain_text = self.sustain_var.get().strip()
        if not sustain_text:
            sustain = defaults.sustain_level
            self.sustain_var.set(self._format_number(sustain))
        else:
            try:
                sustain = float(sustain_text)
            except ValueError as exc:
                raise ValueError("Sustain level must be a number between 0 and 1.") from exc
            if not math.isfinite(sustain) or not 0.0 <= sustain <= 1.0:
                raise ValueError("Sustain level must be between 0 and 1.")

        return SynthSettings(
            waveform=waveform,
            cutoff_hz=cutoff,
            resonance=resonance,
            attack=attack,
            decay=decay,
            sustain_level=sustain,
            release=release,
        )

    def _parse_time_value(self, variable: tk.StringVar, label: str, default: float) -> float:
        text = variable.get().strip()
        if not text:
            variable.set(self._format_number(default))
            return default
        try:
            value = float(text)
        except ValueError as exc:
            raise ValueError(f"{label} time must be a number.") from exc
        if not math.isfinite(value) or value < 0:
            raise ValueError(f"{label} time must be zero or greater.")
        return value

    @staticmethod
    def _format_number(value: float) -> str:
        if abs(value) < 1e-9:
            return "0"
        if abs(value) >= 1:
            return f"{value:.3f}".rstrip("0").rstrip(".")
        return f"{value:.4f}".rstrip("0").rstrip(".")


def parse_user_input(raw_text: str) -> tuple[int, List[str]]:
    voice_count = 4
    chord_symbols: List[str] = []
    tokens = re.split(r"[\s|]+", raw_text)
    for token in tokens:
        token = token.strip()
        if not token:
            continue
        if token.lower().startswith(("voices=", "parts=")):
            try:
                voice_count = int(token.split("=", 1)[1])
            except ValueError as exc:  # noqa: PERF203 - keep message precise
                raise ValueError(f"Could not parse voice directive '{token}'.") from exc
        elif token.startswith("#"):
            continue
        else:
            chord_symbols.append(token)
    return voice_count, chord_symbols


__all__ = ["HarmonyApp", "parse_user_input"]
