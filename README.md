# Parmonizer (Python Edition)

Parmonizer is a small harmony assistant that models SATB (or more general multi-part) writing as a constraint
satisfaction problem with weighted soft preferences. The solver generates voice-leading options for a chord
progression and ranks the resulting harmonisations.

## Running the GUI

The project now uses Python. To start the desktop interface run:

```bash
python -m parmonizer.main
```

The window contains an input text box for chord symbols and a button that produces the best harmonies in the output
panel. Enter chords separated by spaces or newlines (e.g. `Cmaj7 Fmaj7 G7 Cmaj9`). You can optionally set the number of
parts with a directive like `voices=5`. After parsing the chord list the GUI exposes a hold-time entry for each chord and
a **Play Solution** button that auditions the highest ranked solution using those durations.

Audio playback relies on the optional [`simpleaudio`](https://simpleaudio.readthedocs.io/) package:

```bash
pip install simpleaudio
```

## Programmatic Use

You can also access the solver directly from Python:

```python
from parmonizer.music import parse_chord_symbol
from parmonizer.solver import HarmonySolver

chords = [parse_chord_symbol(sym) for sym in ["Cmaj7", "Fmaj7", "G7", "Cmaj9"]]
solver = HarmonySolver()
solutions = solver.solve(chords)
for solution in solutions:
    print(solution.format())
```

The solver balances hard constraints (voice ranges, chord completeness, etc.) with soft preferences (stepwise motion,
limited parallels, compact spacing) and returns the highest scoring progressions.
