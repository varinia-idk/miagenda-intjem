# Project instructions

- Use Java for application code and XML Views for UI.
- Keep code, identifiers, and comments in English.
- Write commit messages in Spanish, matching this repository's history and the
  language of the submitted report.
- Preserve the rubric-mandated Spanish identifiers `Tarea`, `Prioridad`, `Materia`, and `TareaAdapter`.
- Keep user-facing text in Android string resources and provide Spanish and English variants.
- Do not add UI literals to layout files; use resources for text, colors, dimensions, and drawables.
- Keep adaptation in Android resources; do not add Java branches for locale, orientation, night mode, or screen size.
- Preserve task and navigation state across configuration changes.
- Validate changes with `./gradlew assembleDebug lintDebug`.
- When the Android Studio MCP server is available, use IDE inspections and `build_project` after edits.
