package com.example.geektrust.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class ParsedInputTest {

  @Test
  void noopFactoryCreatesNoopInput() {
    ParsedInput input = ParsedInput.noop();
    assertTrue(input.isNoop());
    assertEquals(CommandType.NOOP, input.getCommandType());
    assertTrue(input.getArgs().isEmpty());
  }

  @Test
  void argsAreDefensiveCopies() {
    List<String> list = new ArrayList<>();
    list.add("1");
    ParsedInput input = new ParsedInput(CommandType.SIP, list);

    list.add("2");
    assertEquals(1, input.getArgs().size());
  }

  @Test
  void argsAreImmutable() {
    ParsedInput input = new ParsedInput(CommandType.BALANCE, Arrays.asList("JANUARY"));
    assertThrows(UnsupportedOperationException.class, () -> input.getArgs().add("FEBRUARY"));
  }
}

