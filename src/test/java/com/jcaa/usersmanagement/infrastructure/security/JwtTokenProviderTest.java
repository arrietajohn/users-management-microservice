package com.jcaa.usersmanagement.infrastructure.security;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.jcaa.usersmanagement.domain.enums.UserRole;
import com.jcaa.usersmanagement.domain.enums.UserStatus;
import com.jcaa.usersmanagement.domain.model.UserModel;
import com.jcaa.usersmanagement.domain.valueobject.UserEmail;
import com.jcaa.usersmanagement.domain.valueobject.UserId;
import com.jcaa.usersmanagement.domain.valueobject.UserName;
import com.jcaa.usersmanagement.domain.valueobject.UserPassword;
import com.jcaa.usersmanagement.infrastructure.config.JwtSpringConfig;
import java.time.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;

/**
 * Tests para JwtTokenProvider.
 *
 * <p>Usa el encoder y el decoder reales de {@link JwtSpringConfig} para comprobar el ciclo completo:
 * el token emitido se valida y trae los claims esperados, y un token firmado con otra clave se
 * rechaza.
 */
@DisplayName("JwtTokenProvider")
class JwtTokenProviderTest {

  private static final String SECRET = "una-clave-de-prueba-de-32-bytes!";
  private static final String OTHER_SECRET = "otra-clave-distinta-de-32-bytes!";
  private static final String ISSUER = "users-management-test";
  private static final long EXPIRATION_MINUTES = 60L;
  private static final String USER_ID = "u-001";

  private final JwtSpringConfig jwtSpringConfig = new JwtSpringConfig();

  private JwtTokenProvider provider;
  private JwtDecoder decoder;
  private UserModel user;

  @BeforeEach
  void setUp() {
    final JwtConfig config = new JwtConfig(SECRET, ISSUER, EXPIRATION_MINUTES);
    provider = new JwtTokenProvider(jwtSpringConfig.jwtEncoder(config), config);
    decoder = jwtSpringConfig.jwtDecoder(config);
    user =
        new UserModel(
            new UserId(USER_ID),
            new UserName("John Arrieta"),
            new UserEmail("john@example.com"),
            UserPassword.fromHash("$2a$12$hash"),
            UserRole.ADMIN,
            UserStatus.ACTIVE);
  }

  @Test
  @DisplayName("generate() emite un token válido con id, rol, emisor y expiración configurados")
  void shouldIssueTokenWithExpectedClaims() {
    // Act
    final IssuedToken token = provider.generate(user);
    final Jwt jwt = decoder.decode(token.value());

    // Assert
    final Duration expectedExpiration = Duration.ofMinutes(EXPIRATION_MINUTES);
    assertAll(
        () -> assertEquals(USER_ID, jwt.getSubject()),
        () ->
            assertEquals(
                UserRole.ADMIN.name(), jwt.getClaimAsString(JwtTokenProvider.ROLE_CLAIM)),
        () -> assertEquals(ISSUER, jwt.getClaimAsString("iss")),
        () ->
            assertEquals(
                expectedExpiration, Duration.between(jwt.getIssuedAt(), jwt.getExpiresAt())),
        () -> assertEquals(expectedExpiration.toSeconds(), token.expiresInSeconds()));
  }

  @Test
  @DisplayName("el decoder rechaza un token firmado con otra clave")
  void shouldRejectTokenSignedWithAnotherSecret() {
    // Arrange
    final JwtConfig otherConfig = new JwtConfig(OTHER_SECRET, ISSUER, EXPIRATION_MINUTES);
    final JwtTokenProvider otherProvider =
        new JwtTokenProvider(jwtSpringConfig.jwtEncoder(otherConfig), otherConfig);
    final String foreignToken = otherProvider.generate(user).value();

    // Act & Assert
    assertThrows(JwtException.class, () -> decoder.decode(foreignToken));
  }

  @Test
  @DisplayName("el decoder rechaza un token emitido por otro emisor")
  void shouldRejectTokenFromAnotherIssuer() {
    // Arrange
    final JwtConfig otherIssuerConfig = new JwtConfig(SECRET, "otro-emisor", EXPIRATION_MINUTES);
    final JwtTokenProvider otherProvider =
        new JwtTokenProvider(jwtSpringConfig.jwtEncoder(otherIssuerConfig), otherIssuerConfig);
    final String foreignToken = otherProvider.generate(user).value();

    // Act & Assert
    assertThrows(JwtException.class, () -> decoder.decode(foreignToken));
  }
}
