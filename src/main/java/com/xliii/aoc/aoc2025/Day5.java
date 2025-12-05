package com.xliii.aoc.aoc2025;

import com.xliii.aoc.Puzzle;
import com.xliii.aoc.util.Color;
import com.xliii.aoc.util.Direction;
import com.xliii.aoc.util.grid.Cell;
import com.xliii.aoc.util.grid.Grid;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Day5 extends Puzzle {

    @Override
    protected void run() {
        solve1();
        //solve2();
    }

    private void solve1() {
        List<String> input = getInput();
        Ranges ranges = new Ranges(new ArrayList<>());
        int counter = 0;
        for (String line : input) {
            if (line.contains("-")) {
                String[] parts = line.split("-");
                long from = Long.parseLong(parts[0]);
                long to = Long.parseLong(parts[1]);
                ranges.ranges.add(new Range(from, to));
            } else if (!line.isBlank()) {
                long num = Long.parseLong(line);
                if (ranges.includes(num)) {
                    //System.out.println(num);
                    counter++;
                }
            }
        }
        System.out.println(counter);

        ranges.combineRanges();
        System.out.println(ranges);
    }

    record Ranges(List<Range> ranges) {
        public boolean includes(long num) {
            return ranges.stream()
                    .map(r -> r.includes(num))
                    .filter(Boolean::booleanValue)
                    .findAny()
                    .orElse(false);
        }

        public void combineRanges() {
            boolean updated = false;
            do {

            } while (updated);
        }
    }

    record Range(long from, long to) {
        public boolean includes(long num) {
            return from <= num && num <= to;
        }

        public boolean overlaps(Range range) {
            return includes(range.from) || includes(range.to);
        }

        public Range combine(Range range) {
            return new Range(Math.min(from, range.from), Math.max(to, range.to));
        }
    }

    @Override
    protected boolean useExample() {
        return true;
    }

    public static void main(String[] args) {
        Day5 puzzle = new Day5();
        puzzle.run();
    }
}
