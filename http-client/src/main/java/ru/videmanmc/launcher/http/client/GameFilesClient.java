package ru.videmanmc.launcher.http.client;

import ru.videmanmc.launcher.http.client.domain.value.GameFile;

import java.util.function.UnaryOperator;

/**
 * Used for downloading Minecraft client
 */
public interface GameFilesClient {

    default GameFile download(String url) {
        return download(url, s -> s);
    } // todo этот метод был создан по той причине, что я по которой я прокидывал abstractPathFormatting по всему лаунчеру — PathFormatter без кеша.

    /**
     * @param uri path to file in a file storage
     * @param abstractPathFormatting mapping function that transforms the uri from abstract format to remote one
     */
    GameFile download(String uri, UnaryOperator<String> abstractPathFormatting);

}
