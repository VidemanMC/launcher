package ru.videmanmc.launcher.model.entity;

import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import ru.videmanmc.launcher.model.value.files.DownloadedFile;
import ru.videmanmc.launcher.model.value.files.IgnoredFiles;
import ru.videmanmc.launcher.model.value.files.LocalFiles;
import ru.videmanmc.launcher.model.value.files.RemoteFiles;

import java.util.List;

/**
 * Represents game client, that contain files and operates over it. Client is single in the launcher.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class Client {

    private final LocalFiles localFiles;

    private final RemoteFiles remoteFiles;

    private final IgnoredFiles ignoredFiles;

    /**
     * @return files needed to download
     */
    public List<DownloadedFile> prepareGameFiles() {
        var oldFiles = getFilteredFiles(Filter.DELETE);
        localFiles.delete(oldFiles);

        var changedFiles = getFilteredFiles(Filter.DOWNLOAD);
        return remoteFiles.download(changedFiles);
    }

    private List<String> getFilteredFiles(Filter filter) {
        var localChecksum = localFiles.calculateChecksum();
        var remoteChecksum = remoteFiles.calculateChecksum();

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
