package com.xliii.aoc.util;

import com.xliii.aoc.util.log.Logger;

import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

public class Interactive {

    private static final List<String> QUIT = List.of("q", "quit");

    private static final Logger log = new Logger();

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

    public static void directions(Consumer<Direction> handler) {
        run(input -> {
            var direction = Direction.byAlias(input);
            if (direction.isEmpty()) {
                log.error("Invalid direction: " + input);
                return;
            }

            handler.accept(direction.get());
        });
    }
}
