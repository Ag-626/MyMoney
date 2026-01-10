package com.example.geektrust.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PortfolioTest {

  @Test
  void allocateInitializesState() {
    Portfolio portfolio = new Portfolio();
    portfolio.allocate(100, 200, 300);

    assertTrue(portfolio.isAllocated());
    assertEquals(600, portfolio.totalInitial());
    assertEquals(600, portfolio.totalCurrent());
    assertEquals(100, portfolio.getCurrentAmount(FundType.EQUITY));
    assertEquals(200, portfolio.getCurrentAmount(FundType.DEBT));
    assertEquals(300, portfolio.getCurrentAmount(FundType.GOLD));
  }

  @Test
  void allocationOnlyAllowedOnce() {
    Portfolio portfolio = new Portfolio();
    portfolio.allocate(1, 1, 1);
    assertThrows(IllegalStateException.class, () -> portfolio.allocate(2, 2, 2));
  }

  @Test
  void sipRequiresAllocationAndOnlyOnce() {
    Portfolio portfolio = new Portfolio();
    assertThrows(IllegalStateException.class, () -> portfolio.setSip(1, 1, 1));

    portfolio.allocate(1, 1, 1);
    portfolio.setSip(2, 3, 4);
    assertTrue(portfolio.isSipSet());
    assertThrows(IllegalStateException.class, () -> portfolio.setSip(5, 6, 7));
  }

  @Test
  void tracksSnapshotsAndRebalance() {
    Portfolio portfolio = new Portfolio();
    portfolio.allocate(1, 2, 3);

    PortfolioSnapshot snap = new PortfolioSnapshot(Month.JANUARY, 1, 2, 3);
    portfolio.putMonthEndSnapshot(Month.JANUARY, snap);
    assertTrue(portfolio.hasMonthEndSnapshot(Month.JANUARY));
    assertEquals(snap, portfolio.getMonthEndSnapshot(Month.JANUARY));

    portfolio.setLastRebalanced(snap);
    assertEquals(snap, portfolio.getLastRebalanced());
  }

  @Test
  void updatesCurrentAmountsAndTotals() {
    Portfolio portfolio = new Portfolio();
    portfolio.allocate(10, 20, 30);

    portfolio.setCurrentAmount(FundType.EQUITY, 15);
    portfolio.setCurrentAmount(FundType.DEBT, 25);
    portfolio.setCurrentAmount(FundType.GOLD, 35);

    assertEquals(15, portfolio.getCurrentAmount(FundType.EQUITY));
    assertEquals(75, portfolio.totalCurrent());
  }

  @Test
  void defaultsAreZeroed() {
    Portfolio portfolio = new Portfolio();
    assertFalse(portfolio.isAllocated());
    assertFalse(portfolio.isSipSet());
    assertEquals(0, portfolio.totalCurrent());
  }
}

