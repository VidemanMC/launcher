package ru.videmanmc.launcher.model.value.files;

import ru.videmanmc.launcher.model.value.FileWithChecksum;
import ru.videmanmc.launcher.model.value.RemotePath;

import java.util.List;

/**
 * A some source from which client files are retrieved.
 */
public interface RemoteFiles extends FileWithChecksum {

    /**
     * Downloads files by its name
     */
    List<DownloadedFile> download(List<RemotePath> remotePaths);

    List<String> listRemotePaths();
}
