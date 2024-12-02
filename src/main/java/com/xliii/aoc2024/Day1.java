package com.xliii.aoc2024;

import java.util.*;

public class Day1 extends Puzzle {

    @Override
    protected boolean useExample() {
        return true;
    }

    @Override
    protected void run() {
        List<String[]> zippedList = getInput().stream()
                .map(entry -> entry.split("\\s+"))
                .toList();

        List<Integer> first = new ArrayList<>();
        List<Integer> second = new ArrayList<>();
        for (String[] line : zippedList) {
            first.add(Integer.valueOf(line[0]));
            second.add(Integer.valueOf(line[1]));
        }

        log.info(first);
        log.info(second);

        first.sort(Comparator.naturalOrder());
        second.sort(Comparator.naturalOrder());

        log.info(first);
        log.info(second);

        int sum = 0;

        for (int i = 0; i < first.size(); i++) {
            int diff = Math.abs(first.get(i) - second.get(i));
            log.info(diff);
            sum += diff;
        }

        log.success("result: " + sum);

        Map<Integer, Integer> frequencyMap = new HashMap<>();

        for (int number : second) {
            frequencyMap.put(number, frequencyMap.getOrDefault(number, 0) + 1);
        }

        log.info(frequencyMap);

        int similarity = 0;

        for (int number : first) {
            similarity += frequencyMap.getOrDefault(number, 0) * number;
        }

        log.success(similarity);
    }

    public static void main(String[] args) {
        Day1 day1 = new Day1();
        day1.run();
    }

}
