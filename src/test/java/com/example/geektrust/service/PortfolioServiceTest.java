package com.example.geektrust.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.geektrust.domain.Month;
import com.example.geektrust.domain.Portfolio;
import com.example.geektrust.domain.PortfolioSnapshot;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class PortfolioServiceTest {

  @Test
  void guardsAgainstCallsBeforeAllocation() {
    PortfolioService service = new PortfolioService(new Portfolio());
    assertThrows(IllegalStateException.class, () -> service.setSip(1, 1, 1));
    assertThrows(
        IllegalStateException.class,
        () -> service.applyChange(Month.JANUARY, bd(1), bd(1), bd(1)));
    assertThrows(IllegalStateException.class, () -> service.rebalanceOrNull());
  }

  @Test
  void balanceThrowsWhenSnapshotMissing() {
    PortfolioService service = new PortfolioService(new Portfolio());
    service.allocate(1, 1, 1);
    assertThrows(IllegalStateException.class, () -> service.balance(Month.JANUARY));
  }

  @Test
  void appliesSipAndMarketChanges() {
    PortfolioService service = new PortfolioService(new Portfolio());
    service.allocate(6000, 3000, 1000);
    service.setSip(2000, 1000, 500);

    service.applyChange(Month.JANUARY, bd(4), bd(10), bd(2));
    PortfolioSnapshot jan = service.balance(Month.JANUARY);
    assertEquals(6240, jan.getEquity());
    assertEquals(3300, jan.getDebt());
    assertEquals(1020, jan.getGold());

    service.applyChange(Month.FEBRUARY, bd(-10), bd(40), bd(0));
    PortfolioSnapshot feb = service.balance(Month.FEBRUARY);
    assertEquals(7416, feb.getEquity());
    assertEquals(6020, feb.getDebt());
    assertEquals(1520, feb.getGold());
  }

  @Test
  void rebalancesOnSixthMonth() {
    PortfolioService service = new PortfolioService(new Portfolio());
    service.allocate(100, 100, 100);
    service.setSip(10, 10, 10);

    List<Month> months =
        Arrays.asList(
            Month.JANUARY,
            Month.FEBRUARY,
            Month.MARCH,
            Month.APRIL,
            Month.MAY,
            Month.JUNE);
    for (Month m : months) {
      service.applyChange(m, bd(0), bd(0), bd(0));
    }

    PortfolioSnapshot reb = service.rebalanceOrNull();
    assertNotNull(reb);
    assertEquals(Month.JUNE, reb.getMonth());
    assertEquals(150, reb.getEquity());
    assertEquals(150, reb.getDebt());
    assertEquals(150, reb.getGold());
  }

  @Test
  void rebalanceIsNullBeforeRebalanceMonth() {
    PortfolioService service = new PortfolioService(new Portfolio());
    service.allocate(50, 50, 50);
    service.setSip(0, 0, 0);
    service.applyChange(Month.JANUARY, bd(0), bd(0), bd(0));

    assertNull(service.rebalanceOrNull());
  }

  private static BigDecimal bd(double value) {
    return BigDecimal.valueOf(value);
  }
}

