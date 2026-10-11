package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.UserResponsePrinter;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.UserResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("UserResponsePrinter")
class UserResponsePrinterTest {

  private static final UserResponse USER =
      new UserResponse("u1", "Ana", "ana@x.com", "ADMIN", "ACTIVE");

  @Test
  void print_showsAllFields() {
    final CliTestSupport s = new CliTestSupport("");
    new UserResponsePrinter(s.console()).print(USER);
    assertTrue(s.output().contains("u1"));
    assertTrue(s.output().contains("Ana"));
    assertTrue(s.output().contains("ana@x.com"));
    assertTrue(s.output().contains("ADMIN"));
    assertTrue(s.output().contains("ACTIVE"));
  }

  @Test
  void printList_empty_showsMessage() {
    final CliTestSupport s = new CliTestSupport("");
    new UserResponsePrinter(s.console()).printList(List.of());
    assertTrue(s.output().contains("No users found"));
  }

  @Test
  void printList_showsTotalAndUsers() {
    final CliTestSupport s = new CliTestSupport("");
    new UserResponsePrinter(s.console()).printList(List.of(USER));
    assertTrue(s.output().contains("Total: 1"));
    assertTrue(s.output().contains("Ana"));
  }
}
