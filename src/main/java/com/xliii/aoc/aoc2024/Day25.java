package com.xliii.aoc.aoc2024;

import com.xliii.aoc.Puzzle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day25 extends Puzzle {

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

    }

    private boolean match(List<Integer> key, List<Integer> lock) {
        for (int i = 0; i < key.size(); i++) {
            if (key.get(i) + lock.get(i) > 5) {
                return false;
            }
        }
        return true;
    }

    private void solve1() {
        init();
        var matches = 0;
        for (List<Integer> key : KEYS) {
            for (List<Integer> lock : LOCKS) {
                if (match(key, lock)) {
                    matches++;
                }
            }
        }
        log.success(matches);
    }

    private static final int KEY_SIZE = 7;

    private final List<List<Integer>> KEYS = new ArrayList<>();
    private final List<List<Integer>> LOCKS = new ArrayList<>();

    private void processEntry(List<String> entry) {
        log.warn(entry);
        var isLock = entry.getFirst().equals("#####");

        if (isLock) {
            int[] lock = new int[entry.size() - 2];

            for (int i = 1; i < entry.size(); i++) {
                var line = entry.get(i);
                var index = line.indexOf("#");
                while (index >= 0) {
                    lock[index]++;
                    index = line.indexOf("#", index + 1);
                }
            }
            LOCKS.add(Arrays.stream(lock).boxed().toList());
            log.success("Lock: " + LOCKS.getLast());
        } else {
            int[] key = new int[entry.size() - 2];

            for (int i = entry.size() - 2; i > 0; i--) {
                var line = entry.get(i);
                var index = line.indexOf("#");
                while (index >= 0) {
                    key[index]++;
                    index = line.indexOf("#", index + 1);
                }
            }
            KEYS.add(Arrays.stream(key).boxed().toList());
            log.success("Key: " + KEYS.getLast());
        }
    }

    private void init() {
        var it = getInput().iterator();
        while (it.hasNext()) {
            List<String> entry = new ArrayList<>();
            for (int i = 0; i < KEY_SIZE; i++) {
                entry.add(it.next());
            }

            processEntry(entry);

            if (it.hasNext()) {
                it.next();
            }
        }
    }

    public static void main(String[] ignoredArgs) {
        Day25 puzzle = new Day25();
        puzzle.run();
    }
}
