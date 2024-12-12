package com.xliii.aoc.aoc2024;

import com.xliii.aoc.Puzzle;
import com.xliii.aoc.aoc2024.util.Direction;
import com.xliii.aoc.aoc2024.util.grid.Cell;
import com.xliii.aoc.aoc2024.util.grid.Grid;
import com.xliii.aoc.aoc2024.util.grid.Vector2D;

import java.text.MessageFormat;
import java.util.*;

public class Day12 extends Puzzle {

    @Override
    protected boolean useExample() {
        return false;
    }

    @Override
    protected void run() {
        solve1();
        solve2();
    }

    private void solve2() {
        Grid<Character> grid = Grid.create(getInput());
        Set<Region> regions = split(grid);
        regions.forEach(region -> log.info(region));

        var price = regions.stream().map(Region::price2).reduce(Integer::sum).orElseThrow();
        log.success(price);
    }

    private record Region(Character plant, List<Vector2D> points) {

        public int area() {
            return points.size();
        }

        public int perimeter() {
            int perimeter = 0;
            for (var point : points) {
                for (var neighbor : point.neighbors(Direction.ORTHOGONAL)) {
                    if (!points.contains(neighbor)) {
                        perimeter++;
                    }
                }
            }
            return perimeter;
        }

        public int sides() {
            //sides = angles
            int angles = 0;

            Set<List<Direction>> corners = new HashSet<>();

            for (var diagonal : Direction.DIAGONAL) {
                var next = diagonal.rotate45(true);
                var prev = diagonal.rotate45(false);
                corners.add(List.of(diagonal, next, prev));
            }

            for (var point : points) {
                for (var corner : corners) {
                    var diagonal = point.move(corner.get(0));
                    var next = point.move(corner.get(1));
                    var prev = point.move(corner.get(2));

                    if (!points.contains(diagonal) && !points.contains(next) && !points.contains(prev)) {
                        //outer angle
                        angles++;
                    } else if (!points.contains(diagonal) && points.contains(next) && points.contains(prev)) {
                        //inner angle
                        angles++;
                    } else if (points.contains(diagonal) && !points.contains(next) && !points.contains(prev)) {
                        //inner across angle
                        //AAA
                        //ABA
                        //BAA
                        angles++;
                    }
                }
            }

            return angles;
        }

        public int price() {
            return area() * perimeter();
        }

        public int price2() {
            return area() * sides();
        }

        @Override
        public String toString() {
            return MessageFormat.format("- A region of {0} plants with price {1}*{2}={3} {4}'}'", plant, area(), sides(), price2(), points);
        }
    }

    private void solve1() {
        Grid<Character> grid = Grid.create(getInput());
        Set<Region> regions = split(grid);
        regions.forEach(region -> log.info(region));

        var price = regions.stream().map(Region::price).reduce(Integer::sum).orElseThrow();
        log.success(price);
    }

    private Set<Region> split(Grid<Character> grid) {
        Set<Region> regions = new HashSet<>();
        Set<Vector2D> seen = new HashSet<>();
        for (var cell : grid) {
            if (seen.contains(cell.pos())) {
                continue;
            }

            Queue<Cell<Character>> toCheck = new LinkedList<>();
            toCheck.add(cell);
            while (!toCheck.isEmpty()) {
                Cell<Character> current = toCheck.poll();
                seen.add(current.pos());

                Optional<Region> existingRegion = regions.stream()
                        .filter(region -> region.plant().equals(current.value()))
                        .filter(region -> region.points.stream()
                                .anyMatch(point -> point.isNeighbor(current.pos())))
                        .findFirst();
                if (existingRegion.isPresent()) {
                    existingRegion.get().points.add(current.pos());
                } else {
                    var newRegion = new Region(current.value(), new ArrayList<>());
                    newRegion.points.add(current.pos());
                    regions.add(newRegion);
                }

                for (var neighbor : grid.neighbors(current.x(), current.y(), Direction.ORTHOGONAL)) {
                    if (!neighbor.value().equals(current.value())) {
                        continue;
                    }

                    if (seen.contains(neighbor.pos())) {
                        continue;
                    }

                    if (toCheck.contains(neighbor)) {
                        continue;
                    }

                    toCheck.add(neighbor);
                }
            }



        }
        return regions;
    }

    public static void main(String[] ignoredArgs) {
        Day12 puzzle = new Day12();
        puzzle.run();
    }
}
