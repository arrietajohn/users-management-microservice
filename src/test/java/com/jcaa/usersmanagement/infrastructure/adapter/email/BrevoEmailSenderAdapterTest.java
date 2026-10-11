package com.jcaa.usersmanagement.infrastructure.adapter.email;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.jcaa.usersmanagement.domain.exception.EmailSenderException;
import com.jcaa.usersmanagement.domain.model.EmailDestinationModel;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@DisplayName("BrevoEmailSenderAdapter")
class BrevoEmailSenderAdapterTest {

  private HttpServer server;
  private final AtomicInteger calls = new AtomicInteger();
  private final AtomicReference<String> lastBody = new AtomicReference<>();
  private final AtomicReference<String> lastApiKey = new AtomicReference<>();
  private volatile int statusToReturn = 201;

  @BeforeEach
  void startServer() throws IOException {
    server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
    server.createContext(
        "/v3/smtp/email",
        exchange -> {
          calls.incrementAndGet();
          lastApiKey.set(exchange.getRequestHeaders().getFirst("api-key"));
          lastBody.set(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
          exchange.sendResponseHeaders(statusToReturn, -1);
          exchange.close();
        });
    server.start();
  }

  @AfterEach
  void stopServer() {
    server.stop(0);
  }

  private BrevoEmailSenderAdapter adapter(final String baseUrl, final int maxAttempts, final int minCalls) {
    final BrevoConfig config =
        new BrevoConfig(
            baseUrl, "xkeysib-test", "from@test.local", "Gestion",
            Duration.ofMillis(500), Duration.ofMillis(500),
            maxAttempts, Duration.ofMillis(1), 1.0, 50, 10, minCalls, Duration.ofSeconds(30));
    final SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
    factory.setConnectTimeout(Duration.ofMillis(500));
    factory.setReadTimeout(Duration.ofMillis(500));
    final RestClient client =
        RestClient.builder()
            .baseUrl(baseUrl)
            .requestFactory(factory)
            .defaultHeader("api-key", config.apiKey())
            .defaultHeader("Content-Type", "application/json")
            .build();
    return new BrevoEmailSenderAdapter(config, client);
  }

  private String url() {
    return "http://127.0.0.1:" + server.getAddress().getPort();
  }

  private static EmailDestinationModel destination() {
    return new EmailDestinationModel("dest@test.local", "Destino", "Asunto", "<p>Hola</p>");
  }

  @Test
  @DisplayName("envia el correo a Brevo con api-key, remitente y destinatario")
  void shouldSendEmail() {
    statusToReturn = 201;
    adapter(url(), 2, 5).send(destination());
    assertEquals(1, calls.get());
    assertEquals("xkeysib-test", lastApiKey.get());
    assertTrue(lastBody.get().contains("dest@test.local"));
    assertTrue(lastBody.get().contains("from@test.local"));
    assertTrue(lastBody.get().contains("idempotencyKey"));
  }

  @Test
  @DisplayName("un error 400 no se reintenta y lanza EmailSenderException")
  void shouldNotRetryClientError() {
    statusToReturn = 400;
    final EmailSenderException ex =
        assertThrows(EmailSenderException.class, () -> adapter(url(), 3, 5).send(destination()));
    assertEquals(1, calls.get());
    assertTrue(ex.getMessage().contains("HTTP 400"));
  }

  @Test
  @DisplayName("un error 500 se reintenta hasta el maximo de intentos")
  void shouldRetryServerError() {
    statusToReturn = 500;
    final EmailSenderException ex =
        assertThrows(EmailSenderException.class, () -> adapter(url(), 3, 50).send(destination()));
    assertEquals(3, calls.get());
    assertTrue(ex.getMessage().contains("HTTP 500"));
  }

  @Test
  @DisplayName("un 429 se considera transitorio y se reintenta")
  void shouldRetryTooManyRequests() {
    statusToReturn = 429;
    assertThrows(EmailSenderException.class, () -> adapter(url(), 2, 50).send(destination()));
    assertEquals(2, calls.get());
  }

  @Test
  @DisplayName("si el servidor no responde lanza EmailSenderException")
  void shouldFailWhenServerIsDown() {
    final String deadUrl = url();
    server.stop(0);
    final EmailSenderException ex =
        assertThrows(EmailSenderException.class, () -> adapter(deadUrl, 1, 50).send(destination()));
    assertTrue(ex.getMessage().contains("ResourceAccessException"));
  }

  @Test
  @DisplayName("el circuit breaker abierto bloquea nuevas llamadas")
  void shouldOpenCircuitBreaker() {
    statusToReturn = 500;
    final BrevoEmailSenderAdapter adapter = adapter(url(), 1, 2);
    assertThrows(EmailSenderException.class, () -> adapter.send(destination()));
    assertThrows(EmailSenderException.class, () -> adapter.send(destination()));
    final int callsBefore = calls.get();
    final EmailSenderException ex =
        assertThrows(EmailSenderException.class, () -> adapter.send(destination()));
    assertEquals(callsBefore, calls.get());
    assertTrue(ex.getMessage().contains("circuit breaker abierto"));
  }
}
