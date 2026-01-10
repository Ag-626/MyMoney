package com.example.geektrust.dispatch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.geektrust.cli.CommandType;
import com.example.geektrust.domain.Month;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.Test;

class InputUtilTest {

  @Test
  void requireArgsThrowsWhenMissing() {
    assertThrows(
        IllegalArgumentException.class,
        () -> InputUtil.requireArgs(Arrays.asList("1", "2"), 3, CommandType.ALLOCATE));
  }

  @Test
  void requireLongParsesValue() {
    long value = InputUtil.requireLong(Arrays.asList("5"), 0, CommandType.SIP, "EQUITY");
    assertEquals(5L, value);

    assertThrows(
        IllegalArgumentException.class,
        () -> InputUtil.requireLong(Arrays.asList("x"), 0, CommandType.SIP, "EQUITY"));
    assertThrows(
        IllegalArgumentException.class,
        () -> InputUtil.requireLong(Collections.emptyList(), 1, CommandType.SIP, "EQUITY"));
  }

  @Test
  void requirePercentParsesAndValidates() {
    BigDecimal pct =
        InputUtil.requirePercent(Arrays.asList("10%"), 0, CommandType.CHANGE, "EQUITY_RATE");
    assertEquals(new BigDecimal("10"), pct);

    assertThrows(
        IllegalArgumentException.class,
        () -> InputUtil.requirePercent(Arrays.asList("10"), 0, CommandType.CHANGE, "EQUITY_RATE"));
  }

  @Test
  void requireMonthParsesMonthName() {
    Month m = InputUtil.requireMonth(Arrays.asList("April"), 0, CommandType.BALANCE);
    assertEquals(Month.APRIL, m);
  }
}

