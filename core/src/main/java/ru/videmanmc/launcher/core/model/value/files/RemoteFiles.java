package ru.videmanmc.launcher.core.model.value.files;

import ru.videmanmc.launcher.core.model.value.FileWithChecksum;
import ru.videmanmc.launcher.core.model.value.RemotePath;
import ru.videmanmc.launcher.http.client.domain.value.GameFile;

import java.util.List;

/**
 * A some source from which client files are retrieved.
 */
public interface RemoteFiles extends FileWithChecksum {

    /**
     * Downloads files by its name
     */
    List<GameFile> download(List<RemotePath> remotePaths);

    List<String> listRemotePaths();
}
