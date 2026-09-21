# Integración MCP con Android Studio y Codex

El objetivo es que Codex pueda usar el índice, las inspecciones y las configuraciones de ejecución del IDE mediante el servidor MCP de JetBrains. La configuración se limita a este proyecto.

## Preparación

1. Instalar y habilitar el plugin oficial **MCP Server** (`com.intellij.mcpServer`) en Android Studio.
2. Abrir este proyecto y entrar a **Android Studio > Settings > Tools > MCP Server**.
3. Activar **Enable MCP Server** y revisar los permisos expuestos.
4. Verificar que el puerto mostrado sea `64342`, el mismo que figura en `.codex/config.toml`. Si Android Studio usa otro puerto, ejecutar **Auto-Configure** para Codex y aceptar que actualice la configuración del proyecto.
5. Reiniciar Codex para que lea `.codex/config.toml`.
6. Verificar la conexión con `/mcp` o `codex mcp list`.

El puerto HTTP lo asigna y muestra Android Studio. No debe copiarse un puerto de otro equipo ni subirse una dirección obsoleta al repositorio. Si cambia, se repite **Auto-Configure**.

## Herramientas recomendadas

Para este proyecto conviene exponer como mínimo:

- `build_project`, para compilar desde el IDE;
- `get_file_problems` y `lint_files`, para inspecciones;
- `get_project_modules` y `get_project_dependencies`, para conocer la estructura;
- herramientas de ejecución, Logcat y depuración cuando haya un emulador conectado.

La ejecución de comandos sin confirmación debe mantenerse desactivada salvo que se necesite expresamente. La configuración de Codex admite servidores MCP por proyecto en `.codex/config.toml`, mientras que Android Studio puede generarla con su opción de autoconfiguración.

Referencias:

- [JetBrains MCP Server](https://www.jetbrains.com/help/idea/mcp-server.html)
- [OpenAI: Model Context Protocol](https://developers.openai.com/codex/mcp)
