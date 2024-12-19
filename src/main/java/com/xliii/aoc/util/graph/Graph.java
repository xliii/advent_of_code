package com.xliii.aoc.util.graph;

import com.xliii.aoc.util.grid.Grid;
import com.xliii.aoc.util.grid.Vector2D;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Graph {

    private final Set<Node> nodes = new HashSet<>();

    public void addNode(Node node) {
        nodes.add(node);
    }

    public static GridGraph fromGrid(Grid<Character> grid) {
        Map<Vector2D, Node> nodeMap = new HashMap<>();

        var graph = new GridGraph();
        for (var cell : grid) {
            var node = new GraphNode(cell);
            nodeMap.put(cell.pos(), node);
            graph.addNode(node);
        }

        for (var pos : nodeMap.keySet()) {
            Node current = nodeMap.get(pos);
            for (var neighbor : grid.neighbors(pos.x(), pos.y())) {
                Node adjacent = nodeMap.get(neighbor.pos());
                current.addAdjacentNode(adjacent, 1);
                adjacent.addAdjacentNode(current, 1);
            }
        }

        return graph;
    }

    public Set<Node> getNodes() {
        return nodes;
    }
}
