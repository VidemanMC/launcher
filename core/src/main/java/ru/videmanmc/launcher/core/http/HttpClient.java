package ru.videmanmc.launcher.core.http;

import ru.videmanmc.launcher.core.model.value.files.DownloadedFile;

public interface HttpClient {

    DownloadedFile download(String url);
}
