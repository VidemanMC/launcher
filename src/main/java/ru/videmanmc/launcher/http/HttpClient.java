package ru.videmanmc.launcher.http;

import ru.videmanmc.launcher.model.value.files.DownloadedFile;

public interface HttpClient {

    /**
     * @param url absolute path to a file on remote side
     */
    DownloadedFile download(String url);
}
