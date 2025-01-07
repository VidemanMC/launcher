package ru.videmanmc.launcher.model.value.files;

import ru.videmanmc.launcher.model.value.FileWithChecksum;

import java.util.List;

/**
 * This class represents a some source from which client files are retrieved somehow.
 */
public interface RemoteFiles extends FileWithChecksum {

    /**
     * Downloads files by its name
     */
    List<DownloadedFile> download(List<String> fileNames);

    /**
     * Download list of ignored files
     */
    IgnoredFiles getIgnoredFiles();
}
