package com.xliii.aoc.aoc2024.util;

import java.util.List;
import java.util.stream.Collectors;

public class Statistics {

    public static Double mean(List<? extends Number> data) {
        return data.stream().collect(Collectors.averagingDouble(Number::doubleValue));
    }

    public static Double variance(List<? extends Number> data) {
        if (data.isEmpty()) {
            return 0d;
        }
        Double mean = mean(data);

        Double variance = data.stream().map(number -> Math.pow(number.doubleValue() - mean, 2)).reduce(Double::sum).orElseThrow();
        return variance / data.size();
    }
}
