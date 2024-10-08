package org.example.helperFunctions;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Comparator;
import java.util.stream.Stream;

public class FileAndDirectoryManipulation {

    // Creates an empty file at specified path ( if file already exists , or file creation fails return false )
    public boolean createFile(String filePath) {
        Path pathToCreate = Paths.get(filePath).toAbsolutePath();
        try {
            if (Files.notExists(pathToCreate.getParent())) {
                Files.createDirectory(pathToCreate.getParent());
            }
            Files.createFile(pathToCreate);
        } catch (FileAlreadyExistsException e) {
            System.out.println("File Already Exists : " + pathToCreate);
            return false;
        } catch (IOException e) {
            System.out.println("Error creating file : " + e.getMessage());
            return false;
        }

        return true;
    }

    public boolean createDirectory(String dirPath) {
        Path directory = Paths.get(dirPath).toAbsolutePath();
        try {
            Files.createDirectories(directory); // using createDirectories here creates the full directory tree
        } catch (FileAlreadyExistsException e) {
            System.out.println("Directory Already Exists : " + directory.toAbsolutePath());
            return false;
        } catch (IOException e) {
            System.out.println("Error creating Directory : " + e.getMessage());
            return false;
        }
        return true;
    }

    // Delete specified file
    public boolean deleteFile(String filePath) {
        Path toDelete = Paths.get(filePath).toAbsolutePath();
        try {
            Files.delete(toDelete);
            System.out.println("File deleted successfully");
            return true;
        } catch (NoSuchFileException e) {
            System.out.println("File does not exist: " + toDelete);
            return false;
        } catch (DirectoryNotEmptyException e) {
            System.out.println("The path is a directory that is not empty: " + toDelete);
            return false;
        } catch (IOException e) {
            System.out.println("There was a problem deleting the file: " + e.getMessage());
            return false;
        }
    }

    // Delete the full directory and its content
    private boolean deleteDirectory(String filePath) {
        Path toDelete = Paths.get(filePath);
        if (!Files.isDirectory(toDelete)) {
            System.out.println("This is not a directory: " + toDelete);
            return false;
        }
        try(Stream<Path> stream = Files.walk(toDelete)) {
            stream.sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                            System.out.println("Delete : " + path);
                        } catch (IOException e) {
                            throw new RuntimeException("Error deleting file: " + path, e);
                        }
                    });
        } catch (IOException e) {
            System.out.println("Error walking directory: " + e.getMessage());
            return false;
        }
        return true;
    }

    // This functions renames a file or directory
    private boolean renameFile(String oldPath, String newPath) {
        Path oldFile = Paths.get(oldPath).toAbsolutePath();
        Path newFile = Paths.get(newPath).toAbsolutePath();

        // Make sure that the old file and new file are in the same directory
        if (!oldFile.getParent().equals(newFile.getParent())) {
            System.out.println("The new path is in a different directory. This is not a rename.");
            return false;
        }

        try {
            Files.move(oldFile, newFile, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File renamed successfully from " + oldPath + " to " + newPath);
            return true;
        } catch (FileAlreadyExistsException e) {
            System.out.println("A file with the new name already exists: " + newPath);
            return false;
        } catch (NoSuchFileException e) {
            System.out.println("The original file was not found: " + oldPath);
            return false;
        } catch (IOException e) {
            System.out.println("I/O error occurred while renaming: " + e.getMessage());
            return false;
        }
    }

    // Function that copies a file from one location to another
    private boolean copyFile(String sourcePath, String destinationPath) {
        Path sourceFile = Paths.get(sourcePath).toAbsolutePath();
        Path destinationFile = Paths.get(destinationPath).toAbsolutePath();

        try {
            Path parentDir = destinationFile.getParent();
            if(parentDir != null && Files.notExists(parentDir)) {
                Files.createDirectories(parentDir);
            }
            Files.copy(sourceFile, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File copied successfully from " + sourceFile + " to " + destinationFile);
            return true;
        } catch (FileAlreadyExistsException e) {
            System.out.println("File already exists at the destination path: " + destinationFile);
            return false;
        } catch (NoSuchFileException e) {
            System.out.println("Source file not found: " + sourceFile);
            return false;
        } catch (IOException e) {
            System.out.println("I/O error occurred while copying: " + e.getMessage());
            return false;
        }
    }

}
