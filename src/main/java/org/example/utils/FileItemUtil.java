package org.example.utils;

import javafx.scene.image.Image;
import org.example.models.FileItem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.Optional;

public class FileItemUtil {
    public static FileItem createFileItem(File file) {
        try {
            Path filePath = file.toPath();
            BasicFileAttributes attributes = Files.readAttributes(filePath, BasicFileAttributes.class);
            String name = file.getName();
            String path = file.getAbsolutePath();

            long size = file.isDirectory() ? 0 : file.length();

            String extension = "";
            if (!file.isDirectory() && name.contains(".")) {
                extension = name.substring(name.lastIndexOf(".") + 1);
            }

            boolean isHidden = file.isHidden();
            boolean isReadable = file.canRead();
            boolean isWritable = file.canWrite();
            boolean isExecutable = file.canExecute();

            long lastModified = file.lastModified();

            Instant creationTime = Optional.ofNullable(attributes.creationTime())
                    .map(FileTime::toInstant)
                    .orElse(null);

            String owner = "";
            try {
                owner = Files.getOwner(filePath).getName();
            } catch (IOException e) {
                // Ignore if file doesn't support ownership or there is error
            }

            Image typeIcon = getTypeIcon(extension, file.isDirectory());

            return new FileItem(
                    name,
                    size,
                    path,
                    file.isDirectory(),
                    lastModified,
                    extension,
                    isHidden,
                    isReadable,
                    isWritable,
                    isExecutable,
                    typeIcon,
                    Optional.ofNullable(creationTime),
                    owner
            );
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Image getTypeIcon(String extension, boolean isDirectory) {
        if (isDirectory) {
            return new Image("/icons/folder.png");
        } else if (extension.equals("txt")) {
            return new Image("/icons/text-file.png");
        } else if (extension.equals("jpg") || extension.equals("png")) {
            return new Image("/icons/image-file.png");
        } else {
            return new Image("/icons/default-file.png");
        }
    }
}
