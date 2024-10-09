package org.example.helperFunctions;

import org.example.models.FileItem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import static org.example.utils.FileItemUtil.createFileItem;

public class FilePropertiesAndInformation {
    public Map<String, String> getFileProperties(File file) {
        FileItem fileItem = createFileItem(file);
        Map<String, String> properties = new HashMap<>();
        properties.put("Name", fileItem.getName());
        properties.put("Size", String.valueOf(fileItem.getSize()));
        properties.put("Path", fileItem.getPath());
        properties.put("Is Directory", String.valueOf(fileItem.isDirectory()));
        properties.put("Last Modified", String.valueOf(fileItem.getLastModified()));
        properties.put("Extension", fileItem.getExtension());
        properties.put("Is Hidden", String.valueOf(fileItem.isHidden()));
        properties.put("Is Readable", String.valueOf(fileItem.isReadable()));
        properties.put("Is Writable", String.valueOf(fileItem.isWritable()));
        properties.put("Is Executable", String.valueOf(fileItem.isExecutable()));

        fileItem.getCreationTime().ifPresent(creationTime ->
                properties.put("Creation Time", creationTime.toString())
        );
        if (fileItem.getOwner() != null) {
            properties.put("Owner", fileItem.getOwner());
        }
        return properties;
    }

    // This is a method to calculate directory size and returns it
    public long getDirectorySize(String Dir) {
        Path currentPath = Path.of(Dir);
        AtomicLong size = new AtomicLong(0);
        try (
            Stream<Path> stream = Files.walk(currentPath)) {
            stream.filter(Files::isRegularFile)
                    .forEach(
                    path -> {
                        size.addAndGet(path.toFile().length());
                    }
            );
        } catch (IOException e) {
            throw new RuntimeException("Error calculating directory size: " + Dir, e);
        }
        return size.get();
    }

    // Returns if the directory is empty or not
    public boolean isDirectoryEmpty(String Dir) {
        Path currentPath = Path.of(Dir);
        try(Stream<Path> stream = Files.walk(currentPath, 1)) {
            return stream.findAny().isEmpty();
        }catch (IOException e) {
            throw new RuntimeException("Error checking directory contents: " + Dir, e);
        }
    }


}
