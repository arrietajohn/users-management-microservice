package com.jcaa.usersmanagement.infrastructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

/** Responde 403 cuando el token es válido pero el rol no alcanza para la operación. */
@Component
@RequiredArgsConstructor
public class RestAccessDeniedHandler implements AccessDeniedHandler {

  private static final String MESSAGE_FORBIDDEN =
      "No tienes permisos para realizar esta operación.";

  private final ObjectMapper objectMapper;

  @Override
  public void handle(
      final HttpServletRequest request,
      final HttpServletResponse response,
      final AccessDeniedException exception)
      throws IOException {
    SecurityErrorResponseWriter.write(
        response, objectMapper, HttpStatus.FORBIDDEN, MESSAGE_FORBIDDEN);
  }
}
