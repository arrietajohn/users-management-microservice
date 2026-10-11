package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ConsoleIO")
class ConsoleIOTest {

  @Test
  void readRequired_retriesWhileBlank() {
    final CliTestSupport s = new CliTestSupport("   \n  value  \n");
    assertEquals("value", s.console().readRequired("> "));
    assertTrue(s.output().contains("Value cannot be blank"));
  }

  @Test
  void readOptional_returnsTrimmedValue() {
    final CliTestSupport s = new CliTestSupport("  x \n");
    assertEquals("x", s.console().readOptional("> "));
  }

  @Test
  void readInt_retriesOnInvalidInput() {
    final CliTestSupport s = new CliTestSupport("abc\n7\n");
    assertEquals(7, s.console().readInt("> "));
    assertTrue(s.output().contains("Invalid input"));
  }

  @Test
  void printMethods_writeToOutput() {
    final CliTestSupport s = new CliTestSupport("");
    s.console().println("hello");
    s.console().println();
    s.console().printf("n=%d", 3);
    assertTrue(s.output().contains("hello"));
    assertTrue(s.output().contains("n=3"));
  }
}
