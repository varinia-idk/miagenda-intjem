# MiAgenda INTJEM — Práctica 3.7

Aplicación Android en Java que demuestra el uso del sistema de recursos de Android. El proyecto implementa AP-3.7.1, AP-3.7.2 y la actividad complementaria AC-3.7.3.

## Requisitos cubiertos

- `minSdk 24` y `targetSdk 34`.
- Interfaz con `ConstraintLayout`, recursos externalizados y estilo `EstiloBotonPrincipal`.
- Paleta semántica, dimensiones reutilizables, `shape` XML e ícono vectorial.
- Variantes para orientación horizontal, idioma inglés, modo oscuro y tablet `sw600dp`.
- Tema `Theme.Material3.DayNight.NoActionBar`, roles Material 3, tipografía y formas.
- Código Java sin lógica para idioma, orientación, modo oscuro o tamaño de pantalla.

## Abrir en Android Studio

1. Abre la carpeta raíz de este proyecto en Android Studio.
2. Espera a que finalice Gradle Sync.
3. Selecciona un dispositivo físico o crea un emulador con API 24 o superior.
4. Ejecuta la configuración `app`.

## Verificación desde terminal

```bash
./gradlew assembleDebug
./gradlew lintDebug
```

La documentación académica, la configuración de MCP y las consignas originales están en [`docs/`](docs/).

`targetSdk` permanece en 34 porque es un requisito expreso de la guía. El proyecto usa Gradle 9.6.0 porque es la versión predeterminada y compatible indicada para AGP 9.4.1; Lint omite únicamente las sugerencias de cambiar esas dos versiones.
