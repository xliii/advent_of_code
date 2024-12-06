package com.xliii.aoc.aoc2024;

import com.xliii.aoc.Puzzle;
import com.xliii.aoc.aoc2024.util.Direction;
import com.xliii.aoc.aoc2024.util.grid.Cell;
import com.xliii.aoc.aoc2024.util.grid.Grid;
import com.xliii.aoc.aoc2024.util.grid.GridException;

import java.util.HashSet;
import java.util.Set;

public class Day6 extends Puzzle {

    @Override
    protected boolean useExample() {
        return false;
    }

    @Override
    protected void run() {
        //solve1();
        solve2();
    }

    private static final Character USED = 'X';
    private static final Character EMPTY = '.';
    private static final Character WALL = '#';

    private record Point(int x, int y) {}

    private record PointWithDirection(int x, int y, Direction direction) {}

    private void solve2() {
        Grid<Character> grid = Grid.create(getInput());

        Direction direction = Direction.NORTH;

        log.info(grid);

        Cell<Character> guard = grid.findOne(direction.sign());
        int x = guard.x();
        int y = guard.y();
        Set<PointWithDirection> used = new HashSet<>();
        used.add(new PointWithDirection(x, y, direction));

        Set<Point> potentialBlocks = new HashSet<>();

        Cell<Character> next = grid.neighbor(x, y, direction);

        while (true) {
            try {
                if (next.value().equals(WALL)) {
                    //rotate CW
                    direction = direction.rotate90();
                    next = grid.neighbor(x, y, direction);
                } else {
                    //try blocking next and checking loop
                    if (!potentialBlocks.contains(new Point(x, y))) {
                        log.info("Checking potential block @ " + x + ":" + y);
                        Set<PointWithDirection> usedCopy = new HashSet<>(used);
                        Grid<Character> gridCopy = grid.copy();
                        gridCopy.put(next.x(), next.y(), WALL);
                        if (isLoop(gridCopy, x, y, direction, usedCopy)) {
                            potentialBlocks.add(new Point(x,y));
                        }
                    }


                    //log.info("Move " + direction);
                    grid.put(x, y, USED);
                    grid.put(next.x(), next.y(), direction.sign());
                    //log.info(grid);

                    x = next.x();
                    y = next.y();

                    used.add(new PointWithDirection(x, y, direction));
                    next = grid.neighbor(x, y, direction);
                }
            } catch (GridException e) {
                grid.put(x, y, USED);
                break;
            }
        }

        log.info(grid);

        log.success(potentialBlocks);
        log.success(potentialBlocks.size());
    }

    private boolean isLoop(Grid<Character> grid, int x, int y, Direction direction, Set<PointWithDirection> used) {
        Cell<Character> next = grid.neighbor(x, y, direction);
        while(true) {
            try {
                if (next.value().equals(WALL)) {
                    //rotate CW
                    direction = direction.rotate90();
                    next = grid.neighbor(x, y, direction);
                } else {
                    //log.info("Move " + direction);
                    grid.put(x, y, USED);
                    grid.put(next.x(), next.y(), direction.sign());

                    //log.info(grid);

                    x = next.x();
                    y = next.y();
                    if (used.contains(new PointWithDirection(x, y, direction))) {
                        return true;
                    }

                    used.add(new PointWithDirection(x, y, direction));
                    next = grid.neighbor(x, y, direction);
                }
            } catch (GridException e) {
                grid.put(x, y, USED);
                return false;
            }
        }

    }

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
                if (next.value().equals(WALL)) {
                    //rotate CW
                    direction = direction.rotate90();
                    next = grid.neighbor(x, y, direction);
                } else {
                    log.info("Move " + direction);
                    grid.put(x, y, USED);
                    grid.put(next.x(), next.y(), direction.sign());
                    //log.info(grid);

                    x = next.x();
                    y = next.y();
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
