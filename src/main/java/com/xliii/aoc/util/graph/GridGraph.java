package com.xliii.aoc.util.graph;

import java.util.Optional;

public class GridGraph extends Graph {

    public Optional<Node> findNode(int x, int y) {
        return getNodes().stream().filter(node -> {
            var graphNode = (GraphNode) node;
            return graphNode.getX() == x && graphNode.getY() == y;
        }).findAny();
    }
}
