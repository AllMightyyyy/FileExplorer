package org.example.helperFunctions;

import javafx.scene.image.Image;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class UtilityAndHelper {

    // This function retrieves the available disk space in a directory's file system
    public long getFreeSpace(String directoryPath) {
        Path directoryFile = Paths.get(directoryPath).toAbsolutePath();
        File directory = directoryFile.toFile();
        if (!directory.exists()) {
            System.out.println("Directory does not exist: " + directoryPath);
            return -1;
        }
        if (!directory.isDirectory()) {
            System.out.println("Path is not a directory: " + directoryPath);
            return -1;
        }
        return directory.getFreeSpace();
    }

    // This function Retrieves an icon representing the type of a file ( For GUI display purposes )
    public Image getFileTypeIcon(File file) {
        if (file.isDirectory()) {
            return new Image("/icons/folder.png");
        }

        String fileName = file.getName();
        int lastIndexOf = fileName.lastIndexOf(".");

        if (lastIndexOf == -1) {
            return new Image("/icons/unknown.png");
        }

        String extension = fileName.substring(lastIndexOf + 1).toLowerCase();

        switch (extension) {
            case "txt":
                return new Image("/icons/text-file.png");
            case "jpg":
            case "png":
            case "jpeg":
            case "gif":
                return new Image("/icons/image-file.png");
            case "pdf":
                return new Image("/icons/pdf-file.png");
            case "doc":
            case "docx":
                return new Image("/icons/word-file.png");
            case "xlsx":
            case "xls":
                return new Image("/icons/excel-file.png");
            default:
                return new Image("/icons/default-file.png");
        }
    }


    // Function that returns text content of a text file and provides accurate logging
    public String readTextFileContents(String filePath) {
        Path file = Paths.get(filePath);
        try {
            return new String(Files.readAllBytes(file), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Error reading file content: " + filePath, e);
        }
    }

    // Function that writes to a file, with an option to overwrite or append
    public boolean writeToFile(String filePath, String data, boolean overwriteOption) {
        Path file = Paths.get(filePath);
        try {
            if (overwriteOption) {
                Files.write(file, data.getBytes(StandardCharsets.UTF_8), StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
            } else {
                Files.write(file, data.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
            }
            return true;
        } catch (IOException e) {
            String operation = overwriteOption ? "overwriting" : "appending to";
            throw new RuntimeException("Failed operation while " + operation + " the file: " + filePath, e);
        }
    }



}
