package ru.videmanmc.launcher.http.client;

import ru.videmanmc.launcher.http.client.model.value.DownloadedFile;

import java.util.function.Function;

/**
 * Used for downloading specific files
 */
public interface ContentsClient {

    default DownloadedFile download(String url) {
        return download(url, s -> s);
    } // todo этот метод был создан по той причине, что я по которой я прокидывал abstractPathFormatting по всему лаунчеру — PathFormatter без кеша.

    /**
     * @param uri path to file in a file storage
     * @param abstractPathFormatting mapping function that transforms the uri from abstract format to remote one
     */
    DownloadedFile download(String uri, Function<String, String> abstractPathFormatting);

}
