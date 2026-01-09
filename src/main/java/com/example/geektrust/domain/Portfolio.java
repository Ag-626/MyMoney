package com.example.geektrust.domain;

import java.util.EnumMap;
import java.util.Objects;

public class Portfolio {
  private final EnumMap<FundType, Long> initialAllocation = new EnumMap<>(FundType.class);
  private final EnumMap<FundType, Long> sipAmounts = new EnumMap<>(FundType.class);
  private final EnumMap<FundType, Long> currentAmounts = new EnumMap<>(FundType.class);

  private final EnumMap<Month, PortfolioSnapshot> monthEndSnapshots = new EnumMap<>(Month.class);

  private PortfolioSnapshot lastRebalanced;
  private boolean allocated;
  private boolean sipSet;

  public Portfolio(){
    initializeDefaults();
  }

  public boolean isAllocated(){
    return allocated;
  }

  public boolean isSipSet(){
    return sipSet;
  }

  public void allocate(long equity, long debt, long gold) {
    setInitialAllocation(equity, debt, gold);
    setCurrentAmounts(equity, debt, gold);
    setAllocated(true);
  }

  public void setSip(long equity,long debt, long gold) {
    sipAmounts.put(FundType.EQUITY, equity);
    sipAmounts.put(FundType.DEBT, debt);
    sipAmounts.put(FundType.GOLD, gold);
    setSipSet(true);
  }

  public long getCurrentAmount(FundType fund){
    return currentAmounts.get(Objects.requireNonNull(fund));
  }

  public void setCurrentAmounts(FundType fund, long amount) {
    currentAmounts.put(Objects.requireNonNull(fund), amount);
  }

  public EnumMap<FundType, Long> getInitialAllocation() {
    return new EnumMap<>(initialAllocation);
  }

  public EnumMap<FundType, Long> getSipAmounts() {
    return new EnumMap<>(sipAmounts);
  }

  public long totalInitial() {
    return sum(initialAllocation);
  }

  public long totalCurrent() {
    return sum(currentAmounts);
  }

  public void putMonthEndSnapshot (Month month, PortfolioSnapshot snapshot) {
    monthEndSnapshots.put(Objects.requireNonNull(month), Objects.requireNonNull(snapshot));
  }

  public PortfolioSnapshot getMonthEndSnapshot(Month month) {
    return monthEndSnapshots.get(month);
  }

  public boolean hasMonthEndSnapshot(Month month) {
    return monthEndSnapshots.containsKey(month);
  }


  public void setLastRebalanced(PortfolioSnapshot snapshot){
    this.lastRebalanced = snapshot;
  }

  public PortfolioSnapshot getLastRebalanced() {
    return lastRebalanced;
  }

  private long sum(EnumMap<FundType, Long> map) {
    long total = 0L;
    for(FundType f : FundType.values()) {
      total +=map.get(f);
    }
    return total;
  }

  private void initializeDefaults(){
    for (FundType f : FundType.values()) {
      initialAllocation.put(f, 0L);
      sipAmounts.put(f, 0L);
      currentAmounts.put(f, 0L);
    }
  }

  private void setInitialAllocation(long equity, long debt, long gold){
    initialAllocation.put(FundType.EQUITY, equity);
    initialAllocation.put(FundType.DEBT, debt);
    initialAllocation.put(FundType.GOLD, gold);
  }

  private void setCurrentAmounts(long equity, long debt, long gold){
    currentAmounts.put(FundType.EQUITY, equity);
    currentAmounts.put(FundType.DEBT, debt);
    currentAmounts.put(FundType.GOLD, gold);
  }

  private void setAllocated(boolean allocated){
    this.allocated = allocated;
  }

  private void setSipSet(boolean sipSet){
    this.sipSet = sipSet;
  }



}
