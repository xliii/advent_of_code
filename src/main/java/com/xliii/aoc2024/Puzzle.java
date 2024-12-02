package com.xliii.aoc2024;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;

public abstract class Puzzle {

    public Logger log = new Logger();

    public List<String> getInput() {
        try {
            File file = readFile(inputFilename());
            return Files.readAllLines(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String inputFilename() {
        String day = getClass().getSimpleName().toLowerCase();
        String suffix = useExample() ? "_example" : "";
        String extension = ".txt";

        return day + suffix + extension;
    }

    private File readFile(String filename) {
        URL resource = getClass().getClassLoader().getResource(filename);
        if (resource == null) {
            throw new RuntimeException("File not found: " + filename);
        }
        return new File(resource.getPath());
    }

    protected abstract void run();

    protected abstract boolean useExample();
}
