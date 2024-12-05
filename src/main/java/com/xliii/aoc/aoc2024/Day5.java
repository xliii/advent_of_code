package com.xliii.aoc.aoc2024;

import com.xliii.aoc.Puzzle;
import com.xliii.aoc.aoc2024.util.Sets;

import java.util.*;
import java.util.stream.Collectors;

public class Day5 extends Puzzle {

    @Override
    protected boolean useExample() {
        return false;
    }

    @Override
    protected void run() {
        solve1();
    }

    private final Map<Integer, Set<Integer>> rules = new HashMap<>();

    private void solve1() {
        Map<Boolean, List<String>> collect = getInput().stream().collect(Collectors.partitioningBy(line -> line.contains("|")));

        //populate rules: value -> subsequent values
        for (String ordering : collect.get(true)) {
            String[] parts = ordering.split("\\|");
            Integer first = Integer.valueOf(parts[0]);
            Integer second = Integer.valueOf(parts[1]);

            if (!rules.containsKey(first)) {
                rules.put(first, new HashSet<>());
            }

            rules.get(first).add(second);
        }

        log.info(rules);

        long totalGood = 0;
        long totalBad = 0;

        for (String line : collect.get(false)) {
            if (line.isBlank()) {
                continue;
            }

            log.info(line);

            List<Integer> values = Arrays.stream(line.split(",")).map(Integer::valueOf).toList();
            if (isGood(values)) {
                totalGood += values.get(values.size() / 2);
            } else {
                values = new ArrayList<>(values);
                values.sort((a, b) -> {
                    Set<Integer> afterA = rules.getOrDefault(a, Collections.emptySet());
                    Set<Integer> afterB = rules.getOrDefault(b, Collections.emptySet());

                    if (afterB.contains(a)) {
                        return 1;
                    } else if (afterA.contains(b)) {
                        return -1;
                    } else return 0;
                });
                log.error(values);

                totalBad += values.get(values.size() / 2);
            }
        }

        log.success(totalGood);
        log.success(totalBad);
    }

    private boolean isGood(List<Integer> values) {
        Set<Integer> used = new HashSet<>();
        for (Integer value : values) {
            if (!rules.containsKey(value)) {
                used.add(value);
                continue;
            }

            Set<Integer> intersection = Sets.intersection(used, rules.get(value));
            if (!intersection.isEmpty()) {
                log.error(value + " should go before " + intersection);
                return false;
            }

            used.add(value);
        }
        return true;
    }

    public static void main(String[] args) {
        Day5 puzzle = new Day5();
        puzzle.run();
    }
}
