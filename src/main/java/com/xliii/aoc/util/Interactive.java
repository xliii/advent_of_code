package com.xliii.aoc.util;

import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

public class Interactive {

    private static final List<String> QUIT = List.of("q", "quit");

    public static void run(Consumer<String> handler) {
        var in = new Scanner(System.in);
        while (in.hasNext()) {
            String next = in.next();
            if (QUIT.contains(next)) {
                System.exit(0);
            }

            handler.accept(next);
        }
    }
}
