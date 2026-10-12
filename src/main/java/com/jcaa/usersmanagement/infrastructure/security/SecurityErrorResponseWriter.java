package com.jcaa.usersmanagement.infrastructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.response.ApiErrorResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

/**
 * Escribe los errores 401/403 con el mismo formato {@link ApiErrorResponse} que usa {@code
 * GlobalExceptionHandler}. Hace falta porque esos errores ocurren en la cadena de filtros de
 * seguridad, antes de llegar a los controladores.
 */
@UtilityClass
class SecurityErrorResponseWriter {

  void write(
      final HttpServletResponse response,
      final ObjectMapper objectMapper,
      final HttpStatus status,
      final String message)
      throws IOException {
    response.setStatus(status.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());
    objectMapper.writeValue(
        response.getOutputStream(), new ApiErrorResponse(status.value(), message));
  }
}
