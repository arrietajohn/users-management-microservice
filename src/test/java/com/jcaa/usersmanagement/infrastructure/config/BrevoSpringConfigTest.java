package com.jcaa.usersmanagement.infrastructure.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.jcaa.usersmanagement.infrastructure.adapter.email.BrevoConfig;
import java.time.Duration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

@DisplayName("BrevoSpringConfig")
class BrevoSpringConfigTest {

  private final BrevoSpringConfig springConfig = new BrevoSpringConfig();

  private BrevoConfig config() {
    return springConfig.brevoConfig(
        "http://localhost", "xkeysib-test", "from@test.local", "Gestion",
        Duration.ofMillis(100), Duration.ofMillis(100),
        1, Duration.ofMillis(1), 2.0, 50, 10, 5, Duration.ofSeconds(1));
  }

  @Test
  @DisplayName("crea el BrevoConfig con los valores recibidos")
  void shouldBuildConfig() {
    final BrevoConfig config = config();
    assertEquals("http://localhost", config.baseUrl());
    assertEquals("from@test.local", config.fromAddress());
  }

  @Test
  @DisplayName("crea el RestClient para Brevo")
  void shouldBuildRestClient() {
    final RestClient client = springConfig.brevoRestClient(config());
    assertNotNull(client);
  }
}
