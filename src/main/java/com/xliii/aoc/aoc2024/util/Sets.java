package com.xliii.aoc.aoc2024.util;

import java.util.HashSet;
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
}
