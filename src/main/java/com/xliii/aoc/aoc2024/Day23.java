package com.xliii.aoc.aoc2024;

import com.xliii.aoc.Puzzle;
import com.xliii.aoc.util.graph.Graph;
import com.xliii.aoc.util.graph.Node;

import java.util.HashSet;
import java.util.Set;

public class Day23 extends Puzzle {

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
        var graph = constructGraph();

        log.info(graph);

        var threes = new HashSet<Set<Node>>();

        for (var first : graph.getNodes()) {
            for (var second : first.getAdjacentNodes().keySet()) {
                for (var third : second.getAdjacentNodes().keySet()) {
                    if (third.getAdjacentNodes().containsKey(first)) {
                        if (first.getName().startsWith("t") || second.getName().startsWith("t") || third.getName().startsWith("t")) {
                            threes.add(Set.of(first, second, third));
                        }
                    }
                }
            }
        }

        log.success(threes.size());
        //log.success(threes);
    }

    private Graph constructGraph() {
        Graph graph = new Graph();
        for (var line : getInput()) {
            var parts = line.split("-");
            var first = graph.nodeMap.getOrDefault(parts[0], new Node(parts[0]));
            var second = graph.nodeMap.getOrDefault(parts[1], new Node(parts[1]));
            first.addAdjacentNode(second, 1);
            second.addAdjacentNode(first, 1);

            graph.addNode(first);
            graph.addNode(second);
        }
        return graph;
    }

    public static void main(String[] ignoredArgs) {
        Day23 puzzle = new Day23();
        puzzle.run();
    }
}
