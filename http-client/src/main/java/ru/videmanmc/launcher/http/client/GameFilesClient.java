package ru.videmanmc.launcher.http.client;

import ru.videmanmc.launcher.http.client.domain.value.DownloadedFile;

import java.util.function.Function;

/**
 * Used for downloading Minecraft client
 */
public interface GameFilesClient {

    default DownloadedFile download(String url) {
        return download(url, s -> s);
    } // todo этот метод был создан по той причине, что я по которой я прокидывал abstractPathFormatting по всему лаунчеру — PathFormatter без кеша.

    /**
     * @param uri path to file in a file storage
     * @param abstractPathFormatting mapping function that transforms the uri from abstract format to remote one
     */
    DownloadedFile download(String uri, Function<String, String> abstractPathFormatting);

}
