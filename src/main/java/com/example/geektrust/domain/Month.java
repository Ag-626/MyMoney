package com.example.geektrust.domain;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public enum Month {
  JANUARY(1),
  FEBRUARY(2),
  MARCH(3),
  APRIL(4),
  MAY(5),
  JUNE(6),
  JULY(7),
  AUGUST(8),
  SEPTEMBER(9),
  OCTOBER(10),
  NOVEMBER(11),
  DECEMBER(12);

  private static final int SIP_START_ORDER = 2;
  private static final int REBALANCE_FREQUENCY = 6;

  private static final List<Month> VALUES_DESC =
      Arrays.asList(DECEMBER, NOVEMBER, OCTOBER, SEPTEMBER, AUGUST, JULY, JUNE, MAY, APRIL, MARCH, FEBRUARY, JANUARY);

  private final int order;
  Month (int order) {
    this.order = order;
  }

  public boolean sipApplies() {
    return this.order >= SIP_START_ORDER;
  }

  public boolean isRebalanceMonth() {
    return this.order % REBALANCE_FREQUENCY == 0;
  }

  public static Iterable<Month> descending() {
    return VALUES_DESC;
  }

  public static Month from(String s) {
    return Month.valueOf(s.trim().toUpperCase());
  }

}
