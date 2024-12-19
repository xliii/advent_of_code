package com.xliii.aoc.aoc2024;

import com.xliii.aoc.Puzzle;
import com.xliii.aoc.util.Color;
import com.xliii.aoc.util.graph.Dijkstra;
import com.xliii.aoc.util.graph.Graph;
import com.xliii.aoc.util.graph.GraphNode;
import com.xliii.aoc.util.grid.Grid;
import com.xliii.aoc.util.grid.Vector2D;

import java.util.List;
import java.util.Map;

public class Day18 extends Puzzle {

    private static final char WALL = '#';
    private static final char BOX = 'O';
    private static final char ROBOT = '@';
    private static final char EMPTY = '.';
    private static final char BOX_LEFT = '[';
    private static final char BOX_RIGHT = ']';

    private static final Map<Character, Color> COLOR_MAP = Map.of(
            WALL, Color.BLUE,
            BOX, Color.GREEN, BOX_LEFT, Color.GREEN, BOX_RIGHT, Color.GREEN,
            ROBOT, Color.YELLOW,
            EMPTY, Color.BLACK
    );

    @Override
    protected boolean useExample() {
        return false;
    }

    @Override
    protected void run() {
        //solve1(bytesFallen());
        solve2();
    }

    private int size() {
        return useExample() ? 7 : 71;
    }

    private int bytesFallen() {
        return useExample() ? 12: 1024;
    }

    private Grid<Character> grid;

    private void solve2() {
        var bytes = fallingBytes();
        for (int bytesFallen = bytesFallen(); bytesFallen < bytes.size(); bytesFallen++) {
            int distance = solve1(bytesFallen);
            if (distance == Integer.MAX_VALUE) {
                log.error("Blocked at " + bytesFallen + ": " + bytes.get(bytesFallen - 1));
                return;
            }

            log.success("Path at " + bytesFallen + ": " + distance);
        }

        log.info(grid);
    }

    private int solve1(int bytesFallen) {
        var data = new Character[size()][size()];
        grid = Grid.create(data, COLOR_MAP);
        grid.fill(EMPTY);
        var bytes = fallingBytes();
        bytes = bytes.subList(0, bytesFallen);

        for (var fallingByte : bytes) {
            grid.put(fallingByte.x(), fallingByte.y(), WALL);
        }

        //log.info(grid);

        int endX = size() - 1;
        int endY = size() - 1;

        int startX = 0;
        int startY = 0;

        var graph = Graph.fromGrid(grid, cell -> cell.value().equals(EMPTY));
        var endNode = graph.findNode(endX, endY).orElseThrow();

        Dijkstra.walkTo(endNode);

        var startNode = (GraphNode) graph.findNode(startX, startY).orElseThrow();

        grid.put(startNode.getX(), startNode.getY(), BOX);
        for (var node : startNode.getShortestPath()) {
            var graphNode = (GraphNode) node;
            grid.put(graphNode.getX(), graphNode.getY(), BOX);
        }

        //log.info(grid);

        //log.success(startNode.getDistance());

        return startNode.getDistance();
    }

    private List<Vector2D> fallingBytes() {
        return getInput().stream().map(Vector2D::fromString).toList();
    }

    public static void main(String[] ignoredArgs) {
        Day18 puzzle = new Day18();
        puzzle.run();
    }
}
