package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;

import com.jcaa.usersmanagement.domain.exception.InvalidCredentialsException;
import com.jcaa.usersmanagement.domain.exception.UserAlreadyExistsException;
import com.jcaa.usersmanagement.domain.exception.UserNotFoundException;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler.*;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.UserResponsePrinter;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller.UserController;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.UserResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("CLI handlers")
class CliHandlersTest {

  private static final UserResponse USER =
      new UserResponse("u1", "Ana", "ana@x.com", "ADMIN", "ACTIVE");

  private final UserController controller = mock(UserController.class);

  // ── Create ──
  @Test
  void create_success() {
    final CliTestSupport s = new CliTestSupport("u1\nAna\nana@x.com\nPass1234!\nADMIN\n");
    when(controller.createUser(any())).thenReturn(USER);
    new CreateUserHandler(controller, s.console(), new UserResponsePrinter(s.console())).handle();
    assertTrue(s.output().contains("User created successfully"));
  }

  @Test
  void create_alreadyExists_printsError() {
    final CliTestSupport s = new CliTestSupport("u1\nAna\nana@x.com\nPass1234!\nADMIN\n");
    when(controller.createUser(any()))
        .thenThrow(UserAlreadyExistsException.becauseEmailAlreadyExists("ana@x.com"));
    new CreateUserHandler(controller, s.console(), new UserResponsePrinter(s.console())).handle();
    assertTrue(s.output().contains("Error:"));
  }

  // ── Update ──
  @Test
  void update_success_blankPasswordBecomesNull() {
    final CliTestSupport s = new CliTestSupport("u1\nAna\nana@x.com\n\nADMIN\nACTIVE\n");
    when(controller.updateUser(any())).thenReturn(USER);
    new UpdateUserHandler(controller, s.console(), new UserResponsePrinter(s.console())).handle();
    assertTrue(s.output().contains("User updated successfully"));
  }

  @Test
  void update_withPassword_success() {
    final CliTestSupport s = new CliTestSupport("u1\nAna\nana@x.com\nNew1234!\nADMIN\nACTIVE\n");
    when(controller.updateUser(any())).thenReturn(USER);
    new UpdateUserHandler(controller, s.console(), new UserResponsePrinter(s.console())).handle();
    assertTrue(s.output().contains("User updated successfully"));
  }

  @Test
  void update_notFound_printsMessage() {
    final CliTestSupport s = new CliTestSupport("u1\nAna\nana@x.com\n\nADMIN\nACTIVE\n");
    when(controller.updateUser(any()))
        .thenThrow(UserNotFoundException.becauseIdWasNotFound("u1"));
    new UpdateUserHandler(controller, s.console(), new UserResponsePrinter(s.console())).handle();
    assertTrue(s.output().contains("Not found:"));
  }

  // ── Delete ──
  @Test
  void delete_success() {
    final CliTestSupport s = new CliTestSupport("u1\n");
    new DeleteUserHandler(controller, s.console()).handle();
    verify(controller).deleteUser("u1");
    assertTrue(s.output().contains("User deleted successfully"));
  }

  @Test
  void delete_notFound() {
    final CliTestSupport s = new CliTestSupport("u1\n");
    doThrow(UserNotFoundException.becauseIdWasNotFound("u1")).when(controller).deleteUser("u1");
    new DeleteUserHandler(controller, s.console()).handle();
    assertTrue(s.output().contains("Not found:"));
  }

  // ── Find ──
  @Test
  void find_success() {
    final CliTestSupport s = new CliTestSupport("u1\n");
    when(controller.findUserById("u1")).thenReturn(USER);
    new FindUserByIdHandler(controller, s.console(), new UserResponsePrinter(s.console())).handle();
    assertTrue(s.output().contains("Ana"));
  }

  @Test
  void find_notFound() {
    final CliTestSupport s = new CliTestSupport("u1\n");
    when(controller.findUserById("u1")).thenThrow(UserNotFoundException.becauseIdWasNotFound("u1"));
    new FindUserByIdHandler(controller, s.console(), new UserResponsePrinter(s.console())).handle();
    assertTrue(s.output().contains("Not found:"));
  }

  // ── List ──
  @Test
  void list_printsUsers() {
    final CliTestSupport s = new CliTestSupport("");
    when(controller.listAllUsers()).thenReturn(List.of(USER));
    new ListUsersHandler(controller, new UserResponsePrinter(s.console())).handle();
    assertTrue(s.output().contains("Total: 1"));
  }

  // ── Login ──
  @Test
  void login_success() {
    final CliTestSupport s = new CliTestSupport("ana@x.com\nPass1234!\n");
    when(controller.login(any())).thenReturn(USER);
    new LoginHandler(controller, s.console(), new UserResponsePrinter(s.console())).handle();
    assertTrue(s.output().contains("Login successful"));
  }

  @Test
  void login_invalidCredentials() {
    final CliTestSupport s = new CliTestSupport("ana@x.com\nbad\n");
    when(controller.login(any()))
        .thenThrow(InvalidCredentialsException.becauseCredentialsAreInvalid());
    new LoginHandler(controller, s.console(), new UserResponsePrinter(s.console())).handle();
    assertTrue(s.output().contains("Error:"));
  }
}
