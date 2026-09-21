# MiAgenda INTJEM — Práctica 3.8

Aplicación Android en Java que extiende la práctica 3.7 con componentes avanzados: `RecyclerView`, `CardView`, `Parcelable`, Intents explícitos e implícitos, resultado entre Activities, Fragments, navegación inferior, Toolbar y menú.

## Integrantes y roles

- Brita Varinia Perez Villegas: conductora principal.
- Carlos Andres Flores Carpio: navegante principal.

Los roles están invertidos respecto de la práctica 3.7, como exige la consigna.

## Funcionalidad

- Modelo `Tarea` parcelable con identificador, título, materia, fecha y prioridad.
- Ocho tareas iniciales en un `RecyclerView` con `LinearLayoutManager` y tarjetas Material.
- Apertura y edición del detalle mediante un Intent explícito y `ActivityResultLauncher`.
- Compartir mediante `ACTION_SEND` y el selector del sistema.
- Alta desde un FAB y eliminación por pulsación larga con confirmación.
- Navegación entre Tareas, Calendario y Perfil mediante Fragments.
- Conservación de las tareas, el filtro y la sección seleccionada al rotar.
- Filtro opcional por materia y notificación opcional para tareas de prioridad alta.
- Interfaz en español e inglés, con recursos para modo oscuro y tablet heredados de 3.7.

## Requisitos del entorno

- Android Studio Quail o posterior.
- JDK 17.
- Android SDK con API 34 o superior instalada.
- AVD recomendado: `MiAgenda_API_34`, Pixel 6, Android 14/API 34, Google APIs, `arm64-v8a`.

El proyecto conserva `minSdk 24` y `targetSdk 34`, requeridos por la guía. `compileSdk` puede ser superior porque solo afecta la compilación.

## Abrir y ejecutar

1. Abre esta carpeta como proyecto en Android Studio.
2. Espera a que finalice Gradle Sync.
3. Selecciona `MiAgenda_API_34` en Device Manager.
4. Ejecuta la configuración `app`.

Desde terminal:

```bash
./gradlew assembleDebug lintDebug --no-daemon
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

## Documentación

- [Informe de la práctica 3.8](docs/INFORME_3.8.md)
- [Configuración MCP de Android Studio](docs/MCP_ANDROID_STUDIO.md)
- [Consigna oficial](docs/Guia_Actividades_3.8_Android_Java.pdf)
- [Guía explicada](docs/Guia_3.8_Componentes_Avanzados_EXPLICADA.pdf)
- [Capturas verificadas](docs/captures/3.8/)

La historia de Git de este repositorio conserva los commits de la práctica 3.7 para demostrar que 3.8 es una continuación del mismo proyecto.
