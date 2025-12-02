package com.xliii.aoc.aoc2025;

import com.xliii.aoc.Puzzle;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class Day2 extends Puzzle {

    @Override
    protected void run() {
        //solve1();
        solve2();
    }

    private void solve1() {
        List<String> input = getInput().stream().flatMap(line -> Stream.of(line.split(","))).toList();
        long invalidSum = 0;
        for (String range : input) {
            String[] parts = range.split("-");
            long from = Long.parseLong(parts[0]);
            long to = Long.parseLong(parts[1]);

            invalidSum += invalidSumInRange(from, to);
        }

        System.out.println("Output: " + invalidSum);
    }

    private long invalidSumInRange(long from, long to) {
        long sum = 0;
        for (long i = from; i <= to; i++) {
            if (isInvalid(i)) {
                System.out.println(i);
                sum += i;
            }
        }

        return sum;
    }

    private long invalidSumInRange2(long from, long to) {
        long sum = 0;
        for (long i = from; i <= to; i++) {
            if (isInvalid2(i)) {
                System.out.println("Invalid: " + i);
                sum += i;
            }
        }

        return sum;
    }

    private boolean isInvalid(long num) {
        String str = String.valueOf(num);
        int length = str.length();
        if (length % 2 == 1) return false;

        return str.substring(0, length / 2).equals(str.substring(length / 2, length));
    }

    private boolean isInvalid2(long num) {
        String str = String.valueOf(num);
        for (int i = 1; i <= 5 && i < str.length(); i++) {
            if (isInvalidForSequenceSize(str, i)) {
                return true;
            }
        }

        return false;
    }

    private boolean isInvalidForSequenceSize(String str, int size) {
        int length = str.length();
        if (length % size != 0) return false;
        int partsNum = length / size;
        Set<String> parts = new HashSet<>();

        //System.out.println("Checking " + str + " for " + size);

        for (int i = 0; i < partsNum; i++) {
            String part = str.substring(i * size, (i + 1) * size);
            //System.out.println(part);
            parts.add(part);
            if (parts.size() > 1) return false;
        }

        return true;
    }

    private void solve2() {
        List<String> input = getInput().stream().flatMap(line -> Stream.of(line.split(","))).toList();
        long invalidSum = 0;
        for (String range : input) {
            String[] parts = range.split("-");
            long from = Long.parseLong(parts[0]);
            long to = Long.parseLong(parts[1]);

            invalidSum += invalidSumInRange2(from, to);
        }

        System.out.println("Output: " + invalidSum);
    }

    @Override
    protected boolean useExample() {
        return false;
    }

    public static void main(String[] args) {
        Day2 puzzle = new Day2();
        puzzle.run();
    }
}
