# Users Management — Spring Boot, Arquitectura Hexagonal y DDD

Aplicación de gestión de usuarios construida con Java 17 y Spring Boot. La API REST es el punto de entrada activo. El código de la antigua CLI se conserva como adaptador inactivo y no posee un contenedor de dependencias independiente.

Spring es el único *composition root*: `Main` inicia el contexto y las dependencias se resuelven mediante configuración y component scanning de Spring.

## Verificación

`GET /health` es publico y responde HTTP 200 con los metadatos generados desde
`pom.xml` durante el build:

```json
{"status":"UP","name":"users-management","version":"2.2.0"}
```

Indica que la API responde; no comprueba MySQL ni el proveedor de correo.

```bash
./mvnw clean test
./mvnw clean package
```

En Windows se puede utilizar `mvnw.cmd`.

## Ejecución local (Guía 1)

Requisitos: JDK 17 o superior, MySQL 8.4 y Git. Maven no hace falta: el proyecto trae Maven Wrapper.

1. Crear la base de datos con `src/main/resources/schema.sql`. El script crea `crud_usuarios`, la
   tabla `users` y el administrador inicial (`admin@example.com` / `Admin1234!`).

   ```bash
   mysql -u root -p < src/main/resources/schema.sql
   ```

2. Copiar `.env.example` como `.env` y llenar los valores. `.env` está en `.gitignore` y no se sube
   al repositorio. Spring Boot lo lee desde el directorio de trabajo, así que el proyecto se ejecuta
   desde la raíz del repo.
3. Ejecutar:

   | Sistema | Comando |
   |---|---|
   | macOS / Linux | `./mvnw spring-boot:run` |
   | Windows | `mvnw.cmd spring-boot:run` |

4. Abrir Swagger UI en http://localhost:8080/swagger-ui/index.html.

### Abrir en un IDE

| IDE | Cómo abrirlo | Cómo ejecutarlo |
|---|---|---|
| IntelliJ IDEA | *Open* y elegir la carpeta del repo (detecta el `pom.xml`) | Configuración **Ejecutar-Aplicacion** (`.run/Main.run.xml`) |
| VS Code / Antigravity | *Open Folder* e instalar las extensiones recomendadas | *Run and Debug* → **Ejecutar-Aplicacion** (`.vscode/launch.json`) |
| NetBeans | *Open Project* y elegir la carpeta del repo | *Run Project* sobre la clase `com.jcaa.usersmanagement.Main` |

Las configuraciones de IntelliJ y VS Code fijan la raíz del repo como directorio de trabajo para que
se cargue `.env`.

### Colección de peticiones

- `docs/http/users.http`: para REST Client (VS Code/Antigravity) y para el HTTP Client de IntelliJ.
- `docs/http/users-management.postman_collection.json`: para importar en Postman, Insomnia o Boomerang.

Las dos recorren el flujo completo: health, 401 sin token, login, listar, crear (queda `PENDING`),
consultar por id, login rechazado mientras está `PENDING`, activar, login aceptado, eliminar y
consultar el eliminado (404).

## Despliegue en Render

El archivo `render.yaml` define el servicio web, el build con Docker y el despliegue
automático de cada commit que llegue a la rama `main`.

1. Crear un Blueprint en Render y seleccionar este repositorio.
2. Completar en el panel los secretos marcados como requeridos: `DB_HOST`,
   `DB_USERNAME`, `DB_PASSWORD`, `SMTP_USERNAME`, `SMTP_PASSWORD` y
   `SMTP_FROM_ADDRESS`.
3. Usar una instancia MySQL accesible desde Internet o desde la red privada de
   Render y ejecutar `src/main/resources/schema.sql` una vez para crear el esquema.
4. Desplegar el Blueprint. La API quedará disponible en el subdominio
   `onrender.com` asignado por Render y Swagger UI en `/swagger-ui.html`.

Las credenciales nunca deben guardarse en `application.properties` ni en
`render.yaml`. Para desarrollo local, deben proporcionarse como variables de entorno.
