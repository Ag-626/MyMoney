package com.example.geektrust.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class CommandTypeTest {

  @Test
  void mapsTokenCaseInsensitive() {
    assertEquals(CommandType.ALLOCATE, CommandType.fromToken("allocate"));
    assertEquals(CommandType.SIP, CommandType.fromToken("SiP"));
  }

  @Test
  void returnsNoopForBlankOrNull() {
    assertEquals(CommandType.NOOP, CommandType.fromToken("   "));
    assertEquals(CommandType.NOOP, CommandType.fromToken(null));
  }

  @Test
  void throwsOnUnknownToken() {
    assertThrows(IllegalArgumentException.class, () -> CommandType.fromToken("unknown"));
  }

  @Test
  void minArgsConfiguredPerCommand() {
    assertEquals(3, CommandType.ALLOCATE.minArgs());
    assertEquals(3, CommandType.SIP.minArgs());
    assertEquals(4, CommandType.CHANGE.minArgs());
    assertEquals(1, CommandType.BALANCE.minArgs());
    assertEquals(0, CommandType.REBALANCE.minArgs());
  }
}

