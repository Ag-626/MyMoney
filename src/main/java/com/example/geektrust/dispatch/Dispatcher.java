package com.example.geektrust.dispatch;

import com.example.geektrust.cli.CommandType;
import com.example.geektrust.cli.ParsedInput;
import java.util.EnumMap;
import java.util.Objects;

public final class Dispatcher {
  private final EnumMap<CommandType, Handler> handlers;

  public Dispatcher(EnumMap<CommandType, Handler> handlers){
    this.handlers = Objects.requireNonNull(handlers);
  }

  public void dispatch(ParsedInput input) throws Exception {
    if (input == null || input.isNoop()) return;

    Handler h = handlers.get(input.getCommandType());

    if(h == null) {
      throw new IllegalArgumentException("No handler registered for command: " + input.getCommandType());
    }
    h.handle(input);
  }

}
