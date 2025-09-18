"""Entry point for launching the Parmonizer GUI."""
from __future__ import annotations

from .gui import HarmonyApp


def main() -> None:
    app = HarmonyApp()
    app.mainloop()


if __name__ == "__main__":  # pragma: no cover - manual execution
    main()
