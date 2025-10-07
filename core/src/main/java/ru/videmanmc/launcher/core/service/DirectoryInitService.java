package ru.videmanmc.launcher.core.service;

import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;

public class DirectoryInitService {

    /**
     * Creates a tree of parent directories.
     * @param fileAbsolutePath absolute path to file
     */
    @SneakyThrows
    public void createParents(Path fileAbsolutePath) {
        Files.createDirectories(fileAbsolutePath.getParent());
    }

}
