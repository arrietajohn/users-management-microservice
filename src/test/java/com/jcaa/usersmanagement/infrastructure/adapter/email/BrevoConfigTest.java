package com.jcaa.usersmanagement.infrastructure.adapter.email;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("BrevoConfig")
class BrevoConfigTest {

  private static BrevoConfig build(final String baseUrl, final String apiKey, final String from) {
    return new BrevoConfig(
        baseUrl, apiKey, from, "Gestion", Duration.ofSeconds(1), Duration.ofSeconds(1),
        2, Duration.ofMillis(1), 2.0, 50, 10, 5, Duration.ofSeconds(1));
  }

  @Test
  @DisplayName("acepta una configuracion valida")
  void shouldAcceptValidConfig() {
    final BrevoConfig config = build("http://localhost", "xkeysib-abc", "a@b.co");
    assertEquals("xkeysib-abc", config.apiKey());
    assertEquals("a@b.co", config.fromAddress());
  }

  @Test
  @DisplayName("rechaza base url vacia")
  void shouldRejectBlankBaseUrl() {
    assertThrows(IllegalArgumentException.class, () -> build(" ", "xkeysib-abc", "a@b.co"));
  }

  @Test
  @DisplayName("rechaza api key nula")
  void shouldRejectNullApiKey() {
    assertThrows(IllegalArgumentException.class, () -> build("http://localhost", null, "a@b.co"));
  }

  @Test
  @DisplayName("rechaza remitente vacio")
  void shouldRejectBlankFrom() {
    assertThrows(
        IllegalArgumentException.class, () -> build("http://localhost", "xkeysib-abc", ""));
  }

  @Test
  @DisplayName("rechaza api key sin el prefijo xkeysib-")
  void shouldRejectApiKeyWithoutPrefix() {
    assertThrows(
        IllegalArgumentException.class, () -> build("http://localhost", "abc123", "a@b.co"));
  }
}
