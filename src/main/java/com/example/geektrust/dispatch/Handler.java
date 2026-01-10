package com.example.geektrust.dispatch;

import com.example.geektrust.cli.ParsedInput;

@FunctionalInterface
public interface Handler {
  void handle(ParsedInput input) throws Exception;
}
