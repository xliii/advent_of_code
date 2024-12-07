package com.xliii.aoc.aoc2024;

import com.xliii.aoc.Puzzle;
import com.xliii.aoc.aoc2024.util.Color;
import com.xliii.aoc.aoc2024.util.Direction;
import com.xliii.aoc.aoc2024.util.grid.Cell;
import com.xliii.aoc.aoc2024.util.grid.Grid;
import com.xliii.aoc.aoc2024.util.grid.GridException;

import java.util.*;

public class Day6 extends Puzzle {

    @Override
    protected boolean useExample() {
        return false;
    }

    @Override
    protected void run() {
        solve1();
        solve2();
    }

    private static final Character USED = 'X';
    private static final Character USED_CHECK = 'x';
    private static final Character EMPTY = '.';
    private static final Character WALL = '#';
    private static final Character BLOCK = '0';
    private static final Character START = 'S';
    private static final Character LOOP = '*';

    private record Point(int x, int y) {
        @Override
        public String toString() {
            return "(" + x + ":" + y + ")";
        }
    }

    private record PointWithDirection(int x, int y, Direction direction) {}

    private Map<Character, Color> colorMap() {
        Map<Character, Color> colorMap = new HashMap<>();
        colorMap.put(EMPTY, Color.BLACK);
        colorMap.put(WALL, Color.BLUE);
        colorMap.put(USED, Color.GREEN);
        colorMap.put(USED_CHECK, Color.YELLOW);
        colorMap.put(BLOCK, Color.RED);
        colorMap.put(START, Color.RED);
        for (Direction dir : Direction.ORTHOGONAL) {
            colorMap.put(dir.sign(), Color.RED);
        }
        return colorMap;
    }

    private void solve2() {
        Grid<Character> grid = Grid.create(getInput(), colorMap());

        Direction startDirection = Direction.NORTH;
        Direction direction = startDirection;

        log.info(grid);

        int totalInitial = 0;

        Cell<Character> start = grid.findOne(direction.sign());
        int x = start.x();
        int y = start.y();

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
                    if (!potentialBlocks.contains(new Point(next.x(), next.y()))) {
                        //log.info("Checking potential block @ " + x + ":" + y);
                        Grid<Character> loopCheckGrid = Grid.create(getInput(), colorMap());
                        loopCheckGrid.put(next.x(), next.y(), WALL);
                        if (isLoop(loopCheckGrid, start.x(), start.y(), startDirection)) {
                            log.error("Found block @ " + x + ":" + y);

                            log.info(loopCheckGrid);
                            log.error("_".repeat(loopCheckGrid.getWidth()));

                            potentialBlocks.add(new Point(next.x(), next.y()));
                        }
                    }

                    //log.info("Move " + direction);
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

        Grid<Character> cleanGrid = Grid.create(getInput(), colorMap());
        for (Point block : potentialBlocks) {
            cleanGrid.put(block.x, block.y, BLOCK);
        }
        log.success(cleanGrid);

        log.info(potentialBlocks);
        log.info(potentialBlocks.size());

        log.info("Total initial: " + totalInitial);
    }

    private boolean isLoop(Grid<Character> grid, int x, int y, Direction direction) {
        List<PointWithDirection> used = new ArrayList<>();
        used.add(new PointWithDirection(x, y, direction));

        Cell<Character> next = grid.neighbor(x, y, direction);
        while(true) {
            try {
                if (next.value().equals(WALL)) {
                    //rotate CW
                    direction = direction.rotate90();
                    next = grid.neighbor(x, y, direction);
                } else {
                    //log.info("Move " + direction);

                    if (!grid.get(x, y).equals(START)) {
                        //Don't overwrite start
                        grid.put(x, y, USED_CHECK);
                    }

                    grid.put(next.x(), next.y(), direction.sign());

                    //log.info(grid);

                    x = next.x();
                    y = next.y();

                    if (used.contains(new PointWithDirection(x, y, direction))) {
                        boolean mark = false;
                        for (PointWithDirection point : used) {
                            if (point.x == x && point.y == y) {
                                mark = true;
                            }

                            Character value = grid.get(point.x, point.y);
                            //Mark loop
                            //Don't overwrite start & guard
                            if (mark && !value.equals(START) && !Direction.isDirection(value)) {
                                grid.put(point.x, point.y, LOOP);
                            }
                        }
                        return true;
                    }

                    used.add(new PointWithDirection(x, y, direction));
                    next = grid.neighbor(x, y, direction);
                }
            } catch (GridException e) {
                grid.put(x, y, USED_CHECK);
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

    public static void main(String[] ignoredArgs) {
        Day6 puzzle = new Day6();
        puzzle.run();
    }
}
