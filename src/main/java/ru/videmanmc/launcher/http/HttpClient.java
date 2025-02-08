package ru.videmanmc.launcher.http;

import ru.videmanmc.launcher.model.value.files.DownloadedFile;

public interface HttpClient {

    DownloadedFile download(String url);
}
