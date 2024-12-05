package com.xliii.aoc.aoc2024;

import com.xliii.aoc.Puzzle;

public class Day6 extends Puzzle {

    @Override
    protected boolean useExample() {
        return true;
    }

    @Override
    protected void run() {
        solve1();
    }

    private void solve1() {
        System.out.println(getInput());
    }

    public static void main(String[] args) {
        Day6 puzzle = new Day6();
        puzzle.run();
    }
}
