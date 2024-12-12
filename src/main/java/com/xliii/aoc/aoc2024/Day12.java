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
        return true;
    }

    @Override
    protected void run() {
        solve1();
        solve2();
    }

    private void solve2() {

    }

    private record Region(Character plant, List<Vector2D> points) {

        public int area() {
            return points.size();
        }

        public int perimeter() {
            int perimeter = 0;
            for (var point : points) {
                for (var neighbor: point.neighbors(Direction.ORTHOGONAL)) {
                    if (!points.contains(neighbor)) {
                        perimeter++;
                    }
                }
            }
            return perimeter;
        }

        public int price() {
            return area() * perimeter();
        }

        @Override
        public String toString() {
            return MessageFormat.format("- A region of {0} plants with price {1}*{2}={3} {4}'}'", plant, area(), perimeter(), price(), points);
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
        for (Character plant : grid.distinctValues()) {
            var plants = grid.findAll(plant);
            regions.addAll(formRegions(plants));
        }
        return regions;
    }

    private Set<Region> formRegions(List<Cell<Character>> plants) {
        var iterator = plants.iterator();
        Set<Region> regions = new HashSet<>();

        //TODO: Fix region forming
        while (iterator.hasNext()) {
            var plant = iterator.next();
            Optional<Region> existingRegion = regions.stream()
                    .filter(region -> region.points.stream()
                            .anyMatch(point -> point.isNeighbor(plant.pos())))
                    .findFirst();
            if (existingRegion.isPresent()) {
                existingRegion.get().points.add(plant.pos());
            } else {
                var newRegion = new Region(plant.value(), new ArrayList<>(List.of(plant.pos())));
                regions.add(newRegion);
            }
        }
        return regions;
    }

    public static void main(String[] ignoredArgs) {
        Day12 puzzle = new Day12();
        puzzle.run();
    }
}
