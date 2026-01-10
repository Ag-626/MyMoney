package com.example.geektrust.domain;

import java.util.EnumMap;


public class PortfolioSnapshot {
  private final Month month;
  private final long equity;
  private final long debt;
  private final long gold;

  public PortfolioSnapshot(Month month, long equity, long debt, long gold) {
    this.month = month;
    this.equity = equity;
    this.debt = debt;
    this.gold = gold;
  }

  public Month getMonth() {
    return  month;
  }

  public long getEquity () {
    return equity;
  }

  public long getDebt() {
    return debt;
  }

  public long getGold() {
    return gold;
  }

  public long total() {
    return equity + debt + gold;
  }

  public EnumMap<FundType, Long> asMap() {
    EnumMap<FundType, Long> m = new EnumMap<>(FundType.class);
    m.put(FundType.EQUITY, equity);
    m.put(FundType.DEBT, debt);
    m.put(FundType.GOLD, gold);
    return m;
  }

  public static PortfolioSnapshot fromCurrent(Portfolio portfolio, Month month) {
    return new PortfolioSnapshot(
        month,
        portfolio.getCurrentAmount(FundType.EQUITY),
        portfolio.getCurrentAmount(FundType.DEBT),
        portfolio.getCurrentAmount(FundType.GOLD)
    );
  }
}
