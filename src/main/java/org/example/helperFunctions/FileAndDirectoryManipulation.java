package org.example.helperFunctions;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Comparator;
import java.util.List;
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

    public boolean copyDirectory(String sourcePath, String destinationPath) {
        Path sourceDir = Paths.get(sourcePath).toAbsolutePath();
        Path destinationDir = Paths.get(destinationPath).toAbsolutePath();

        /*
        Noticed that sorting by directories first and creating them is a better practice and less errors would pop up
         */
        try {
            Stream<Path> stream = Files.walk(sourceDir);
            stream.sorted(Comparator.comparing(Path::toFile, Comparator.comparing(File::isDirectory).reversed())).forEach(source -> {
                Path destination = destinationDir.resolve(sourceDir.relativize(source));
                try {
                    if(Files.isDirectory(source)) {
                        Files.createDirectories(destination);
                    }
                    else {
                        Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Error copying file: " + source.toString(), e);
                }

            });
        } catch (IOException e) {
            throw new RuntimeException("Error walking source directory: " + sourceDir, e);
        }
        return true;
    }

    // Function that moves a file to a new location
    public boolean moveFile(String source, String destination) {
        Path sourcePath = Paths.get(source).toAbsolutePath();
        Path destinationPath = Paths.get(destination).toAbsolutePath();

        try {
            if (Files.isDirectory(sourcePath)) {
                System.out.println("Source is a directory, not a file: " + sourcePath);
                return false;
            }
            Path parentDir = destinationPath.getParent();
            if (parentDir != null && Files.notExists(parentDir)) {
                Files.createDirectories(parentDir);
            }
            Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File moved successfully from " + sourcePath + " to " + destinationPath);
            return true;
        } catch (NoSuchFileException e) {
            System.out.println("Source file not found: " + sourcePath);
            return false;
        } catch (IOException e) {
            System.out.println("I/O error occurred while copying: " + e.getMessage());
            return false;
        }

    }

    // Function that moves a directory to a new location
    public boolean moveDirectory(String source, String destination) {
        Path sourcePath = Paths.get(source).toAbsolutePath();
        Path destinationPath = Paths.get(destination).toAbsolutePath();

        try {
            if (Files.isRegularFile(sourcePath)) {
                System.out.println("This is a regular file not a Directory : " + sourcePath);
                return false;
            }

            Path parentDir = destinationPath.getParent();
            if (parentDir != null && Files.notExists(parentDir)) {
                Files.createDirectories(parentDir);
            }

            Stream<Path> stream = Files.walk(sourcePath);
            stream.sorted(Comparator.comparing(Path::toFile, Comparator.comparing(File::isDirectory).reversed())).forEach(
                    path -> {
                        Path destinationFile = destinationPath.resolve(sourcePath.relativize(path));
                        if(Files.isDirectory(path)) {
                            try {
                                Files.createDirectories(destinationFile);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            try {
                                Files.move(path, destinationFile, StandardCopyOption.REPLACE_EXISTING);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
            );
            Files.delete(sourcePath);

            System.out.println("Directory moved successfully from " + sourcePath + " to " + destinationPath);
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
