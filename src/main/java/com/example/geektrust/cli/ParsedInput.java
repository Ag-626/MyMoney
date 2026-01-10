package com.example.geektrust.cli;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class ParsedInput {

  private final CommandType commandType;
  private final List<String> args;

  public ParsedInput(CommandType commandType, List<String> args) {
    this.commandType = Objects.requireNonNull(commandType);
    this.args = (args == null) ? Collections.emptyList() : Collections.unmodifiableList(new ArrayList<>(args));
  }

  public static ParsedInput noop() {
    return new ParsedInput(CommandType.NOOP, Collections.emptyList());
  }

  public boolean isNoop() {
    return commandType == CommandType.NOOP;
  }

  public CommandType getCommandType() {
    return commandType;
  }

  public List<String> getArgs() {
    return args;
  }
}
