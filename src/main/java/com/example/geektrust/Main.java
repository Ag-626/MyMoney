package com.example.geektrust;

import com.example.geektrust.app.AppRunner;
import com.example.geektrust.app.ApplicationBootstrap;

public class Main {

    public static final int INPUT_FILE_PATH = 0;
    public static void main(String[] args) {
        if(args.length<1){
            System.err.println("Usage: java -jar <jarfile> <input-file>");
            return;
        }

        try {
            AppRunner appRunner = ApplicationBootstrap.createRunner();
            appRunner.run(args[INPUT_FILE_PATH]);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
