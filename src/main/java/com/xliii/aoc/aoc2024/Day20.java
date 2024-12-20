package com.xliii.aoc.aoc2024;

import com.xliii.aoc.Puzzle;
import com.xliii.aoc.util.Direction;
import com.xliii.aoc.util.graph.*;
import com.xliii.aoc.util.grid.Grid;

import java.util.ArrayList;
import java.util.Set;

public class Day20 extends Puzzle {

    private static final char WALL = '#';
    private static final char START = 'S';
    private static final char END = 'E';
    private static final char EMPTY = '.';
    private static final char WALK = 'o';

    private static final Set<Character> ELIGIBLE_CELLS = Set.of(EMPTY, START, END);

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

    }

    private void solve1() {
        Grid<Character> grid = Grid.create(getInput());
        log.info(grid);

        var start = grid.findOne(START).orElseThrow();
        var end = grid.findOne(END).orElseThrow();

        GridGraph graph = Graph.fromGrid(grid, cell -> ELIGIBLE_CELLS.contains(cell.value()));

        var startNode = (GraphNode) graph.findNode(start.x(), start.y()).orElseThrow();
        var endNode = graph.findNode(end.x(), end.y()).orElseThrow();

        Dijkstra.walkTo(endNode);

        var path = new ArrayList<>(startNode.getShortestPath().stream().map(node -> (GraphNode) node).toList());
        path.addFirst(startNode);

        int skips = 0;

        for (var node : path) {

            for (var neighbor : grid.neighbors(node.getX(), node.getY())) {
                if (ELIGIBLE_CELLS.contains(neighbor.value())) continue;

                //Neighbor is skipped wall

                //Check neighbors of neighbor
                for (var neighbor2 : grid.neighbors(neighbor.x(), neighbor.y())) {
                    //Skip self
                    if (neighbor2.x() == node.getX() && neighbor2.y() == node.getY()) continue;
                    //Skip walls
                    if (neighbor2.value().equals(WALL)) continue;

                    //Simulate
                    var cheatNode = graph.findNode(neighbor2.x(), neighbor2.y()).orElseThrow();
                    var saved = node.getDistance() - cheatNode.getDistance() - 2; //Subtract 2 for skipped wall movement
                    if (saved >= 100) {
                        skips++;
                    }
                }
            }
        }

        log.success(skips);
    }

    public static void main(String[] ignoredArgs) {
        Day20 puzzle = new Day20();
        puzzle.run();
    }
}
