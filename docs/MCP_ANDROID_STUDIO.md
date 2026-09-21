# Integración MCP con Android Studio y Codex

El objetivo es que Codex pueda usar el índice, las inspecciones y las configuraciones de ejecución del IDE mediante el servidor MCP de JetBrains. La configuración se limita a este proyecto.

## Preparación

1. Instalar y habilitar el plugin oficial **MCP Server** (`com.intellij.mcpServer`) en Android Studio.
2. Abrir este proyecto y entrar a **Android Studio > Settings > Tools > MCP Server**.
3. Activar **Enable MCP Server** y revisar los permisos expuestos.
4. Verificar que el puerto mostrado sea `64342`, el mismo que figura en `.codex/config.toml`. Si Android Studio usa otro puerto, ejecutar **Auto-Configure** para Codex y aceptar que actualice la configuración del proyecto.
5. Reiniciar Codex desde la raíz de este proyecto para que lea `.codex/config.toml`.
6. Verificar que `codex mcp list` muestre `android_studio`; el transporte configurado en este proyecto usa `/stream`.

El puerto HTTP lo asigna y muestra Android Studio. No debe copiarse un puerto de otro equipo ni subirse una dirección obsoleta al repositorio. Si cambia, se repite **Auto-Configure**.

## Herramientas recomendadas

Para este proyecto conviene exponer como mínimo:

- `build_project`, para compilar desde el IDE;
- `get_file_problems` y `lint_files`, para inspecciones;
- `get_project_modules` y `get_project_dependencies`, para conocer la estructura;
- herramientas de ejecución, Logcat y depuración cuando haya un emulador conectado.

La ejecución de comandos sin confirmación debe mantenerse desactivada salvo que se necesite expresamente. La configuración de Codex admite servidores MCP por proyecto en `.codex/config.toml`, mientras que Android Studio puede generarla con su opción de autoconfiguración.

## Estado verificado

El 20 de septiembre de 2026 se comprobó la integración con estos resultados:

- plugin oficial `com.intellij.mcpServer` instalado y servidor habilitado;
- endpoint local `http://127.0.0.1:64342/stream` accesible;
- negociación correcta del protocolo MCP `2025-06-18`;
- servidor identificado como `Android Studio MCP Server`, versión `Quail 4 | 2026.1.4 Patch 1`;
- catálogo de herramientas disponible, incluidas `build_project`, `get_file_problems`, `get_project_modules`, `get_project_dependencies` y las herramientas de ejecución;
- llamada real a `get_project_modules` completada sin error, con los módulos `MiAgendaINTJEM` y `MiAgendaINTJEM.app` reconocidos por el IDE.

Una sesión de Codex iniciada en el directorio padre no carga automáticamente la configuración situada en este subproyecto. Para consumir las herramientas directamente por nombre, Codex debe iniciarse de nuevo con esta carpeta como directorio de trabajo.

Referencias:

- [JetBrains MCP Server](https://www.jetbrains.com/help/idea/mcp-server.html)
- [OpenAI: Model Context Protocol](https://developers.openai.com/codex/mcp)
