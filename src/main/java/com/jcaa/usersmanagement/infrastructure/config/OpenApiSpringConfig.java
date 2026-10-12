package com.jcaa.usersmanagement.infrastructure.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/** Registra el esquema Bearer JWT para que Swagger UI muestre el botón "Authorize". */
@Configuration
@SecurityScheme(
    name = OpenApiSpringConfig.BEARER_AUTH_SCHEME,
    type = SecuritySchemeType.HTTP,
    scheme = OpenApiSpringConfig.BEARER_SCHEME,
    bearerFormat = OpenApiSpringConfig.BEARER_FORMAT)
public class OpenApiSpringConfig {

  public static final String BEARER_AUTH_SCHEME = "bearerAuth";
  static final String BEARER_SCHEME = "bearer";
  static final String BEARER_FORMAT = "JWT";
}
