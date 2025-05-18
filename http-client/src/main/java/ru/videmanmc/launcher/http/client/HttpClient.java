package ru.videmanmc.launcher.http.client;

import ru.videmanmc.launcher.http.client.model.value.DownloadedFile;

import java.util.function.Function;

public interface HttpClient {

    default DownloadedFile download(String url) {
        return download(url, s -> s);
    }

    /**
     * @param url path to file in a file storage
     * @param abstractPathFormatting mapping function that transforms the url from abstract format to remote
     */
    DownloadedFile download(String url, Function<String, String> abstractPathFormatting);
}
