package com.jcaa.usersmanagement.infrastructure.config;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jcaa.usersmanagement.application.port.in.CreateUserUseCase;
import com.jcaa.usersmanagement.application.port.in.DeleteUserUseCase;
import com.jcaa.usersmanagement.application.port.in.GetAllUsersUseCase;
import com.jcaa.usersmanagement.application.port.in.GetUserByIdUseCase;
import com.jcaa.usersmanagement.application.port.in.LoginUseCase;
import com.jcaa.usersmanagement.application.port.in.UpdateUserUseCase;
import com.jcaa.usersmanagement.domain.enums.UserRole;
import com.jcaa.usersmanagement.domain.enums.UserStatus;
import com.jcaa.usersmanagement.domain.exception.InvalidCredentialsException;
import com.jcaa.usersmanagement.domain.model.UserModel;
import com.jcaa.usersmanagement.domain.valueobject.UserEmail;
import com.jcaa.usersmanagement.domain.valueobject.UserId;
import com.jcaa.usersmanagement.domain.valueobject.UserName;
import com.jcaa.usersmanagement.domain.valueobject.UserPassword;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.controller.AuthRestController;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.controller.UserRestController;
import com.jcaa.usersmanagement.infrastructure.security.JwtTokenProvider;
import com.jcaa.usersmanagement.infrastructure.security.RestAccessDeniedHandler;
import com.jcaa.usersmanagement.infrastructure.security.RestAuthenticationEntryPoint;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Tests de las reglas de acceso de SecuritySpringConfig.
 *
 * <p>Levanta solo la capa web con la configuración de seguridad real y usa tokens emitidos por el
 * {@link JwtTokenProvider} real, así se prueba el ciclo completo: firma, validación, conversión del
 * rol y autorización. Los casos de uso se mockean.
 */
@DisplayName("SecuritySpringConfig")
@WebMvcTest(controllers = {UserRestController.class, AuthRestController.class})
@Import({
  SecuritySpringConfig.class,
  JwtSpringConfig.class,
  JwtTokenProvider.class,
  RestAuthenticationEntryPoint.class,
  RestAccessDeniedHandler.class
})
@TestPropertySource(properties = "spring.main.web-application-type=servlet")
class SecuritySpringConfigTest {

  private static final String LOGIN_URL = "/api/auth/login";
  private static final String USERS_URL = "/api/users";
  private static final String USER_URL = "/api/users/u-001";
  private static final String BEARER_PREFIX = "Bearer ";
  private static final String LOGIN_BODY =
      "{\"email\":\"admin@example.com\",\"password\":\"Admin1234!\"}";
  private static final String CREATE_USER_BODY =
      "{\"id\":\"u-002\",\"name\":\"Jane Doe\",\"email\":\"jane@example.com\","
          + "\"password\":\"SecurePass1\",\"role\":\"MEMBER\"}";

  @Autowired private MockMvc mockMvc;
  @Autowired private JwtTokenProvider jwtTokenProvider;

  @MockBean private CreateUserUseCase createUserUseCase;
  @MockBean private UpdateUserUseCase updateUserUseCase;
  @MockBean private DeleteUserUseCase deleteUserUseCase;
  @MockBean private GetUserByIdUseCase getUserByIdUseCase;
  @MockBean private GetAllUsersUseCase getAllUsersUseCase;
  @MockBean private LoginUseCase loginUseCase;

  // ── login (público)

  @Test
  @DisplayName("POST /api/auth/login es público y retorna un token Bearer")
  void shouldReturnTokenWhenLoginSucceeds() throws Exception {
    // Arrange
    when(loginUseCase.execute(any())).thenReturn(buildUser(UserRole.ADMIN));

    // Act & Assert
    mockMvc
        .perform(post(LOGIN_URL).contentType(MediaType.APPLICATION_JSON).content(LOGIN_BODY))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.accessToken").isNotEmpty())
        .andExpect(jsonPath("$.tokenType").value("Bearer"))
        .andExpect(jsonPath("$.expiresIn").value(3600));
  }

  @Test
  @DisplayName("POST /api/auth/login responde 401 cuando las credenciales son inválidas")
  void shouldReturnUnauthorizedWhenCredentialsAreInvalid() throws Exception {
    // Arrange
    when(loginUseCase.execute(any()))
        .thenThrow(InvalidCredentialsException.becauseCredentialsAreInvalid());

    // Act & Assert
    mockMvc
        .perform(post(LOGIN_URL).contentType(MediaType.APPLICATION_JSON).content(LOGIN_BODY))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.status").value(401));
  }

  // ── sin token o token inválido

  @Test
  @DisplayName("GET /api/users responde 401 con cuerpo JSON cuando no se envía token")
  void shouldReturnUnauthorizedWhenTokenIsMissing() throws Exception {
    // Act & Assert
    mockMvc
        .perform(get(USERS_URL))
        .andExpect(status().isUnauthorized())
        .andExpect(header().string(HttpHeaders.WWW_AUTHENTICATE, "Bearer"))
        .andExpect(jsonPath("$.status").value(401));
    verifyNoInteractions(getAllUsersUseCase);
  }

  @Test
  @DisplayName("GET /api/users responde 401 cuando el token no es un JWT válido")
  void shouldReturnUnauthorizedWhenTokenIsMalformed() throws Exception {
    // Act & Assert
    mockMvc
        .perform(get(USERS_URL).header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + "no-es-un-jwt"))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.status").value(401));
    verifyNoInteractions(getAllUsersUseCase);
  }

  // ── autorización por rol

  @Test
  @DisplayName("GET /api/users responde 200 para cualquier usuario autenticado (MEMBER)")
  void shouldAllowReadForAuthenticatedMember() throws Exception {
    // Arrange
    when(getAllUsersUseCase.execute()).thenReturn(List.of());

    // Act & Assert
    mockMvc
        .perform(get(USERS_URL).header(HttpHeaders.AUTHORIZATION, bearerTokenFor(UserRole.MEMBER)))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("POST /api/users responde 403 con cuerpo JSON cuando el rol no es ADMIN")
  void shouldForbidCreateForMember() throws Exception {
    // Act & Assert
    mockMvc
        .perform(
            post(USERS_URL)
                .header(HttpHeaders.AUTHORIZATION, bearerTokenFor(UserRole.MEMBER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(CREATE_USER_BODY))
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.status").value(403));
    verifyNoInteractions(createUserUseCase);
  }

  @Test
  @DisplayName("POST /api/users responde 201 cuando el rol es ADMIN")
  void shouldAllowCreateForAdmin() throws Exception {
    // Arrange
    when(createUserUseCase.execute(any())).thenReturn(buildUser(UserRole.MEMBER));

    // Act & Assert
    mockMvc
        .perform(
            post(USERS_URL)
                .header(HttpHeaders.AUTHORIZATION, bearerTokenFor(UserRole.ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(CREATE_USER_BODY))
        .andExpect(status().isCreated());
  }

  @Test
  @DisplayName("DELETE /api/users/{id} responde 204 cuando el rol es ADMIN")
  void shouldAllowDeleteForAdmin() throws Exception {
    // Act & Assert
    mockMvc
        .perform(delete(USER_URL).header(HttpHeaders.AUTHORIZATION, bearerTokenFor(UserRole.ADMIN)))
        .andExpect(status().isNoContent());
    verify(deleteUserUseCase).execute(any());
  }

  @Test
  @DisplayName("DELETE /api/users/{id} responde 403 cuando el rol es REVIEWER")
  void shouldForbidDeleteForReviewer() throws Exception {
    // Act & Assert
    mockMvc
        .perform(
            delete(USER_URL).header(HttpHeaders.AUTHORIZATION, bearerTokenFor(UserRole.REVIEWER)))
        .andExpect(status().isForbidden());
    verifyNoInteractions(deleteUserUseCase);
  }

  private String bearerTokenFor(final UserRole role) {
    return BEARER_PREFIX + jwtTokenProvider.generate(buildUser(role)).value();
  }

  private static UserModel buildUser(final UserRole role) {
    return new UserModel(
        new UserId("u-001"),
        new UserName("John Arrieta"),
        new UserEmail("john@example.com"),
        UserPassword.fromHash("$2a$12$hash"),
        role,
        UserStatus.ACTIVE);
  }
}
