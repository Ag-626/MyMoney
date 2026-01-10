package com.example.geektrust.cli;

public enum CommandType {
  ALLOCATE(3),
  SIP(3),
  CHANGE(4),
  BALANCE(1),
  REBALANCE(0),
  NOOP(0);

  private final int minArgs;

  CommandType(int minArgs) {
    this.minArgs = minArgs;
  }

  public int minArgs() {
    return minArgs;
  }

  public static CommandType fromToken(String token) {
    if (token == null || token.trim().isEmpty())
      return NOOP;
    String t = token.trim().toUpperCase();
    try{
      return CommandType.valueOf(t.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Unknown command: " + token);
    }
  }
}
