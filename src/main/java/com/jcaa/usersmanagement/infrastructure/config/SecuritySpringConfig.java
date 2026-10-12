package com.jcaa.usersmanagement.infrastructure.config;

import com.jcaa.usersmanagement.domain.enums.UserRole;
import com.jcaa.usersmanagement.infrastructure.security.JwtTokenProvider;
import com.jcaa.usersmanagement.infrastructure.security.RestAccessDeniedHandler;
import com.jcaa.usersmanagement.infrastructure.security.RestAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Reglas de acceso de la API REST (stateless, autenticación por JWT Bearer):
 *
 * <ul>
 *   <li>Público: login, Swagger UI / OpenAPI y {@code /error}.
 *   <li>{@code GET /api/users/**}: cualquier usuario autenticado.
 *   <li>{@code POST/PUT/DELETE /api/users/**}: solo rol {@code ADMIN}.
 *   <li>Cualquier otra ruta: requiere autenticación.
 * </ul>
 */
@Configuration
@EnableWebSecurity
public class SecuritySpringConfig {

  private static final String LOGIN_PATH = "/api/auth/login";
  private static final String USERS_PATH = "/api/users/**";
  private static final String ERROR_PATH = "/error";
  private static final String[] OPEN_API_PATHS = {
    "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**"
  };
  private static final String ROLE_AUTHORITY_PREFIX = "ROLE_";

  @Bean
  public SecurityFilterChain securityFilterChain(
      final HttpSecurity http,
      final JwtAuthenticationConverter jwtAuthenticationConverter,
      final RestAuthenticationEntryPoint authenticationEntryPoint,
      final RestAccessDeniedHandler accessDeniedHandler)
      throws Exception {
    return http.csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            authorize ->
                authorize
                    .requestMatchers(HttpMethod.POST, LOGIN_PATH).permitAll()
                    .requestMatchers(OPEN_API_PATHS).permitAll()
                    .requestMatchers(ERROR_PATH).permitAll()
                    .requestMatchers(HttpMethod.GET, USERS_PATH).authenticated()
                    .requestMatchers(USERS_PATH).hasRole(UserRole.ADMIN.name())
                    .anyRequest().authenticated())
        .oauth2ResourceServer(
            oauth2 ->
                oauth2
                    .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter))
                    .authenticationEntryPoint(authenticationEntryPoint)
                    .accessDeniedHandler(accessDeniedHandler))
        .exceptionHandling(
            exceptions ->
                exceptions
                    .authenticationEntryPoint(authenticationEntryPoint)
                    .accessDeniedHandler(accessDeniedHandler))
        .build();
  }

  /** Convierte el claim {@code role} del token en la autoridad {@code ROLE_<rol>}. */
  @Bean
  public JwtAuthenticationConverter jwtAuthenticationConverter() {
    final JwtGrantedAuthoritiesConverter authoritiesConverter =
        new JwtGrantedAuthoritiesConverter();
    authoritiesConverter.setAuthoritiesClaimName(JwtTokenProvider.ROLE_CLAIM);
    authoritiesConverter.setAuthorityPrefix(ROLE_AUTHORITY_PREFIX);

    final JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
    converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
    return converter;
  }
}
