package com.example.geektrust.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.geektrust.domain.Month;
import com.example.geektrust.domain.Portfolio;
import com.example.geektrust.domain.PortfolioSnapshot;
import com.example.geektrust.service.PortfolioService;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class MyMoneyAppServiceTest {

  @Test
  void delegatesToPortfolioService() {
    Portfolio portfolio = new Portfolio();
    PortfolioService service = new PortfolioService(portfolio);
    MyMoneyAppService app = new MyMoneyAppService(service);

    app.allocate(100, 200, 300);
    assertTrue(portfolio.isAllocated());

    app.sip(10, 20, 30);
    app.change(Month.JANUARY, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);

    PortfolioSnapshot jan = app.balance(Month.JANUARY);
    assertEquals(100, jan.getEquity());
    assertEquals(200, jan.getDebt());
    assertEquals(300, jan.getGold());

    assertNull(app.rebalanceOrNull());
  }

  @Test
  void rebalanceSnapshotIsReturnedWhenAvailable() {
    Portfolio portfolio = new Portfolio();
    PortfolioService service = new PortfolioService(portfolio);
    MyMoneyAppService app = new MyMoneyAppService(service);

    app.allocate(100, 100, 100);
    app.change(Month.JUNE, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);

    PortfolioSnapshot reb = app.rebalanceOrNull();
    assertNotNull(reb);
    assertEquals(Month.JUNE, reb.getMonth());
  }
}

