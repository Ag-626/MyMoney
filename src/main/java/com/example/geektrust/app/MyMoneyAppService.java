package com.example.geektrust.app;

import com.example.geektrust.domain.Month;
import com.example.geektrust.domain.PortfolioSnapshot;
import com.example.geektrust.service.PortfolioService;
import java.math.BigDecimal;
import java.util.Objects;

public final class MyMoneyAppService {

  private final PortfolioService portfolioService;

  public MyMoneyAppService(PortfolioService portfolioService) {
    this.portfolioService = Objects.requireNonNull(portfolioService);
  }

  public void allocate(long equity, long debt, long gold) {
    portfolioService.allocate(equity, debt, gold);
  }

  public void sip(long equity, long debt, long gold) {
    portfolioService.setSip(equity, debt, gold);
  }

  public void change(Month month, BigDecimal equityPct, BigDecimal debtPct, BigDecimal goldPct) {
    portfolioService.applyChange(month, equityPct, debtPct, goldPct);
  }

  public PortfolioSnapshot balance(Month month) {
    return portfolioService.balance(month);
  }

  public PortfolioSnapshot rebalanceOrNull() {
    return portfolioService.rebalanceOrNull();
  }
}
