package com.example.geektrust.service;

import com.example.geektrust.domain.FundType;
import com.example.geektrust.domain.Month;
import com.example.geektrust.domain.Portfolio;
import com.example.geektrust.domain.PortfolioSnapshot;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.EnumMap;
import java.util.Objects;

public class PortfolioService {
  private static final RoundingMode FLOOR_MODE = RoundingMode.FLOOR;
  private static final int PCT_DIV_SCALE = 10;

  private final Portfolio portfolio;

  public PortfolioService(Portfolio portfolio){
    this.portfolio = Objects.requireNonNull(portfolio);
  }


  public void allocate(long equity, long debt, long gold) {
    portfolio.allocate(equity, debt, gold);
  }

  public void setSip(long equity, long debt, long gold) {
    portfolio.setSip(equity, debt, gold);
  }

  public void applyChange(Month month,
      BigDecimal equityRatePct,
      BigDecimal debtRatePct,
      BigDecimal goldRatePct) {
    requireAllocated();

    validateApplyChange(month, equityRatePct, debtRatePct, goldRatePct);
    applySipIfApplicable(month);
    applyMarketChange(equityRatePct, debtRatePct, goldRatePct);

    storeMonthEndSnapshot(month);
    storeRebalanceSnapshotIfApplicable(month);

  }

  public PortfolioSnapshot balance(Month month) {
    requireAllocated();
    Objects.requireNonNull(month);

    PortfolioSnapshot snap = portfolio.getMonthEndSnapshot(month);
    if(snap == null)
      throw new IllegalStateException("No data for month " + month);
    return snap;
  }

  public PortfolioSnapshot rebalanceOrNull() {
    requireAllocated();
    return portfolio.getLastRebalanced();
  }

  private void validateApplyChange(Month month,
      BigDecimal equityRatePct,
      BigDecimal debtRatePct,
      BigDecimal goldRatePct) {
    Objects.requireNonNull(month);
    Objects.requireNonNull(equityRatePct);
    Objects.requireNonNull(debtRatePct);
    Objects.requireNonNull(goldRatePct);
  }

  private void applySipIfApplicable(Month month){
    if(!month.sipApplies()) return;
    applySip();
  }

  private void applySip() {
    if (!portfolio.isSipSet())
      return;

    EnumMap<FundType, Long> sip = portfolio.getSipAmounts();
    for (FundType f : FundType.values()) {
      long updated = portfolio.getCurrentAmount(f) + sip.get(f);
      portfolio.setCurrentAmount(f, updated);
    }
  }

  private void applyMarketChange(BigDecimal equityRatePct,
      BigDecimal debtRatePct,
      BigDecimal goldRatePct) {
    applyOne(FundType.EQUITY, equityRatePct);
    applyOne(FundType.DEBT, debtRatePct);
    applyOne(FundType.GOLD, goldRatePct);
  }


  private void applyOne(FundType fund, BigDecimal ratePct) {
    long current = portfolio.getCurrentAmount(fund);

    BigDecimal multiplier = BigDecimal.ONE.add(
      ratePct.divide(BigDecimal.valueOf(100), PCT_DIV_SCALE, RoundingMode.HALF_UP)
    );

    BigDecimal next = BigDecimal.valueOf(current).multiply(multiplier);

    long floored = next.setScale(0, FLOOR_MODE).longValueExact();
    portfolio.setCurrentAmount(fund, floored);
  }

  private void storeMonthEndSnapshot(Month month) {
    PortfolioSnapshot snapshot = PortfolioSnapshot.fromCurrent(portfolio, month);
    portfolio.putMonthEndSnapshot(month, snapshot);
  }

  private void storeRebalanceSnapshotIfApplicable(Month month){
    if (!(month.isRebalanceMonth()))
      return;

    PortfolioSnapshot reb = computeRebalanceSnapshotFromCurrentTotal(month);
    portfolio.setLastRebalanced(reb);

    portfolio.setCurrentAmount(FundType.EQUITY, reb.getEquity());
    portfolio.setCurrentAmount(FundType.DEBT, reb.getDebt());
    portfolio.setCurrentAmount(FundType.GOLD, reb.getGold());
  }


  private PortfolioSnapshot computeRebalanceSnapshotFromCurrentTotal(Month month){
    long total = portfolio.totalCurrent();
    return computeRebalanceSnapshot(total, month);
  }

  private PortfolioSnapshot computeRebalanceSnapshot(long total, Month month){
    EnumMap<FundType, Long> init = portfolio.getInitialAllocation();
    long initTotal = portfolio.totalInitial();

    long eq = floorRatio(total, init.get(FundType.EQUITY), initTotal);
    long de = floorRatio(total, init.get(FundType.DEBT), initTotal);
    long go = floorRatio(total, init.get(FundType.GOLD), initTotal);

    return new PortfolioSnapshot(month, eq, de, go);
  }

  private long floorRatio(long total, long part, long denom) {
    if (denom == 0) return 0L;
    return (total*part)/denom;
  }


  private void requireAllocated() {
    if (!portfolio.isAllocated()) {
      throw new IllegalStateException("ALLOCATE must be called before other commands");
    }
  }

}
