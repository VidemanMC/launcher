package ru.videmanmc.launcher.http.client;

import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ru.videmanmc.launcher.http.client.model.value.DownloadedFile;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Function;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class GitHubHttpClient implements ru.videmanmc.launcher.http.client.HttpClient {

    public static final String RAW_CONTENT_MIME = "application/vnd.github.raw+json";

    private static final String DOWNLOAD_URL_TEMPLATE = "https://api.github.com/repos/VidemanMC/modpack/contents/{path}";

    private final HttpClient httpClient = HttpClient.newHttpClient();

    private final HttpRequest.Builder httpBuilder;

    @Override
    @SneakyThrows
    public DownloadedFile download(String filePath, Function<String, String> abstractPathFormatting) {
        var uri = URI.create(
                DOWNLOAD_URL_TEMPLATE.replace(
                        "{path}",
                        encodeUriPath(filePath)
                )
        );
        var request = httpBuilder.uri(uri).build();
        var bytes = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray()).body();

        var abstractFilePath = abstractPathFormatting.apply(filePath);

        return new DownloadedFile(bytes, abstractFilePath);
    }

    private String encodeUriPath(String original) {
        return original.replace(" ", "%20");
    }
}
