package com.example.geektrust.dispatch;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.geektrust.cli.CommandType;
import com.example.geektrust.cli.ParsedInput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import org.junit.jupiter.api.Test;

class DispatcherTest {

  @Test
  void skipsNullOrNoopInputs() throws Exception {
    EnumMap<CommandType, Handler> handlers = new EnumMap<>(CommandType.class);
    List<ParsedInput> received = new ArrayList<>();
    handlers.put(
        CommandType.ALLOCATE,
        in -> {
          received.add(in);
        });
    Dispatcher dispatcher = new Dispatcher(handlers);

    dispatcher.dispatch(null);
    dispatcher.dispatch(ParsedInput.noop());

    assertTrue(received.isEmpty());
  }

  @Test
  void throwsWhenHandlerMissing() {
    EnumMap<CommandType, Handler> handlers = new EnumMap<>(CommandType.class);
    Dispatcher dispatcher = new Dispatcher(handlers);
    ParsedInput sip = new ParsedInput(CommandType.SIP, Arrays.asList("1", "2", "3"));

    assertThrows(IllegalArgumentException.class, () -> dispatcher.dispatch(sip));
  }

  @Test
  void invokesRegisteredHandler() throws Exception {
    EnumMap<CommandType, Handler> handlers = new EnumMap<>(CommandType.class);
    boolean[] called = {false};
    handlers.put(CommandType.ALLOCATE, in -> called[0] = true);
    Dispatcher dispatcher = new Dispatcher(handlers);

    dispatcher.dispatch(new ParsedInput(CommandType.ALLOCATE, Arrays.asList("10", "20", "30")));

    assertTrue(called[0]);
  }
}

