package ru.videmanmc.launcher.core.http;

import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ru.videmanmc.launcher.core.mapper.PathFormatMapper;
import ru.videmanmc.launcher.core.model.value.files.DownloadedFile;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class GitHubHttpClient implements ru.videmanmc.launcher.core.http.HttpClient {

    public static final String RAW_CONTENT_MIME = "application/vnd.github.raw+json";

    private static final String DOWNLOAD_URL_TEMPLATE = "https://api.github.com/repos/VidemanMC/modpack/contents/{path}";

    private final HttpClient httpClient = HttpClient.newHttpClient();

    private final HttpRequest.Builder httpBuilder;

    private final PathFormatMapper pathFormatMapper;

    @Override
    @SneakyThrows
    public DownloadedFile download(String filePath) {
        var uri = URI.create(
                DOWNLOAD_URL_TEMPLATE.replace(
                        "{path}",
                        encodeUriPath(filePath)
                )
        );
        var request = httpBuilder.uri(uri).build();
        var bytes = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray()).body();

        var abstractFilePath = pathFormatMapper.remoteToAbstractFormat(filePath);

        System.out.println("Downloaded " + abstractFilePath);

        return new DownloadedFile(bytes, abstractFilePath);
    }

    private String encodeUriPath(String original) {
        return original.replace(" ", "%20");
    }
}
