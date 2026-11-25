# Diagramas de gestión de usuarios

Generados con [Archify](https://github.com/tt-a1i/archify) a partir del commit `fb19417f94b26958f35463cc855b3f91c5bd31d0`. Abra los archivos HTML en un navegador; cada uno incluye referencias al código y opciones de exportación. Los JSON del mismo nombre son las fuentes editables de Archify.

## Vistas generales

- [Contexto general](contexto.html): clientes HTTP, consola, casos de uso, persistencia y correo.
- [Arquitectura hexagonal](hexagonal.html): entradas, puertos, aplicación, dominio, adaptadores y proveedores.

## Recorridos HTTP

- [Crear usuario — POST /api/users](crear.html)
- [Listar usuarios — GET /api/users](listar.html)
- [Consultar usuario — GET /api/users/{id}](consultar.html)
- [Actualizar usuario — PUT /api/users/{id}](actualizar.html)
- [Eliminar usuario — DELETE /api/users/{id}](eliminar.html)
- [Autenticar — POST /api/auth/login](login.html)
- [Salud — GET /health](salud.html)

## Dominio

- [Ciclo de vida de usuario](ciclo-usuario.html): creación en PENDING, estados permitidos y eliminación física. La actualización admite cualquiera de los estados, sin restringir las transiciones. `EmailDestinationModel` es un valor inmutable de notificación sin estados de ciclo de vida definidos.

Las vistas generales incluyen el código de consola como entrada inactiva; los recorridos detallados corresponden a los endpoints HTTP propios solicitados. Crear y actualizar programan notificaciones asíncronas: su ejecución y entrega no deben interpretarse como una confirmación previa a la respuesta HTTP. Los diagramas representan el código del commit indicado, no una comprobación del estado real de los proveedores desplegados.
