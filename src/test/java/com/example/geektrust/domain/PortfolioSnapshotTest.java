package com.example.geektrust.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PortfolioSnapshotTest {

  @Test
  void exposesValuesAndTotals() {
    PortfolioSnapshot snapshot = new PortfolioSnapshot(Month.MARCH, 10, 20, 30);
    assertEquals(Month.MARCH, snapshot.getMonth());
    assertEquals(10, snapshot.getEquity());
    assertEquals(20, snapshot.getDebt());
    assertEquals(30, snapshot.getGold());
    assertEquals(60, snapshot.total());
  }

  @Test
  void asMapContainsAllFunds() {
    PortfolioSnapshot snapshot = new PortfolioSnapshot(Month.APRIL, 1, 2, 3);
    assertEquals(1L, snapshot.asMap().get(FundType.EQUITY));
    assertEquals(2L, snapshot.asMap().get(FundType.DEBT));
    assertEquals(3L, snapshot.asMap().get(FundType.GOLD));
  }

  @Test
  void buildsFromCurrentPortfolioState() {
    Portfolio portfolio = new Portfolio();
    portfolio.allocate(5, 6, 7);
    portfolio.setCurrentAmount(FundType.EQUITY, 8);
    PortfolioSnapshot snapshot = PortfolioSnapshot.fromCurrent(portfolio, Month.MAY);

    assertEquals(8, snapshot.getEquity());
    assertEquals(Month.MAY, snapshot.getMonth());
  }
}

