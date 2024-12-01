package com.xliii.aoc2024;

import java.util.*;

public class Day1 extends Puzzle {

    public static void main(String[] args) {
        Day1 day1 = new Day1();
        List<String[]> zippedList = day1.getInput().stream()
                .map(entry -> entry.split("\\s+"))
                .toList();

        List<Integer> first = new ArrayList<>();
        List<Integer> second = new ArrayList<>();
        for (String[] line : zippedList) {
            first.add(Integer.valueOf(line[0]));
            second.add(Integer.valueOf(line[1]));
        }

        System.out.println(first);
        System.out.println(second);

        first.sort(Comparator.naturalOrder());
        second.sort(Comparator.naturalOrder());

        System.out.println(first);
        System.out.println(second);

        int sum = 0;

        for (int i = 0; i < first.size(); i++) {
            int diff = Math.abs(first.get(i) - second.get(i));
            System.out.println(diff);
            sum += diff;
        }

        System.out.println("result: " + sum);

        Map<Integer, Integer> frequencyMap = new HashMap<>();

        for (int number : second) {
            frequencyMap.put(number, frequencyMap.getOrDefault(number, 0) + 1);
        }

        System.out.println(frequencyMap);

        int similarity = 0;

        for (int number : first) {
            similarity += frequencyMap.getOrDefault(number, 0) * number;
        }

        System.out.println(similarity);
    }

}
