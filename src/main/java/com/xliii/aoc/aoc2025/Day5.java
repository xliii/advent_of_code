package com.xliii.aoc.aoc2025;

import com.xliii.aoc.Puzzle;
import com.xliii.aoc.util.Color;
import com.xliii.aoc.util.Direction;
import com.xliii.aoc.util.grid.Cell;
import com.xliii.aoc.util.grid.Grid;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

        System.out.println(ranges.count());
    }

    record Ranges(List<Range> ranges) {
        public boolean includes(long num) {
            return ranges.stream()
                    .map(r -> r.includes(num))
                    .filter(Boolean::booleanValue)
                    .findAny()
                    .orElse(false);
        }

        public long count() {
            return ranges.stream().mapToLong(r -> r.to - r.from + 1).sum();
        }

        public void combineRanges() {
            boolean updated;
            do {
                updated = false;
                outer:
                for (int i = 0; i < ranges.size(); i++) {
                    Range range = ranges.get(i);
                    for (int j = i + 1; j < ranges.size(); j++) {
                        Range range2 = ranges.get(j);
                        if (range.overlaps(range2)) {
                            Range combined = range.combine(range2);
                            ranges.remove(range);
                            ranges.remove(range2);
                            ranges.add(combined);
                            System.out.println(range + " + " + range2 + " = " + combined);
                            updated = true;
                            break outer;
                        }
                    }
                }
            } while (updated);
        }

        @Override
        public String toString() {
            return ranges.stream().map(Range::toString).collect(Collectors.joining(System.lineSeparator()));
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

        @Override
        public String toString() {
            return "[" + from + "-" + to + "]";
        }
    }

    @Override
    protected boolean useExample() {
        return false;
    }

    public static void main(String[] args) {
        Day5 puzzle = new Day5();
        puzzle.run();
    }
}
