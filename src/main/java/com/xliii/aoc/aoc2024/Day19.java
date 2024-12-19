package com.xliii.aoc.aoc2024;

import com.xliii.aoc.Puzzle;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day19 extends Puzzle {

    @Override
    protected boolean useExample() {
        return false;
    }

    @Override
    protected void run() {
        solve1();
        solve2();
    }

    private void solve2() {
        var input = getInput();

        List<String> parts = Arrays.asList(input.getFirst().split(", "));

        log.info(parts);

        long total = 0;

        for (int i = 2; i < input.size(); i++) {
            String pattern = input.get(i);
            long solutions = memoizedSolutions(pattern, parts);
            if (solutions > 0) {
                log.success(pattern + ": " + solutions + " solutions");
                total += solutions;
            } else {
                log.error(pattern + ": No solutions");
            }
        }
        log.info();

        log.success(total);
    }

    private void solve1() {
        var input = getInput();

        List<String> parts = Arrays.asList(input.getFirst().split(", "));

        log.info(parts);

        long total = 0;

        for (int i = 2; i < input.size(); i++) {
            String pattern = input.get(i);
            boolean solveable = solveable(pattern, parts);
            if (solveable) {
                log.success(pattern + ": Solvable ");
                total++;
            } else {
                log.error(pattern + ": Not solvable");
            }
        }
        log.info();

        log.success(total);
    }

    private final Map<String, Long> cache = new HashMap<>();

    private long memoizedSolutions(String pattern, List<String> parts) {
        if (!cache.containsKey(pattern)) {
            cache.put(pattern, solutions(pattern, parts));
        }

        return cache.get(pattern);
    }

    private long solutions(String pattern, List<String> parts) {
        if (pattern.isEmpty()) {
            return 1;
        }

        long solutions = 0;

        for (var part : parts) {
            if (pattern.startsWith(part)) {
                solutions += memoizedSolutions(pattern.substring(part.length()), parts);
            }
        }

        return solutions;
    }

    private boolean solveable(String pattern, List<String> parts) {
        if (pattern.isEmpty()) {
            return true;
        }

        for (var part : parts) {
            if (pattern.startsWith(part)) {
                if (solveable(pattern.substring(part.length()), parts)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static void main(String[] ignoredArgs) {
        Day19 puzzle = new Day19();
        puzzle.run();
    }
}
