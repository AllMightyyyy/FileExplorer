package org.example.helperFunctions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FileSearchingAndFiltering {

    // Function to search for file pattern by name
    public List<Path> searchFilesByName(String destinationPath, String fileNamePattern) {
        Path destination = Paths.get(destinationPath).toAbsolutePath();
        List<Path> results = new ArrayList<>();
        try (Stream<Path> stream = Files.walk(destination)) {
            stream.filter(p -> p.getFileName().toString().contains(fileNamePattern))
                    .forEach(results::add);
        } catch (IOException e) {
            throw new RuntimeException("Error walking the directory: " + destinationPath, e);
        }
        return results;
    }

    // Function that filters files by extension
    public List<Path> filterFilesByExtension(String directoryPath, String extension) {
        Path destination = Paths.get(directoryPath).toAbsolutePath();
        List<Path> results = new ArrayList<>();
        try (Stream<Path> stream = Files.walk(destination)) {
            stream.filter(p -> p.getFileName().toString().endsWith(extension)).forEach(results::add);
        } catch (IOException e) {
            throw new RuntimeException("Error walking the directory: " + destination, e);
        }
        return results;
    }

    // Function to filter by size
    // My first implementation worked but i knew it was fucking brain dead and i couldve made it easier
    /*
    public List<Path> filterFilesBySize(String directoryPath, long sizeThreshold, boolean greaterThan) {
        Path directoryFile = Paths.get(directoryPath).toAbsolutePath();
        List<Path> results = new ArrayList<>();
        if (greaterThan) {
            try (Stream<Path> stream = Files.walk(directoryFile)) {
                stream.filter(p -> (p.toFile().length()) > sizeThreshold).forEach(results::add);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return results;
        } else {
            try (Stream<Path> stream = Files.walk(directoryFile)) {
                stream.filter(p -> (p.toFile().length()) < sizeThreshold).forEach(results::add);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return results;
        }
    }
     */
    public List<Path> filterFilesBySize(String directoryPath, long sizeThreshold, boolean greaterThan) {
        Path directoryFile = Paths.get(directoryPath).toAbsolutePath();
        List<Path> results = new ArrayList<>();
        // Filter files if they are directories
        try(Stream<Path> stream = Files.walk(directoryFile)) {
            stream.filter(p -> !Files.isDirectory(p)).filter(p -> greaterThan ? (p.toFile().length() > sizeThreshold) : (p.toFile().length() < sizeThreshold)).forEach(results::add);
        } catch (IOException e) {
            System.out.println("There was a problem during execution " + e);
        }
        return results;
    }
}
