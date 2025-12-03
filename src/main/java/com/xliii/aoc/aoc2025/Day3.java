package com.xliii.aoc.aoc2025;

import com.xliii.aoc.Puzzle;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day3 extends Puzzle {

    @Override
    protected void run() {
        solve1();
        //solve2();
    }

    private void solve1() {
        System.out.println(getInput().stream().mapToLong(this::power).sum());
    }

    private Long power(String line) {
        return power(line, 12);
    }

    private record IndexValue(int index, int value){}

    private Long power(String line, int size) {
        //Take N last digits
        //Move digits to the left starting with the leftmost digit up to max value possible
        List<Integer> list = line.chars().boxed().map(i -> i - 48).toList();
        List<Integer> selected = new ArrayList<>(IntStream.range(list.size() - size, list.size()).boxed().toList());
        int min = 0;
        for (int i = 0; i < selected.size(); i++) {
            Integer currentIndex = selected.get(i);
            IndexValue candidate = new IndexValue(currentIndex, list.get(currentIndex));
            for (int j = currentIndex - 1; j >= min; j--) {
                Integer proposedValue = list.get(j);
                if (proposedValue >= candidate.value && !selected.contains(j)) {
                    candidate = new IndexValue(j, proposedValue);
                }
            }

            min = candidate.index;
            selected.set(i, candidate.index);
        }
        String number = selected.stream().map(i -> String.valueOf(line.charAt(i))).collect(Collectors.joining());
        return Long.parseLong(number);
    }

    private void solve2() {

    }

    @Override
    protected boolean useExample() {
        return false;
    }

    public static void main(String[] args) {
        Day3 puzzle = new Day3();
        puzzle.run();
    }
}
