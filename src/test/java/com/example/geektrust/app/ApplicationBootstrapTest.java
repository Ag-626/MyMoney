package com.example.geektrust.app;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class ApplicationBootstrapTest {

  @Test
  void createRunnerBuildsRunnablePipeline() throws Exception {
    File temp = File.createTempFile("mymoney", ".txt");
    Files.write(
        temp.toPath(),
        Arrays.asList(
            "ALLOCATE 6000 3000 1000",
            "SIP 2000 1000 500",
            "CHANGE 4% 10% 2% JANUARY",
            "BALANCE JANUARY",
            "CHANGE -10% 40% 0% FEBRUARY",
            "BALANCE FEBRUARY"));

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(baos));
    try {
      AppRunner runner = ApplicationBootstrap.createRunner();
      runner.run(temp.getAbsolutePath());
      String[] lines = baos.toString().trim().split("\\R");
      assertArrayEquals(new String[] {"6240 3300 1020", "7416 6020 1520"}, lines);
    } finally {
      System.setOut(originalOut);
      temp.delete();
    }
  }
}

