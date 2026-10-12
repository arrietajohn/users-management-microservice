package com.jcaa.usersmanagement.infrastructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/** Responde 401 cuando falta el token o no es válido (firma, emisor o expiración). */
@Component
@RequiredArgsConstructor
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private static final String MESSAGE_UNAUTHORIZED = "Se requiere un token de acceso válido.";
  private static final String BEARER_CHALLENGE = "Bearer";

  private final ObjectMapper objectMapper;

  @Override
  public void commence(
      final HttpServletRequest request,
      final HttpServletResponse response,
      final AuthenticationException exception)
      throws IOException {
    response.setHeader(HttpHeaders.WWW_AUTHENTICATE, BEARER_CHALLENGE);
    SecurityErrorResponseWriter.write(
        response, objectMapper, HttpStatus.UNAUTHORIZED, MESSAGE_UNAUTHORIZED);
  }
}
