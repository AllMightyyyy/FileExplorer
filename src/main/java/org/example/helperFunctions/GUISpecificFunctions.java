package org.example.helperFunctions;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.io.File;

public class GUISpecificFunctions {

    // Method to display the directory structure in a TreeView
    public void displayDirectoryTree(TreeView<File> treeView, String rootDirectory) {
        File rootFile = new File(rootDirectory);

        // Create the root item for the TreeView
        TreeItem<File> rootItem = new TreeItem<>(rootFile);
        rootItem.setExpanded(true); // Expand root by default

        addTreeItems(rootItem, rootFile);

        treeView.setRoot(rootItem);
    }

    // Method to update the TableView with the contents of the selected directory
    public void updateFileListView(TableView<File> fileListView, String directoryPath) {
        File directory = new File(directoryPath);

        // Check if the directory is valid and exists
        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("Invalid directory: " + directoryPath);
            return;
        }

        // Get the list of files and directories
        File[] files = directory.listFiles();

        // Clear the existing items in the TableView
        ObservableList<File> fileList = FXCollections.observableArrayList();
        fileListView.getItems().clear();

        // Add all files and directories to the ObservableList
        if (files != null) {
            for (File file : files) {
                fileList.add(file);
            }
        }

        // Set the items for the TableView
        fileListView.setItems(fileList);
    }

    private void addTreeItems(TreeItem<File> parentItem, File parentFile) {
        File[] files = parentFile.listFiles();
        if (files != null) {
            for (File file : files) {
                TreeItem<File> childItem = new TreeItem<>(file);
                parentItem.getChildren().add(childItem);

                // If the current file is a directory, recursively add its children
                if (file.isDirectory()) {
                    addTreeItems(childItem, file);
                }
            }
        }
    }
}
