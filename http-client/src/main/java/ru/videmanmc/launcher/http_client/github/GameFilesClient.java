package ru.videmanmc.launcher.http_client.github;


import ru.videmanmc.launcher.dto.http.GameFile;

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
