package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli;

import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.ConsoleIO;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

final class CliTestSupport {

  private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
  private final ConsoleIO console;

  CliTestSupport(final String input) {
    console =
        new ConsoleIO(
            new Scanner(input),
            new PrintStream(buffer, true, StandardCharsets.UTF_8));
  }

  ConsoleIO console() {
    return console;
  }

  String output() {
    return buffer.toString(StandardCharsets.UTF_8);
  }
}
