package ru.videmanmc.launcher.repository;

import com.google.inject.Inject;
import lombok.SneakyThrows;
import ru.videmanmc.launcher.model.value.Settings;
import ru.videmanmc.launcher.model.value.files.DownloadedFile;
import ru.videmanmc.launcher.service.DirectoryInitService;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ClientRepositoryImp implements ClientRepository {

    private final String clientDirectory;

    private final DirectoryInitService directoryInitService;

    @Inject
    public ClientRepositoryImp(Settings settings, DirectoryInitService directoryInitService) {
        this.clientDirectory = settings.getGame().getDirectory().getAbsolutePath();
        this.directoryInitService = directoryInitService;
    }

    @Override
    public void deleteByNames(List<String> fileNames) {
        fileNames.stream()
                .map(fileName -> Path.of(clientDirectory, fileName))
                .forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @Override
    public List<String> listDirectoryFileNames() {
        Path clientPath = Paths.get(clientDirectory);
        if (Files.notExists(clientPath)) {
            return new ArrayList<>();
        }

        try (Stream<Path> stream = Files.walk(clientPath)) {
            return stream
                    .filter(Files::isRegularFile)
                    .map(Path::toString)
                    .toList();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @SneakyThrows
    public void saveToFile(DownloadedFile downloadedFile) {
        var absoluteFilePath = Path.of(clientDirectory, downloadedFile.filePath());

        directoryInitService.createParents(absoluteFilePath);

        Files.write(absoluteFilePath, downloadedFile.contents());
    }
}
