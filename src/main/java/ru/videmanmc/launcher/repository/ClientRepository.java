package ru.videmanmc.launcher.repository;

import ru.videmanmc.launcher.model.value.files.DownloadedFile;

import java.util.List;

public interface ClientRepository {

    void deleteByNames(List<String> fileNames);

    List<String> listDirectoryFileNames();

    void saveToFile(DownloadedFile downloadedFile);
}
