package com.example.geektrust.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.EnumSet;
import org.junit.jupiter.api.Test;

class FundTypeTest {

  @Test
  void containsExpectedFundTypes() {
    assertEquals(3, FundType.values().length);
    assertTrue(EnumSet.allOf(FundType.class).contains(FundType.EQUITY));
    assertTrue(EnumSet.allOf(FundType.class).contains(FundType.DEBT));
    assertTrue(EnumSet.allOf(FundType.class).contains(FundType.GOLD));
  }
}

