package ru.videmanmc.launcher.http.client;

import ru.videmanmc.launcher.http.client.domain.value.GameFile;

import java.util.List;

/**
 * Used for downloading Minecraft client
 */
public interface GameFilesClient {

    /**
     * @param uri path to file in a file storage
     */
    GameFile download(String uri);

    List<String> getFilePaths();

}
