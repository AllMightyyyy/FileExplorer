package org.example.models;

import javafx.scene.image.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class FileItem {
    private String name;
    private long size;
    private String path;
    private boolean isDirectory;
    private long lastModified;
    private String extension;
    private boolean isHidden;
    private boolean isReadable;
    private boolean isWritable;
    private boolean isExecutable;
    private Image typeIcon;
    private Optional<Instant> creationTime;
    private String owner;

    public FileItem(String name, long size, String path, boolean isDirectory, long lastModified, String extension, boolean isHidden, boolean isReadable, boolean isWritable, boolean isExecutable, Image typeIcon, Optional<Instant> creationTime, String owner) {
        this.name = name;
        this.size = size;
        this.path = path;
        this.isDirectory = isDirectory;
        this.lastModified = lastModified;
        this.extension = extension;
        this.isHidden = isHidden;
        this.isReadable = isReadable;
        this.isWritable = isWritable;
        this.isExecutable = isExecutable;
        this.typeIcon = typeIcon;
        this.creationTime = creationTime;
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setHidden(boolean hidden) {
        isHidden = hidden;
    }

    public boolean isReadable() {
        return isReadable;
    }

    public void setReadable(boolean readable) {
        isReadable = readable;
    }

    public boolean isWritable() {
        return isWritable;
    }

    public void setWritable(boolean writable) {
        isWritable = writable;
    }

    public boolean isExecutable() {
        return isExecutable;
    }

    public void setExecutable(boolean executable) {
        isExecutable = executable;
    }

    public Image getTypeIcon() {
        return typeIcon;
    }

    public void setTypeIcon(Image typeIcon) {
        this.typeIcon = typeIcon;
    }

    public Optional<Instant> getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Optional<Instant> creationTime) {
        this.creationTime = creationTime;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    // Helper method to format file size
    public String getFormattedSize() {
        if (size < 1024) {
            return size + " B";
        } else if (size < 1024 * 1024) {
            return (size / 1024) + " KB";
        } else {
            return ( size / ( 1024 * 1024 ) + " MB");
        }
    }
}
