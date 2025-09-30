package ru.videmanmc.launcher.core.model.entity;

import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import ru.videmanmc.launcher.core.model.value.files.IgnoredFiles;
import ru.videmanmc.launcher.core.model.value.files.LocalFiles;
import ru.videmanmc.launcher.dto.http.GameFile;
import ru.videmanmc.launcher.http_client.exception.HttpDownloadException;
import ru.videmanmc.launcher.http_client.github.GameFilesClient;
import ru.videmanmc.launcher.http_client.github.PathFormatMapper;
import ru.videmanmc.launcher.http_client.github.RemoteChecksumCalculator;

import java.util.List;

/**
 * Represents game client, that contain files and operates over it. Client is single in the launcher.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class Client {

    private final LocalFiles localFiles;

    private final GameFilesClient gameFilesClient;

    private final RemoteChecksumCalculator remoteChecksumCalculator;

    private final IgnoredFiles ignoredFiles;

    private final PathFormatMapper pathFormatMapper;

    /**
     * @throws HttpDownloadException if backend returned error status
     */
    public List<GameFile> update() {
        var oldFiles = getFilteredFiles(Filter.DELETE);
        localFiles.delete(oldFiles);

        System.out.println("Delete: " + oldFiles);

        var changedFiles = getFilteredFiles(Filter.DOWNLOAD);

        System.out.println("Download: " + changedFiles);

        var remoteMappedChangedFiles = pathFormatMapper.abstractToRemoteFormat(
                changedFiles,
                gameFilesClient.getFilePaths()
        );

        return remoteMappedChangedFiles.stream()
                                       .map(gameFilesClient::download)
                                       .toList();
    }

    private List<String> getFilteredFiles(Filter filter) {
        var localChecksum = localFiles.calculateChecksum();
        var remoteChecksum = remoteChecksumCalculator.calculateChecksum();

        var difference = switch (filter) {
            case DELETE -> localChecksum.difference(remoteChecksum);
            case DOWNLOAD -> remoteChecksum.difference(localChecksum);
        };

        return ignoredFiles.filter(difference.getFileNames());
    }

    public enum Filter {

        /**
         * Filter files to download from remote.
         */
        DOWNLOAD,

        /**
         * Filter files to delete local, excluding ignored ones.
         */
        DELETE
    }

}
