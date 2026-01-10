package com.example.geektrust;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import org.junit.jupiter.api.Test;

public class MainTest {

  @Test
  void printsUsageWhenNoArgs() {
    PrintStream originalErr = System.err;
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    System.setErr(new PrintStream(baos));
    try {
      Main.main(new String[]{});
      assertTrue(baos.toString().contains("Usage: java -jar"));
    } finally {
      System.setErr(originalErr);
    }
  }

  @Test
  void runsWithEmptyInputFile() throws Exception {
    File temp = File.createTempFile("mymoney", ".txt");
    temp.deleteOnExit();

    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(new ByteArrayOutputStream()));
    try {
      assertDoesNotThrow(() -> Main.main(new String[]{temp.getAbsolutePath()}));
    } finally {
      System.setOut(originalOut);
    }
  }
}