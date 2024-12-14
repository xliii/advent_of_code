package com.xliii.aoc.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Sets {

    public static <T> Set<T> intersection(Set<T> left, Set<T> right) {
        return left.stream().filter(right::contains).collect(Collectors.toSet());
    }

    public static <T> Set<T> union(Set<T> left, Set<T> right) {
        Set<T> result = new HashSet<>(left);
        result.addAll(right);
        return result;
    }

    public static <T> Set<T> difference(Set<T> left, Set<T> right) {
        Set<T> result = new HashSet<>(left);
        result.removeAll(right);
        return result;
    }

    public static <T> Set<Set<T>> combinations(Set<T> values, int size) {
        if (size != 2) {
            throw new IllegalArgumentException("Only 2-combinations implemented");
        }

        return pairCombinations(values);
    }

    private static <T> Set<Set<T>> pairCombinations(Set<T> values) {
        Set<Set<T>> pairs = new HashSet<>();
        List<T> list = new ArrayList<>(values);
        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                pairs.add(Set.of(list.get(i), list.get(j)));
            }
        }
        return pairs;
    }
}
