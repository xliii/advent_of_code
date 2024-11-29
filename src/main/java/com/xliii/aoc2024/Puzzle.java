package com.xliii.aoc2024;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;

public class Puzzle {

    protected List<String> getInput() {
        try {
            String filename = getClass().getSimpleName().toLowerCase() + ".txt";
            File file = readFile(filename);
            return Files.readAllLines(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private File readFile(String filename) {
        URL resource = getClass().getClassLoader().getResource(filename);
        if (resource == null) {
            throw new RuntimeException("File not found: " + filename);
        }
        return new File(resource.getPath());
    }
}
