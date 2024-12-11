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
                .map(stone -> new Entry(stone, steps))
                .map(this::countStone)
                .reduce(Long::sum)
                .orElseThrow();

        log.success(sum);
    }

    private void solve1() {
        int steps = 25;
        long sum = transformedInput().stream()
                .map(stone -> new Entry(stone, steps))
                .map(this::countStone)
                .reduce(Long::sum)
                .orElseThrow();

        log.success(sum);
    }

    private long countStone(Entry entry) {
        if (entry.depth == 0) {
            return 1;
        }

        if (entry.stone == 0) {
            if (!cache.containsKey(entry)) {
                cache.put(entry, countStone(new Entry(1L, entry.depth - 1)));
            }
            return cache.get(entry);
        } else if (entry.stone.toString().length() % 2 == 0) {
            String str = entry.stone.toString();
            Long left = Long.valueOf(str.substring(0, str.length() / 2));
            Long right = Long.valueOf(str.substring(str.length() / 2));
            if (!cache.containsKey(entry)) {
                cache.put(entry,
                        countStone(new Entry(left, entry.depth - 1))
                      + countStone(new Entry(right, entry.depth - 1))
                );
            }
            return cache.get(entry);
        } else {
            if (!cache.containsKey(entry)) {
                cache.put(entry, countStone(new Entry(entry.stone * 2024, entry.depth - 1)));
            }
            return cache.get(entry);
        }
    }

    private List<Long> transformedInput() {
        return Arrays.stream(getInput().getFirst().split(" ")).map(Long::valueOf).toList();
    }

    public static void main(String[] ignoredArgs) {
        Day11 puzzle = new Day11();
        puzzle.run();
    }
}
