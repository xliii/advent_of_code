package com.xliii.aoc.aoc2024;

import com.xliii.aoc.Puzzle;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//TODO: Memoization tools
public class Day11 extends Puzzle {

    @Override
    protected boolean useExample() {
        return false;
    }

    @Override
    protected void run() {
        solve1();
        solve2();
    }

    private record Entry(Long stone, int depth) {}

    Map<Entry, Long> cache = new ConcurrentHashMap<>();

    private void solve2() {
        int steps = 75;
        long sum = transformedInput().stream()
                .map(stone -> memoizedCountStone(stone, steps))
                .reduce(Long::sum)
                .orElseThrow();

        log.success(sum);
    }

    private void solve1() {
        int steps = 25;
        long sum = transformedInput().stream()
                .map(stone -> memoizedCountStone(stone, steps))
                .reduce(Long::sum)
                .orElseThrow();

        log.success(sum);
    }

    private long memoizedCountStone(long stone, int depth) {
        Entry entry = new Entry(stone, depth);
        if (!cache.containsKey(entry)) {
            cache.put(entry, countStone(entry.stone, entry.depth));
        }
        return cache.get(entry);
    }


    private long countStone(long stone, int depth) {
        System.out.println(stone + "@" + depth);
        if (depth == 0) {
            return 1;
        }

        if (stone == 0) {
            return memoizedCountStone(1, depth - 1);
        } else if (String.valueOf(stone).length() % 2 == 0) {
            String str = String.valueOf(stone);
            long left = Long.parseLong(str.substring(0, str.length() / 2));
            long right = Long.parseLong(str.substring(str.length() / 2));
            return memoizedCountStone(left, depth - 1) + countStone(right, depth - 1);
        } else {
            return memoizedCountStone(stone * 2024, depth - 1);
        }
    }

    private List<Integer> transformedInput() {
        return Arrays.stream(getInput().getFirst().split(" ")).map(Integer::valueOf).toList();
    }

    public static void main(String[] ignoredArgs) {
        Day11 puzzle = new Day11();
        puzzle.run();
    }
}
