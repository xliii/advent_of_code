package com.xliii.aoc.aoc2024;

import com.xliii.aoc.Puzzle;

public class Day22 extends Puzzle {

    private static final long ITERATIONS = 2000;

    @Override
    protected boolean useExample() {
        return false;
    }

    @Override
    protected void run() {
        solve1();
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

    private void solve2() {

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
