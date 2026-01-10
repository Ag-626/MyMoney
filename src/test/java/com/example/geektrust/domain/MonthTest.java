package com.example.geektrust.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;
import org.junit.jupiter.api.Test;

class MonthTest {

  @Test
  void sipAppliesFromFebruaryOnwards() {
    assertFalse(Month.JANUARY.sipApplies());
    assertTrue(Month.FEBRUARY.sipApplies());
  }

  @Test
  void identifiesRebalanceMonths() {
    assertTrue(Month.JUNE.isRebalanceMonth());
    assertTrue(Month.DECEMBER.isRebalanceMonth());
    assertFalse(Month.JULY.isRebalanceMonth());
  }

  @Test
  void descendingOrdersFromDecemberToJanuary() {
    Iterator<Month> it = Month.descending().iterator();
    assertEquals(Month.DECEMBER, it.next());
    while (it.hasNext()) {
      Month last = it.next();
      if (!it.hasNext()) {
        assertEquals(Month.JANUARY, last);
      }
    }
  }

  @Test
  void parsesFromStringCaseInsensitive() {
    assertEquals(Month.APRIL, Month.from("april"));
  }
}

