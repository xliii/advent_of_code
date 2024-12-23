package com.xliii.aoc.util.graph;

import com.xliii.aoc.util.grid.Cell;
import com.xliii.aoc.util.grid.Grid;
import com.xliii.aoc.util.grid.Vector2D;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Graph {

    public void addNode(Node node) {
        nodeMap.put(node.getName(), node);
    }

    public final Map<String, Node> nodeMap = new HashMap<>();

    public static GridGraph fromGrid(Grid<Character> grid, Function<Cell<Character>, Boolean> eligibility) {
        Map<Vector2D, Node> nodeMap = new HashMap<>();

        var graph = new GridGraph();
        for (var cell : grid) {
            if (eligibility.apply(cell)) {
                var node = new GraphNode(cell);
                nodeMap.put(cell.pos(), node);
                graph.addNode(node);
            }
        }

        for (var pos : nodeMap.keySet()) {
            Node current = nodeMap.get(pos);
            for (var neighbor : grid.neighbors(pos.x(), pos.y())) {
                if (!eligibility.apply(neighbor)) continue;

                Node adjacent = nodeMap.get(neighbor.pos());
                current.addAdjacentNode(adjacent, 1);
                adjacent.addAdjacentNode(current, 1);
            }
        }

        return graph;
    }

    public Set<Node> getNodes() {
        return new HashSet<>(nodeMap.values());
    }

    @Override
    public String toString() {
        return nodeMap.values().stream().map(node -> "(" + node.getName() + " - " + node.getAdjacentNodes().keySet() + ")").collect(Collectors.joining());
    }
}
