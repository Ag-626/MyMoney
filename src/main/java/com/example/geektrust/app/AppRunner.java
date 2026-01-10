package com.example.geektrust.app;

import com.example.geektrust.cli.LineParser;
import com.example.geektrust.cli.ParsedInput;
import com.example.geektrust.dispatch.Dispatcher;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Objects;

public final class AppRunner {

  private final Dispatcher dispatcher;
  private final LineParser lineParser;

  public AppRunner(Dispatcher dispatcher, LineParser parser) {
    this.dispatcher = Objects.requireNonNull(dispatcher);
    this.lineParser = Objects.requireNonNull(parser);
  }

  public void run(String inputFilePath) throws Exception {
    try (BufferedReader br = new BufferedReader((new FileReader(inputFilePath)))) {
      String line;
      while ((line = br.readLine()) != null) {
        ParsedInput input = lineParser.parse(line);
        dispatcher.dispatch(input);
      }
    }
  }

}
