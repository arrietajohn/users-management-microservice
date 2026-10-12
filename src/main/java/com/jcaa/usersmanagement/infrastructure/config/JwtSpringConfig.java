package com.jcaa.usersmanagement.infrastructure.config;

import com.jcaa.usersmanagement.infrastructure.security.JwtConfig;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

@Slf4j
@Configuration(proxyBeanMethods = false)
public class JwtSpringConfig {

  private static final String PROP_JWT_SECRET             = "${jwt.secret}";
  private static final String PROP_JWT_ISSUER             = "${jwt.issuer}";
  private static final String PROP_JWT_EXPIRATION_MINUTES = "${jwt.expiration-minutes}";

  private static final String LOG_JWT_INIT =
      "[JwtSpringConfig] JWT inicializado. issuer={} expirationMinutes={}";

  @Value(PROP_JWT_SECRET)
  private String jwtSecret;

  @Value(PROP_JWT_ISSUER)
  private String jwtIssuer;

  @Value(PROP_JWT_EXPIRATION_MINUTES)
  private long jwtExpirationMinutes;

  @Bean
  public JwtConfig jwtConfig() {
    final JwtConfig config = new JwtConfig(jwtSecret, jwtIssuer, jwtExpirationMinutes);
    log.info(LOG_JWT_INIT, config.issuer(), config.expirationMinutes());
    return config;
  }

  @Bean
  public JwtEncoder jwtEncoder(final JwtConfig jwtConfig) {
    return new NimbusJwtEncoder(new ImmutableSecret<>(jwtConfig.secretKey()));
  }

  /** Valida firma HS256, expiración y que el emisor sea esta misma aplicación. */
  @Bean
  public JwtDecoder jwtDecoder(final JwtConfig jwtConfig) {
    final NimbusJwtDecoder decoder =
        NimbusJwtDecoder.withSecretKey(jwtConfig.secretKey())
            .macAlgorithm(MacAlgorithm.HS256)
            .build();
    decoder.setJwtValidator(JwtValidators.createDefaultWithIssuer(jwtConfig.issuer()));
    return decoder;
  }
}
