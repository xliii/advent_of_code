package com.xliii.aoc.aoc2025;

import com.xliii.aoc.Puzzle;
import com.xliii.aoc.util.Color;
import com.xliii.aoc.util.Direction;
import com.xliii.aoc.util.grid.Cell;
import com.xliii.aoc.util.grid.Grid;

import java.util.Map;

public class Day4 extends Puzzle {

    @Override
    protected void run() {
        solve1();
        //solve2();
    }

    private static final Character EMPTY = '.';
    private static final Character PAPER = '@';
    private static final Character MARKED = 'x';

    private static final Map<Character, Color> COLORS = Map.of(
        EMPTY, Color.GREEN,
        PAPER, Color.WHITE,
        MARKED, Color.RED
    );

    private void solve1() {
        int total = 0;
        Grid<Character> grid = Grid.create(getInput(), COLORS);
        int removed = 1;
        while (removed > 0) {
            removed = 0;
            for (int x = 0; x < grid.getWidth(); x++) {
                for (int y = 0; y < grid.getHeight(); y++) {
                    if (grid.get(x, y).equals(EMPTY) || grid.get(x, y).equals(MARKED)) {
                        continue;
                    }

                    int adjacent = 0;
                    for (Cell<Character> neighbor : grid.neighbors(x, y, Direction.ALL)) {
                        if (neighbor.value().equals(PAPER) || neighbor.value().equals(MARKED)) {
                            adjacent++;
                        }

                        if (adjacent >= 4) {
                            break;
                        }
                    }

                    if (adjacent < 4) {
                        total++;
                        removed++;
                        grid.put(x, y, MARKED);
                    }

                }
            }
            System.out.println(grid);
            for (Cell<Character> cell : grid) {
                if (cell.value().equals(MARKED)) {
                    grid.put(cell.x(), cell.y(), EMPTY);
                }
            }
        }


        System.out.println(total);
    }

    private void solve2() {

    }

    @Override
    protected boolean useExample() {
        return false;
    }

    public static void main(String[] args) {
        Day4 puzzle = new Day4();
        puzzle.run();
    }
}
