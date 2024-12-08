package com.xliii.aoc.aoc2024;

import com.xliii.aoc.Puzzle;

public class DayTemplate extends Puzzle {

    @Override
    protected boolean useExample() {
        return true;
    }

    @Override
    protected void run() {
        solve1();
        solve2();
    }

    private void solve2() {

    }

    private void solve1() {

    }

    public static void main(String[] ignoredArgs) {
        DayTemplate puzzle = new DayTemplate();
        puzzle.run();
    }
}
