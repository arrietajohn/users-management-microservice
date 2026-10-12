package com.jcaa.usersmanagement.infrastructure.entrypoint.rest.controller;

import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.request.LoginRestRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.response.ApiErrorResponse;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.response.TokenRestResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

/** Contrato OpenAPI / Swagger para {@link AuthRestController}. */
@Tag(name = "Auth", description = "Autenticación: obtener el token JWT de acceso.")
public interface AuthRestControllerDocs {

  // ─────────────────────────────────────────────────────────────────────────────
  // POST /api/auth/login
  // ─────────────────────────────────────────────────────────────────────────────

  @Operation(
      summary = "Iniciar sesión",
      description =
          "Valida correo y contraseña y retorna un JWT de acceso. "
              + "Solo los usuarios con estado ACTIVE pueden iniciar sesión. "
              + "Envía el token en el header 'Authorization: Bearer <token>' "
              + "para consumir /api/users.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Credenciales válidas. Se retorna el token de acceso.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = TokenRestResponse.class))),
    @ApiResponse(
        responseCode = "400",
        description = "Datos de entrada inválidos (campos vacíos o formato incorrecto).",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(
        responseCode = "401",
        description = "Correo o contraseña incorrectos, o la cuenta no está activa.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class)))
  })
  TokenRestResponse login(@Valid @RequestBody LoginRestRequest request);
}
