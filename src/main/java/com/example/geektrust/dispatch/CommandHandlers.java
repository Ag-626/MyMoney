package com.example.geektrust.dispatch;

import com.example.geektrust.app.MyMoneyAppService;
import com.example.geektrust.cli.CommandType;
import com.example.geektrust.domain.Month;
import com.example.geektrust.domain.PortfolioSnapshot;
import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.List;
import java.util.Objects;

public final class CommandHandlers {

  private CommandHandlers() {}

  private static final int IDX_EQUITY = 0;
  private static final int IDX_DEBT = 1;
  private static final int IDX_GOLD = 2;
  private static final int IDX_CHANGE_MONTH = 3;
  private static final int IDX_BALANCE_MONTH = 0;

  private static final String ARG_EQUITY = "EQUITY";
  private static final String ARG_DEBT = "DEBT";
  private static final String ARG_GOLD = "GOLD";

  private static final String ARG_EQUITY_RATE = "EQUITY_RATE";
  private static final String ARG_DEBT_RATE = "DEBT_RATE";
  private static final String ARG_GOLD_RATE = "GOLD_RATE";


  public static EnumMap<CommandType, Handler> build(MyMoneyAppService app) {
    Objects.requireNonNull(app);

    EnumMap<CommandType, Handler> m = new EnumMap<>(CommandType.class);

    m.put(CommandType.ALLOCATE, in -> {
      List<String> a = in.getArgs();
      InputUtil.requireArgs(a, CommandType.ALLOCATE.minArgs(), CommandType.ALLOCATE);

      app.allocate(
          InputUtil.requireLong(a, IDX_EQUITY, CommandType.ALLOCATE, ARG_EQUITY),
          InputUtil.requireLong(a, IDX_DEBT, CommandType.ALLOCATE, ARG_DEBT),
          InputUtil.requireLong(a, IDX_GOLD, CommandType.ALLOCATE, ARG_GOLD)
      );
    });

    m.put(CommandType.SIP, in -> {
      List<String> a = in.getArgs();
      InputUtil.requireArgs(a, CommandType.SIP.minArgs(), CommandType.SIP);

      app.sip(
        InputUtil.requireLong(a, IDX_EQUITY, CommandType.SIP, ARG_EQUITY),
        InputUtil.requireLong(a, IDX_DEBT, CommandType.SIP, ARG_DEBT),
        InputUtil.requireLong(a, IDX_GOLD, CommandType.SIP, ARG_GOLD)
      );
    });

    m.put(CommandType.CHANGE, in -> {
      List<String> a = in.getArgs();
      InputUtil.requireArgs(a, CommandType.CHANGE.minArgs(), CommandType.CHANGE);

      BigDecimal eq = InputUtil.requirePercent(a, IDX_EQUITY, CommandType.CHANGE, ARG_EQUITY_RATE);
      BigDecimal de = InputUtil.requirePercent(a, IDX_DEBT, CommandType.CHANGE, ARG_DEBT_RATE);
      BigDecimal go = InputUtil.requirePercent(a, IDX_GOLD, CommandType.CHANGE, ARG_GOLD_RATE);
      Month month = InputUtil.requireMonth(a, IDX_CHANGE_MONTH, CommandType.CHANGE);

      app.change(month, eq, de, go);
    });

    m.put(CommandType.BALANCE, in -> {
      List<String> a = in.getArgs();
      InputUtil.requireArgs(a, CommandType.BALANCE.minArgs(), CommandType.BALANCE);

      Month month = InputUtil.requireMonth(a, IDX_BALANCE_MONTH, CommandType.BALANCE);
      PortfolioSnapshot snap = app.balance(month);

      System.out.println(
          snap.getEquity() + " " + snap.getDebt() + " " + snap.getGold()
      );
    });


    m.put(CommandType.REBALANCE, in -> {
      PortfolioSnapshot reb = app.rebalanceOrNull();

      System.out.println(
          reb == null
              ? "CANNOT_REBALANCE"
              : reb.getEquity() + " " + reb.getDebt() + " " + reb.getGold()
      );
    });

    return m;
  }
}
