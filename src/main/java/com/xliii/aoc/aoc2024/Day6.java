package com.xliii.aoc.aoc2024;

import com.xliii.aoc.Puzzle;
import com.xliii.aoc.aoc2024.util.Direction;
import com.xliii.aoc.aoc2024.util.grid.Cell;
import com.xliii.aoc.aoc2024.util.grid.Grid;
import com.xliii.aoc.aoc2024.util.grid.GridException;

public class Day6 extends Puzzle {

    @Override
    protected boolean useExample() {
        return false;
    }

    @Override
    protected void run() {
        solve1();
    }

    private static final Character USED = 'X';
    private static final Character EMPTY = '.';
    private static final Character WALL = '#';

    private void solve1() {
        Grid<Character> grid = Grid.create(getInput());

        Direction direction = Direction.NORTH;

        log.info(grid);

        Cell<Character> guard = grid.findOne(direction.sign());
        int x = guard.x();
        int y = guard.y();

        Cell<Character> next = grid.neighbor(x, y, direction);
        while (true) {
            try {
                if (next.value().equals(EMPTY) || next.value().equals(USED)) {
                    log.info("Move " + direction);
                    grid.put(x, y, USED);
                    grid.put(next.x(), next.y(), direction.sign());
                    //log.info(grid);

                    x = next.x();
                    y = next.y();
                    next = grid.neighbor(x, y, direction);
                } else if (next.value().equals(WALL)) {
                    //rotate CW
                    direction = direction.rotate90();
                    next = grid.neighbor(x, y, direction);
                }
            } catch (GridException e) {
                grid.put(x, y, USED);
                break;
            }
        }

        log.info(grid);
        log.success(grid.count(USED));
    }

    public static void main(String[] args) {
        Day6 puzzle = new Day6();
        puzzle.run();
    }
}
