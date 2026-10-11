package com.jcaa.usersmanagement.infrastructure.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@DisplayName("AsyncNotificationConfig")
class AsyncNotificationConfigTest {

  @Test
  @DisplayName("el executor ejecuta tareas en un hilo email-notification-")
  void shouldRunTasksInNotificationThread() throws InterruptedException {
    final Executor executor = new AsyncNotificationConfig().notificationExecutor();
    assertNotNull(executor);
    final CountDownLatch latch = new CountDownLatch(1);
    final StringBuilder threadName = new StringBuilder();
    executor.execute(
        () -> {
          threadName.append(Thread.currentThread().getName());
          latch.countDown();
        });
    assertTrue(latch.await(2, TimeUnit.SECONDS));
    assertTrue(threadName.toString().startsWith("email-notification-"));
    ((ThreadPoolTaskExecutor) executor).shutdown();
  }
}
