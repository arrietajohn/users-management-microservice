package com.jcaa.usersmanagement.infrastructure.security;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.jcaa.usersmanagement.infrastructure.config.ConfigurationException;
import java.time.Duration;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests para JwtConfig.
 *
 * <p>Cubre: construcción válida, rechazo de clave nula o corta, emisor vacío, expiración no
 * positiva y que {@code toString()} no exponga la clave.
 */
@DisplayName("JwtConfig")
class JwtConfigTest {

  private static final String VALID_SECRET = "una-clave-de-prueba-de-32-bytes!";
  private static final String SHORT_SECRET = "clave-de-solo-31-bytes-de-largo";
  private static final String ISSUER = "users-management-test";
  private static final long EXPIRATION_MINUTES = 60L;

  @Test
  @DisplayName("constructor acepta una configuración válida y expone clave HMAC y duración")
  void shouldBuildSecretKeyAndExpirationWhenConfigIsValid() {
    // Arrange
    final JwtConfig config = new JwtConfig(VALID_SECRET, ISSUER, EXPIRATION_MINUTES);

    // Act
    final SecretKey secretKey = config.secretKey();
    final Duration expiration = config.expiration();

    // Assert
    assertAll(
        () -> assertEquals("HmacSHA256", secretKey.getAlgorithm()),
        () -> assertEquals(Duration.ofMinutes(EXPIRATION_MINUTES), expiration));
  }

  @Test
  @DisplayName("constructor lanza ConfigurationException cuando la clave es nula")
  void shouldThrowWhenSecretIsNull() {
    // Act & Assert
    assertThrows(
        ConfigurationException.class, () -> new JwtConfig(null, ISSUER, EXPIRATION_MINUTES));
  }

  @Test
  @DisplayName("constructor lanza ConfigurationException cuando la clave tiene menos de 32 bytes")
  void shouldThrowWhenSecretIsTooShort() {
    // Act & Assert
    assertThrows(
        ConfigurationException.class,
        () -> new JwtConfig(SHORT_SECRET, ISSUER, EXPIRATION_MINUTES));
  }

  @Test
  @DisplayName("constructor lanza ConfigurationException cuando el emisor está vacío")
  void shouldThrowWhenIssuerIsBlank() {
    // Act & Assert
    assertThrows(
        ConfigurationException.class, () -> new JwtConfig(VALID_SECRET, " ", EXPIRATION_MINUTES));
  }

  @Test
  @DisplayName("constructor lanza ConfigurationException cuando la expiración no es positiva")
  void shouldThrowWhenExpirationIsNotPositive() {
    // Act & Assert
    assertThrows(ConfigurationException.class, () -> new JwtConfig(VALID_SECRET, ISSUER, 0L));
  }

  @Test
  @DisplayName("toString() no expone la clave secreta")
  void shouldNotExposeSecretInToString() {
    // Arrange
    final JwtConfig config = new JwtConfig(VALID_SECRET, ISSUER, EXPIRATION_MINUTES);

    // Act
    final String result = config.toString();

    // Assert
    assertFalse(result.contains(VALID_SECRET), "la clave nunca debe aparecer en toString()");
  }
}
