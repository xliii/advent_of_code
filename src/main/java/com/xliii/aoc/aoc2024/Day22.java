package com.xliii.aoc.aoc2024;

import com.xliii.aoc.Puzzle;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Day22 extends Puzzle {

    private static final long ITERATIONS = 2000;

    @Override
    protected boolean useExample() {
        return false;
    }

    @Override
    protected void run() {
        //solve1();
        solve2();
    }

    private long nextSecret(long secret) {
        //p1
        var result = secret * 64;
        secret = mix(result, secret);
        secret = prune(secret);
        //p2
        result = secret / 32;
        secret = mix(result, secret);
        secret = prune(secret);
        //p3
        result = secret * 2048;
        secret = mix(result, secret);
        secret = prune(secret);

        return secret;
    }

    private long prune(long secret) {
        return secret % 16777216;
    }

    private long mix(long secret, long result) {
        return secret ^ result;
    }

    private final Map<String, Long> SEQUENCE_PRICE_MAP = new ConcurrentHashMap<>();

    private void solve2() {
        for (var line : getInput()) {
            List<Long> changes = new ArrayList<>();
            long secret = Long.parseLong(line);
            var price = secret % 10;
            //log.error(secret + " -> " + price);
            Set<String> used = new HashSet<>();
            for (var i = 0; i < ITERATIONS; i++) {
                secret = nextSecret(secret);
                var newPrice = secret % 10;
                var change = newPrice - price;
                price = newPrice;
                //log.warn(secret + " -> " + price + "(" + change + ")");
                changes.add(change);
                if (changes.size() >= 4) {
                    String changeSequence = changes.subList(changes.size() - 4, changes.size()).stream().map(String::valueOf).collect(Collectors.joining());
                    if (!used.contains(changeSequence)) {
                        used.add(changeSequence);
                        SEQUENCE_PRICE_MAP.merge(changeSequence, price, Long::sum);
                    }
                }
            }
        }

        //log.info(SEQUENCE_PRICE_MAP);
        Map.Entry<String, Long> max = SEQUENCE_PRICE_MAP.entrySet().stream().max(Comparator.comparingLong(Map.Entry::getValue)).orElseThrow();
        log.success(max);
    }

    private void solve1() {
        long sum = 0;
        for (var line : getInput()) {
            long secret = Long.parseLong(line);
            for (var i = 0; i < ITERATIONS; i++) {
                secret = nextSecret(secret);
            }

            sum += secret;
            //log.warn(secret);
        }

        log.success(sum);
    }

    public static void main(String[] ignoredArgs) {
        Day22 puzzle = new Day22();
        puzzle.run();
    }
}
