package org.example.helperFunctions;

import org.example.models.FileItem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.example.utils.FileItemUtil.createFileItem;

public class DirectoryAndFileNavigation {

    // This function lists all files and subdirectories within a given directory ( so it should be recursive )
    public List<FileItem> listDirectoryContents(String directoryPath) {

        List<FileItem> result = new ArrayList<>();

        // This implementation has been replaced because it used classic java methods which arent as efficient as modern java methods

        /*
        File currentDir = new File(directoryPath);
        if(!currentDir.isDirectory()) {
            throw new IllegalArgumentException("Provided path isn't a directory: " + directoryPath);
        }
        File[] subDirectoriesAndFiles = currentDir.listFiles();
        if (subDirectoriesAndFiles == null) {
            return result; // Return an empty list if there is a I/O error or no permission to access
        }
        for (File file : subDirectoriesAndFiles) {
            result.add(createFileItem(file));

            if(file.isDirectory()) {
                result.addAll(listDirectoryContents(file.getAbsolutePath()));
            }
        }
        return result;
        */

        // This implementation is much cleaner, efficient, and powerful

        Path startPath = Paths.get(directoryPath);
        try (Stream<Path> stream = Files.walk(startPath)) {
            stream.forEach(
                    path -> {
                        File file = path.toFile();
                        result.add(createFileItem(file));
                    }
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    // This function gets the parent directory of current directory
    public Path getParentDirectory(String currentDirectoryPath) {
        // This implementation is old, it uses File class which doesnt allow for as mutability as Path class and as many utility functions and helper methods
        /*
        File currentFile = new File(currentDirectoryPath);
        File parentFile = currentFile.getParentFile();

        if (parentFile == null) {
            return Path.of(currentFile.getAbsolutePath());
        }

        return Path.of(parentFile.getAbsolutePath());

         */
        // This implementation is modern and clean version than the above version
        Path currentPath = Paths.get(currentDirectoryPath);
        Path parentPath = currentPath.getParent();
        // Handle the case where current directory is root dir ( e.g C:// or D:// ) in this case return current directory
        if(parentPath == null) {
            return currentPath.toAbsolutePath();
        }
        return parentPath.toAbsolutePath();
    }

    // This function allows navigation to a specified directory and lists its content
    public List<FileItem> navigateToDirectory(String directoryPath) {
        Path dirPath = Paths.get(directoryPath).toAbsolutePath();
        // Check if the provided path is a valid directory
        if(!Files.isDirectory(dirPath)) {
            throw new IllegalArgumentException("The provided path is not a directory : " + dirPath);
        }
        return listDirectoryContents(dirPath.toString());
    }


}
