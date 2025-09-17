"""Tkinter based interface for the Parmonizer harmony solver."""
from __future__ import annotations

import tkinter as tk
from tkinter import messagebox
import re
from typing import List, Optional, Sequence

from .music import build_default_voice_ranges, parse_chord_symbol
from .playback import play_progression
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
        self.last_solution: Optional[HarmonySolution] = None
        self.current_chords: List[str] = []
        self.hold_time_entries: List[tk.Entry] = []

        self.columnconfigure(0, weight=1)
        self.rowconfigure(1, weight=1)
        self.rowconfigure(4, weight=1)

        instruction = tk.Label(self, text=_INPUT_INSTRUCTIONS, justify=tk.LEFT, anchor="w")
        instruction.grid(row=0, column=0, sticky="ew", padx=10, pady=(10, 0))

        self.input_text = tk.Text(self, height=10, wrap=tk.WORD)
        self.input_text.grid(row=1, column=0, sticky="nsew", padx=10, pady=10)

        self.hold_time_frame = tk.LabelFrame(self, text="Chord hold times (seconds)")
        self.hold_time_frame.grid(row=2, column=0, sticky="ew", padx=10)
        self.hold_time_frame.columnconfigure(1, weight=1)

        controls = tk.Frame(self)
        controls.grid(row=3, column=0, sticky="ew", padx=10, pady=(5, 0))
        controls.columnconfigure(0, weight=1)
        controls.columnconfigure(1, weight=1)

        generate_button = tk.Button(controls, text="Generate Harmony", command=self.generate_harmony)
        generate_button.grid(row=0, column=0, sticky="ew", padx=(0, 5))

        self.play_button = tk.Button(controls, text="Play Solution", state=tk.DISABLED, command=self.play_solution)
        self.play_button.grid(row=0, column=1, sticky="ew")

        self.output_text = tk.Text(self, state=tk.DISABLED, height=12, wrap=tk.WORD)
        self.output_text.grid(row=4, column=0, sticky="nsew", padx=10, pady=(10, 10))

        self.update_hold_time_fields([])

    def generate_harmony(self) -> None:
        raw_input = self.input_text.get("1.0", tk.END)
        self.play_button.configure(state=tk.DISABLED)
        self.last_solution = None
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
            solutions = solver.solve(chords, max_results=3)
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
            self.last_solution = solutions[0]
            self.play_button.configure(state=tk.NORMAL)
        self.output_text.configure(state=tk.DISABLED)

    def play_solution(self) -> None:
        if self.last_solution is None:
            messagebox.showinfo("Parmonizer", "Generate a harmony before attempting playback.")
            return

        try:
            hold_times = self.collect_hold_times(len(self.last_solution.voicings))
        except ValueError as exc:
            messagebox.showerror("Parmonizer", str(exc))
            return

        try:
            play_progression(self.last_solution, hold_times)
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
