package com.example.geektrust.app;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.geektrust.cli.CommandType;
import com.example.geektrust.cli.LineParser;
import com.example.geektrust.cli.ParsedInput;
import com.example.geektrust.dispatch.Dispatcher;
import com.example.geektrust.dispatch.Handler;
import java.io.File;
import java.util.Arrays;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import org.junit.jupiter.api.Test;

class AppRunnerTest {

  @Test
  void dispatchesEachParsedLine() throws Exception {
    List<ParsedInput> received = new ArrayList<>();
    Handler recorder = in -> {
      received.add(in);
    };

    EnumMap<CommandType, Handler> handlers = new EnumMap<>(CommandType.class);
    handlers.put(CommandType.ALLOCATE, recorder);
    handlers.put(CommandType.SIP, recorder);

    Dispatcher dispatcher = new Dispatcher(handlers);
    LineParser parser = new LineParser();

    File temp = File.createTempFile("runner", ".txt");
    Files.write(temp.toPath(), Arrays.asList("ALLOCATE 10 20 30", "SIP 1 2 3"));
    try {
      new AppRunner(dispatcher, parser).run(temp.getAbsolutePath());
      assertEquals(2, received.size());
      assertEquals(CommandType.ALLOCATE, received.get(0).getCommandType());
      assertEquals(CommandType.SIP, received.get(1).getCommandType());
    } finally {
      temp.delete();
    }
  }
}

