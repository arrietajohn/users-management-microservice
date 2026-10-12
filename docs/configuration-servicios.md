# Configuración de Servicios

### Aiven Valkey
La aplicación cuenta con una integración opcional de caché distribuida utilizando Valkey.

* **¿Qué se cachea?:** Se cachean exclusivamente las respuestas de los endpoints `GET /api/users` y `GET /api/users/{id}`.
* **Claves utilizadas:** Se utilizan las claves `users-management:users-list:all` y `users-management:users-by-id:<id>`.
* **Tiempo de vida (TTL):** El TTL es de 60 segundos por defecto, configurable mediante la variable de entorno `APP_CACHE_TTL_SECONDS`.
* **Invalidación:** La caché de la lista de usuarios se invalida automáticamente después de crear, actualizar o eliminar un usuario. La caché individual de un usuario se invalida cuando se actualiza o elimina ese usuario en específico.
* **Tolerancia a fallos:** Valkey es opcional. Si el servicio de caché está apagado, es inaccesible o falla, la aplicación captura el error, registra una advertencia en los logs y continúa consultando la base de datos PostgreSQL de forma transparente para que la aplicación no se detenga.
* **Restricciones de seguridad:** Por seguridad, nunca se cachean las respuestas del endpoint de login, tokens JWT, credenciales ni búsquedas por correo. Solo se guardan en caché los DTOs de respuesta sin contraseñas.

**Variables de entorno (Ejemplos ficticios):**
| Variable | Descripción | Ejemplo |
|---|---|---|
| `APP_CACHE_ENABLED` | Habilita o deshabilita la caché | `true` |
| `APP_CACHE_TTL_SECONDS` | Tiempo de vida de los datos | `60` |
| `VALKEY_HOST` | Host del servidor Valkey | `localhost` |
| `VALKEY_PORT` | Puerto del servidor | `6379` |
| `VALKEY_USERNAME` | Usuario de Valkey | `default` |
| `VALKEY_PASSWORD` | Contraseña | `password123` |
| `VALKEY_SSL_ENABLED` | Habilita conexión segura SSL | `false` (usar `true` en Aiven/Render) |
| `VALKEY_CONNECT_TIMEOUT_MS` | Timeout de conexión | `500` |
| `VALKEY_COMMAND_TIMEOUT_MS` | Timeout de comandos | `500` |