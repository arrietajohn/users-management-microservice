package com.jcaa.usersmanagement.infrastructure.entrypoint.rest.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jcaa.usersmanagement.infrastructure.security.JwtAuthenticationFilter;
import com.jcaa.usersmanagement.infrastructure.security.JwtTokenService;
import com.jcaa.usersmanagement.infrastructure.security.RestAccessDeniedHandler;
import com.jcaa.usersmanagement.infrastructure.security.RestAuthenticationEntryPoint;
import com.jcaa.usersmanagement.infrastructure.security.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.info.ProjectInfoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = HealthRestController.class,
    properties = "spring.main.web-application-type=servlet")
@MockBean(JwtTokenService.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class,
    RestAuthenticationEntryPoint.class, RestAccessDeniedHandler.class,
    ProjectInfoAutoConfiguration.class})
class HealthRestControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private BuildProperties buildProperties;

  @Test
  void shouldReturnHealthAndBuildMetadataWithoutAuthentication() throws Exception {
    mockMvc.perform(get("/health"))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.status").value("UP"))
        .andExpect(jsonPath("$.name").value(buildProperties.getName()))
        .andExpect(jsonPath("$.version").value(buildProperties.getVersion()));
  }

  @Test
  void shouldKeepUserListingProtected() throws Exception {
    mockMvc.perform(get("/api/users")).andExpect(status().isUnauthorized());
  }
}
