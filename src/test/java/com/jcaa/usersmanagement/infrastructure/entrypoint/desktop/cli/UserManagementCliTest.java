package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller.UserController;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.UserResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("UserManagementCli")
class UserManagementCliTest {

  private final UserController controller = mock(UserController.class);

  private String run(final String input) {
    final CliTestSupport s = new CliTestSupport(input);
    new UserManagementCli(controller, s.console()).start();
    return s.output();
  }

  @Test
  void exit_immediately() {
    final String out = run("0\n");
    assertTrue(out.contains("Users Management System"));
    assertTrue(out.contains("Goodbye!"));
  }

  @Test
  void invalidOption_thenExit() {
    assertTrue(run("42\n0\n").contains("Invalid option"));
  }

  @Test
  void listOption_runsHandler() {
    when(controller.listAllUsers())
        .thenReturn(List.of(new UserResponse("u1", "Ana", "a@x.com", "ADMIN", "ACTIVE")));
    assertTrue(run("1\n0\n").contains("Total: 1"));
  }

  @Test
  void runtimeException_isReportedAsUnexpectedError() {
    when(controller.listAllUsers()).thenThrow(new IllegalStateException("boom"));
    assertTrue(run("1\n0\n").contains("Unexpected error: boom"));
  }

  @Test
  void constraintViolation_isReported() {
    @SuppressWarnings("unchecked")
    final ConstraintViolation<Object> violation = mock(ConstraintViolation.class);
    when(violation.getMessage()).thenReturn("bad field");
    final ConstraintViolationException exception =
        new ConstraintViolationException(Set.of(violation));
    when(controller.listAllUsers()).thenThrow(exception);
    final String out = run("1\n0\n");
    assertTrue(out.contains("Validation errors"));
    assertTrue(out.contains("bad field"));
  }
}
