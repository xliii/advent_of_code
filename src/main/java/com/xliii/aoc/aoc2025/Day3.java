package com.xliii.aoc.aoc2025;

import com.xliii.aoc.Puzzle;

public class Day3 extends Puzzle {

    @Override
    protected void run() {
        solve1();
        //solve2();
    }

    private void solve1() {
        System.out.println(getInput().stream().mapToInt(this::power).sum());
    }

    private int power(String line) {
        char max = (char) line.chars().max().orElseThrow();
        int index = line.indexOf(max);
        if (index == line.length() - 1) {
            //Last, this is second digit, find max before it
            String beforeMax = line.substring(0, index);
            char max2 = (char) beforeMax.chars().max().orElseThrow();
            return Integer.parseInt(max2 + "" + max);
        } else {
            //Find second max digit after this one
            String afterMax = line.substring(index + 1);
            char max2 = (char) afterMax.chars().max().orElseThrow();
            return Integer.parseInt(max + "" + max2);
        }
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
