# Project instructions

- Use Java for application code and XML Views for UI.
- Keep code, identifiers, comments, and commit messages in English.
- Keep user-facing text in Android string resources and provide Spanish and English variants.
- Do not add UI literals to layout files; use resources for text, colors, dimensions, and drawables.
- Preserve the same view IDs in every `activity_main.xml` configuration variant.
- Keep adaptation in Android resources; do not add Java branches for locale, orientation, night mode, or screen size.
- Validate changes with `./gradlew assembleDebug lintDebug`.
- When the Android Studio MCP server is available, use IDE inspections and `build_project` after edits.
