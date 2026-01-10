package com.example.geektrust.dispatch;

import com.example.geektrust.cli.CommandType;
import com.example.geektrust.domain.Month;
import java.math.BigDecimal;
import java.util.List;

public final class InputUtil {
  private InputUtil() {}

  public static void requireArgs(List<String> args, int count, CommandType commandType){
    if(args == null || args.size() < count) {
      throw new IllegalArgumentException(commandType + " requires " + count + " arguments");
    }
  }

  public  static long requireLong(List<String> args, int idx, CommandType commandType, String name) {
    if(args.size() <=idx) throw new IllegalArgumentException(commandType + " requires " + name);
    String raw = args.get(idx);
    try {
      return Long.parseLong(raw);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException(commandType + " invalid " + name + ": " + raw);
    }
  }

  public static BigDecimal requirePercent(List<String> args, int idx, CommandType commandType, String name) {
    if (args.size() <= idx) throw new IllegalArgumentException(commandType + " requires " + name);
    String raw = args.get(idx).trim();
    if(!raw.endsWith("%"))
      throw new IllegalArgumentException(commandType + " invalid " + name + ": " + raw);

    String number = raw.substring(0, raw.length() -1);
    try {
      return new BigDecimal(number);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException(commandType + " invalid " + name + ": " + raw);
    }
  }

  public static Month requireMonth(List<String> args, int idx, CommandType commandType) {
    if (args.size() <=idx) throw new IllegalArgumentException(commandType + " requires MONTH");
    return Month.from(args.get(idx));
  }

}
