package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.menu.MenuOption;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MenuOption")
class MenuOptionTest {

  @Test
  void fromNumber_returnsMatchingOption() {
    for (final MenuOption option : MenuOption.values()) {
      assertEquals(option, MenuOption.fromNumber(option.getNumber()).orElseThrow());
      assertTrue(!option.getDescription().isBlank());
    }
  }

  @Test
  void fromNumber_unknownNumber_returnsEmpty() {
    assertTrue(MenuOption.fromNumber(99).isEmpty());
  }
}
