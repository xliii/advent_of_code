package com.xliii.aoc.util.graph;

import com.xliii.aoc.util.Sets;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class BronKerbosch {

    public static Set<Set<Node>> maximalCliques(Graph graph) {
        return maximalCliques(new HashSet<>(), graph.getNodes(), new HashSet<>());
    }

    public static Set<Node> maximumClique(Graph graph) {
        return maximalCliques(graph).stream().max(Comparator.comparingInt(Set::size)).orElseThrow();
    }

    private static Set<Set<Node>> maximalCliques(Set<Node> all, Set<Node> some, Set<Node> none) {
        if (some.isEmpty() && none.isEmpty()) {
            return Set.of(all);
        }

        Set<Set<Node>> result = new HashSet<>();
        var it = some.iterator();
        while (it.hasNext()) {
            var node = it.next();
            result.addAll(maximalCliques(
                    Sets.union(all, node),
                    Sets.intersection(some, node.getAdjacentNodes().keySet()),
                    Sets.intersection(none, node.getAdjacentNodes().keySet())
            ));
            it.remove();
            none.add(node);
        }

        return result;
    }
}
