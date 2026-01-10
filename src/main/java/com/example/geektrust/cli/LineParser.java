package com.example.geektrust.cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public final class LineParser {

  public ParsedInput parse(String line) {
    if(line == null || line.trim().isEmpty()) {
      return ParsedInput.noop();
    }

    Iterator<String> it = Arrays.asList(line.trim().split("\\s+")).iterator();

    if(!it.hasNext())
      return ParsedInput.noop();

    CommandType cmd = CommandType.fromToken(it.next());

    List<String> args = new ArrayList<>();
    it.forEachRemaining(args::add);

    return new ParsedInput(cmd, args);
  }
}
