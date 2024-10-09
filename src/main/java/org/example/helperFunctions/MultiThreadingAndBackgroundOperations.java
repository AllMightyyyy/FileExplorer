package org.example.helperFunctions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MultiThreadingAndBackgroundOperations {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    // Function to Copy files in the background and can be tracked for progress
    public Future<Boolean> copyFileInBackground(String sourcePath, String destinationPath) {
        Callable<Boolean> task = () -> {
            FileAndDirectoryManipulation fdm = new FileAndDirectoryManipulation();
            return fdm.copyFile(Path.of(sourcePath), Path.of(destinationPath));
        };

        return executorService.submit(task);
    }

    public Future<Boolean> deleteFileInBackground(String filePath) {
        return executorService.submit(() -> {
            return FileAndDirectoryManipulation.deleteFile(filePath);
        });
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
