package com.xliii.aoc.util.graph;

import com.xliii.aoc.util.grid.Cell;

public class GraphNode extends Node {

    private final int x;
    private final int y;

    public GraphNode(Cell<Character> cell) {
        super(cell.value().toString());
        x = cell.x();
        y = cell.y();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
