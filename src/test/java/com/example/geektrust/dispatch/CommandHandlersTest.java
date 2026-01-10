package com.example.geektrust.dispatch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.geektrust.app.MyMoneyAppService;
import com.example.geektrust.cli.CommandType;
import com.example.geektrust.cli.ParsedInput;
import com.example.geektrust.domain.Month;
import com.example.geektrust.domain.Portfolio;
import com.example.geektrust.domain.PortfolioSnapshot;
import com.example.geektrust.service.PortfolioService;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import org.junit.jupiter.api.Test;

class CommandHandlersTest {

  @Test
  void handlesAllocate() throws Exception {
    RecordingPortfolioService svc = new RecordingPortfolioService();
    EnumMap<CommandType, Handler> handlers = CommandHandlers.build(new MyMoneyAppService(svc));

    handlers
        .get(CommandType.ALLOCATE)
        .handle(new ParsedInput(CommandType.ALLOCATE, Arrays.asList("10", "20", "30")));

    assertTrue(svc.allocateCalled);
    assertEquals(10, svc.allocateEq);
    assertEquals(20, svc.allocateDebt);
    assertEquals(30, svc.allocateGold);
  }

  @Test
  void handlesSip() throws Exception {
    RecordingPortfolioService svc = new RecordingPortfolioService();
    EnumMap<CommandType, Handler> handlers = CommandHandlers.build(new MyMoneyAppService(svc));

    handlers
        .get(CommandType.SIP)
        .handle(new ParsedInput(CommandType.SIP, Arrays.asList("1", "2", "3")));

    assertTrue(svc.sipCalled);
    assertEquals(1, svc.sipEq);
    assertEquals(2, svc.sipDebt);
    assertEquals(3, svc.sipGold);
  }

  @Test
  void handlesChange() throws Exception {
    RecordingPortfolioService svc = new RecordingPortfolioService();
    EnumMap<CommandType, Handler> handlers = CommandHandlers.build(new MyMoneyAppService(svc));

    handlers
        .get(CommandType.CHANGE)
        .handle(
            new ParsedInput(
                CommandType.CHANGE, Arrays.asList("10%", "20%", "30%", "March")));

    assertTrue(svc.changeCalled);
    assertEquals(Month.MARCH, svc.changeMonth);
    assertEquals(new BigDecimal("10"), svc.changeEq);
    assertEquals(new BigDecimal("20"), svc.changeDebt);
    assertEquals(new BigDecimal("30"), svc.changeGold);
  }

  @Test
  void printsBalanceSnapshot() throws Exception {
    RecordingPortfolioService svc = new RecordingPortfolioService();
    svc.balanceReturn = new PortfolioSnapshot(Month.MARCH, 1, 2, 3);
    EnumMap<CommandType, Handler> handlers = CommandHandlers.build(new MyMoneyAppService(svc));

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream original = System.out;
    System.setOut(new PrintStream(baos));
    try {
      handlers
          .get(CommandType.BALANCE)
          .handle(new ParsedInput(CommandType.BALANCE, Arrays.asList("MARCH")));
      assertEquals("1 2 3", baos.toString().trim());
    } finally {
      System.setOut(original);
    }
  }

  @Test
  void printsRebalanceSnapshotOrCannotRebalance() throws Exception {
    RecordingPortfolioService svc = new RecordingPortfolioService();
    EnumMap<CommandType, Handler> handlers = CommandHandlers.build(new MyMoneyAppService(svc));

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream original = System.out;
    System.setOut(new PrintStream(baos));
    try {
      svc.rebalanceReturn = new PortfolioSnapshot(Month.DECEMBER, 10, 20, 30);
      handlers
          .get(CommandType.REBALANCE)
          .handle(new ParsedInput(CommandType.REBALANCE, Collections.emptyList()));
    } finally {
      System.setOut(original);
    }

    String outputWithSnapshot = baos.toString().trim();
    assertEquals("10 20 30", outputWithSnapshot);

    svc.rebalanceReturn = null;
    ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
    System.setOut(new PrintStream(baos2));
    try {
      handlers
          .get(CommandType.REBALANCE)
          .handle(new ParsedInput(CommandType.REBALANCE, Collections.emptyList()));
    } finally {
      System.setOut(original);
    }
    assertEquals("CANNOT_REBALANCE", baos2.toString().trim());
  }

  private static final class RecordingPortfolioService extends PortfolioService {
    boolean allocateCalled;
    boolean sipCalled;
    boolean changeCalled;
    long allocateEq;
    long allocateDebt;
    long allocateGold;
    long sipEq;
    long sipDebt;
    long sipGold;
    Month changeMonth;
    BigDecimal changeEq;
    BigDecimal changeDebt;
    BigDecimal changeGold;
    PortfolioSnapshot balanceReturn;
    PortfolioSnapshot rebalanceReturn;

    RecordingPortfolioService() {
      super(new Portfolio());
    }

    @Override
    public void allocate(long equity, long debt, long gold) {
      allocateCalled = true;
      allocateEq = equity;
      allocateDebt = debt;
      allocateGold = gold;
    }

    @Override
    public void setSip(long equity, long debt, long gold) {
      sipCalled = true;
      sipEq = equity;
      sipDebt = debt;
      sipGold = gold;
    }

    @Override
    public void applyChange(
        Month month, BigDecimal equityRatePct, BigDecimal debtRatePct, BigDecimal goldRatePct) {
      changeCalled = true;
      changeMonth = month;
      changeEq = equityRatePct;
      changeDebt = debtRatePct;
      changeGold = goldRatePct;
    }

    @Override
    public PortfolioSnapshot balance(Month month) {
      return balanceReturn;
    }

    @Override
    public PortfolioSnapshot rebalanceOrNull() {
      return rebalanceReturn;
    }
  }
}

