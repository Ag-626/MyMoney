package com.example.geektrust.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

class LineParserTest {

  private final LineParser parser = new LineParser();

  @Test
  void returnsNoopForBlankLines() {
    assertTrue(parser.parse("   ").isNoop());
    assertTrue(parser.parse(null).isNoop());
  }

  @Test
  void parsesCommandAndArguments() {
    ParsedInput input = parser.parse("ALLOCATE 10 20 30");
    assertEquals(CommandType.ALLOCATE, input.getCommandType());
    assertEquals(Arrays.asList("10", "20", "30"), input.getArgs());
  }

  @Test
  void trimsExtraWhitespace() {
    ParsedInput input = parser.parse("  SIP   1   2  3  ");
    assertEquals(CommandType.SIP, input.getCommandType());
    assertEquals(Arrays.asList("1", "2", "3"), input.getArgs());
  }
}

