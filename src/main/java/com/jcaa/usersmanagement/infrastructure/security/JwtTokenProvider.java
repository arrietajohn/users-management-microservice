package com.jcaa.usersmanagement.infrastructure.security;

import com.jcaa.usersmanagement.domain.model.UserModel;
import java.time.Duration;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

/**
 * Emite el JWT de acceso de un usuario ya autenticado. El token solo lleva identificadores
 * técnicos: el ID en {@code sub} y el rol en {@value #ROLE_CLAIM}, nunca datos personales.
 */
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

  public static final String ROLE_CLAIM = "role";

  private final JwtEncoder jwtEncoder;
  private final JwtConfig jwtConfig;

  public IssuedToken generate(final UserModel user) {
    final Instant issuedAt = Instant.now();
    final Duration expiration = jwtConfig.expiration();

    final JwtClaimsSet claims =
        JwtClaimsSet.builder()
            .issuer(jwtConfig.issuer())
            .issuedAt(issuedAt)
            .expiresAt(issuedAt.plus(expiration))
            .subject(user.getId().value())
            .claim(ROLE_CLAIM, user.getRole().name())
            .build();
    final JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();

    final String tokenValue =
        jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    return new IssuedToken(tokenValue, expiration.toSeconds());
  }
}
