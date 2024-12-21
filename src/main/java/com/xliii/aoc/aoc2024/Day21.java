package com.xliii.aoc.aoc2024;

import com.xliii.aoc.Puzzle;
import com.xliii.aoc.util.Direction;
import com.xliii.aoc.util.graph.Dijkstra;
import com.xliii.aoc.util.graph.Graph;
import com.xliii.aoc.util.graph.GraphNode;
import com.xliii.aoc.util.graph.Node;
import com.xliii.aoc.util.grid.Grid;
import com.xliii.aoc.util.grid.Vector2D;

import java.util.*;
import java.util.stream.Collectors;

public class Day21 extends Puzzle {

    /*
    Keypad:
    789
    456
    123
     0A

    Directional pad:
     ^A
    <v>

    */

    private static final Character EMPTY = ' ';

    @Override
    protected boolean useExample() {
        return true;
    }

    @Override
    protected void run() {
        solve1();
        solve2();
    }

    private final Map<Path, String> KEYPAD_MOVEMENTS = new HashMap<>();
    private final Map<Path, String> DIRECTIONAL_MOVEMENTS = new HashMap<>();

    private void solve2() {
    }

    private record Path (String from, String to) {}

    private String pathToSequence(Node start, List<Node> path) {
        List<Character> sequence = new ArrayList<>();
        path.addFirst(start);
        for (int i = 0; i < path.size() - 1; i++) {
            var from = (GraphNode) path.get(i);
            var to = (GraphNode) path.get(i + 1);

            var vector = new Vector2D(to.getX() - from.getX(), to.getY() - from.getY());
            var direction = Direction.byVector(vector).orElseThrow();
            sequence.add(direction.sign());
        }
        //TODO: Optimize sequences while not allowing stepping outside keypad

        sequence.add('A');
        return sequence.stream().map(String::valueOf).collect(Collectors.joining());
    }

    private void fillKeypadMap() {
        var grid = Grid.create(List.of("789", "456", "123", " 0A"));

        //TODO: calculate sequence from x/y
        for (var cell : grid) {
            var graph = Graph.fromGrid(grid, c -> !c.value().equals(EMPTY));
            if (cell.value().equals(EMPTY)) continue;

            var targetNode = graph.findNode(cell.x(), cell.y()).orElseThrow();
            Dijkstra.walkTo(targetNode);
            for (var node : graph.getNodes()) {
                String path = pathToSequence(node, node.getShortestPath().reversed());
                //log.info("Path from " + node.getName() + " to " + targetNode.getName() + ": " + path);
                KEYPAD_MOVEMENTS.put(new Path(node.getName(), targetNode.getName()), path);
            }
        }
    }

    private void fillDirectionalMap() {
        var grid = Grid.create(List.of(" ^A", "<v>"));

        for (var cell : grid) {
            var graph = Graph.fromGrid(grid, c -> !c.value().equals(EMPTY));
            if (cell.value().equals(EMPTY)) continue;

            var targetNode = graph.findNode(cell.x(), cell.y()).orElseThrow();
            Dijkstra.walkTo(targetNode);
            for (var node : graph.getNodes()) {
                String path = pathToSequence(node, node.getShortestPath().reversed());
                //log.info("Path from " + node.getName() + " to " + targetNode.getName() + ": " + path);
                DIRECTIONAL_MOVEMENTS.put(new Path(node.getName(), targetNode.getName()), path);
            }
        }
    }

    private void solve1() {
        fillDirectionalMap();
        fillKeypadMap();

        int total = 0;

        for (var line : getInput()) {
            log.info(line);
            var first = unwrapSequence(line, KEYPAD_MOVEMENTS);
            log.error(first);
            var second = unwrapSequence(first, DIRECTIONAL_MOVEMENTS);
            log.warn(second);
            var third = unwrapSequence(second, DIRECTIONAL_MOVEMENTS);
            log.success(third);

            int number = Integer.parseInt(line.substring(0, line.length() - 1));
            int length = third.length();
            total += number * length;
            log.error("Complexity: " + length + " * " + number + " = " + number * length);

            //TODO: 3rd & 4th are wrong
        }

        log.success(total);

        String unwrap1 = unwrapSequence("^<<A^^A>>AvvvA", DIRECTIONAL_MOVEMENTS);
        System.out.println(unwrap1);
        System.out.println(unwrapSequence(unwrap1, DIRECTIONAL_MOVEMENTS));
    }

    private String unwrapSequence(String sequence, Map<Path, String> lookup) {
        sequence = "A" + sequence;
        var sb = new StringBuilder();
        for (int i = 0; i < sequence.length() - 1; i++) {
            var from = sequence.substring(i, i + 1);
            var to = sequence.substring(i + 1, i + 2);
            String path = lookup.get(new Path(from, to));
            sb.append(path);
        }
        return sb.toString();
    }

    public static void main(String[] ignoredArgs) {
        Day21 puzzle = new Day21();
        puzzle.run();
    }
}
