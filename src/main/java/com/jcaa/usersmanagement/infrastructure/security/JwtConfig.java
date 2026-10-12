package com.jcaa.usersmanagement.infrastructure.security;

import com.jcaa.usersmanagement.infrastructure.config.ConfigurationException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Objects;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Parámetros del JWT. Se valida al construirse para que la aplicación falle al arrancar si la
 * configuración es insegura, en lugar de fallar al firmar el primer token.
 */
public record JwtConfig(String secret, String issuer, long expirationMinutes) {

  /** HMAC-SHA256 exige una clave de al menos 256 bits. */
  private static final int MINIMUM_SECRET_BYTES = 32;

  private static final String HMAC_ALGORITHM = "HmacSHA256";
  private static final String TO_STRING_TEMPLATE =
      "JwtConfig[secret=****, issuer=%s, expirationMinutes=%d]";

  public JwtConfig {
    validateSecret(secret);
    validateIssuer(issuer);
    validateExpiration(expirationMinutes);
  }

  public SecretKey secretKey() {
    return new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HMAC_ALGORITHM);
  }

  public Duration expiration() {
    return Duration.ofMinutes(expirationMinutes);
  }

  /** Oculta la clave para que nunca termine en un log. */
  @Override
  public String toString() {
    return String.format(TO_STRING_TEMPLATE, issuer, expirationMinutes);
  }

  private static void validateSecret(final String secret) {
    if (Objects.isNull(secret)
        || secret.getBytes(StandardCharsets.UTF_8).length < MINIMUM_SECRET_BYTES) {
      throw ConfigurationException.becauseJwtSecretIsTooShort(MINIMUM_SECRET_BYTES);
    }
  }

  private static void validateIssuer(final String issuer) {
    if (Objects.isNull(issuer) || issuer.isBlank()) {
      throw ConfigurationException.becauseJwtIssuerIsBlank();
    }
  }

  private static void validateExpiration(final long expirationMinutes) {
    if (expirationMinutes <= 0) {
      throw ConfigurationException.becauseJwtExpirationIsNotPositive();
    }
  }
}
