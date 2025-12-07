package com.xliii.aoc.aoc2025;

import com.xliii.aoc.Puzzle;
import com.xliii.aoc.util.Color;
import com.xliii.aoc.util.Direction;
import com.xliii.aoc.util.grid.Cell;
import com.xliii.aoc.util.grid.Grid;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Day7 extends Puzzle {

    @Override
    protected void run() {
        solve1();
        //solve2();
    }

    private static final Character START = 'S';
    private static final Character EMPTY = '.';
    private static final Character SPLITTER = '^';
    private static final Character BEAM = '|';

    private static final Map<Character, Color> COLOR_MAP = Map.of(
            START, Color.YELLOW,
            EMPTY, Color.BLACK,
            SPLITTER, Color.RED,
            BEAM, Color.GREEN
    );

    private void solve1() {
        Grid<Character> grid = Grid.create(getInput(), COLOR_MAP);
        System.out.println(grid);

        Cell<Character> start = grid.findOne(START).orElseThrow();
        grid.put(start.x(), start.y() + 1, BEAM);

        System.out.println(grid);

        int splits = 0;

        for (int level = 1; level < grid.getHeight() - 1; level++) {
            List<Cell<Character>> beams = grid.findAll(BEAM);
            for (Cell<Character> beam : beams) {
                if (beam.y() != level) {
                    continue;
                }

                Cell<Character> below = grid.neighbor(beam.x(), beam.y(), Direction.SOUTH);
                if (below.value().equals(EMPTY)) {
                    grid.put(below.x(), below.y(), BEAM);
                } else if (below.value().equals(SPLITTER)) {
                    splits++;
                    grid.put(below.x() - 1, below.y(), BEAM);
                    grid.put(below.x() + 1, below.y(), BEAM);
                }

            }

            System.out.println(grid);
            System.out.println(splits);
        }
    }

    @Override
    protected boolean useExample() {
        return false;
    }

    public static void main(String[] args) {
        Day7 puzzle = new Day7();
        puzzle.run();
    }
}
