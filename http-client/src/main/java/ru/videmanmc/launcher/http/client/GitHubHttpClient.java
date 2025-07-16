package ru.videmanmc.launcher.http.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ru.videmanmc.launcher.http.client.domain.value.Asset;
import ru.videmanmc.launcher.http.client.domain.value.DownloadedFile;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Function;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class GitHubHttpClient implements GameFilesClient, AssetsClient {

    public static final String RAW_CONTENT_MIME = "application/vnd.github.raw+json";

    private static final String BASE_URL = "https://api.github.com/repos/VidemanMC/modpack";

    private static final String DOWNLOAD_URI_TEMPLATE = BASE_URL + "/contents/{path}";

    private static final String LATEST_RELEASES_URI = BASE_URL + "/releases/latest";

    private final HttpClient httpClient = HttpClient.newHttpClient();

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final HttpRequest.Builder httpBuilder;

    /*
    todo КАКОГО ХРЕНА?

        Почему внешняя система заадёт форму моего интерфейса?
    Почему метод download яано декларирует: я скачива файлы И преобразую пути. Потребность в преобразовании путей возникла из-за гитхаба, а не ВООБЩЕ.

        Поэтому интерфейс метода нужно упростить: убрать abstractPathFormatting, see https://github.com/VidemanMC/launcher/issues/11
     */

    @Override
    @SneakyThrows
    public DownloadedFile download(String filePath, Function<String, String> abstractPathFormatting) {
        var uri = URI.create(
                DOWNLOAD_URI_TEMPLATE.replace(
                        "{path}",
                        encodeUri(filePath)
                )
        );
        var request = httpBuilder.uri(uri)
                .build();
        var bytes = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofByteArray()
        ).body();
        var abstractFilePath = abstractPathFormatting.apply(filePath);

        return new DownloadedFile(bytes, abstractFilePath);
    }

    private String encodeUri(String original) {
        return original.replace(" ", "%20");
    }

    @Override
    @SneakyThrows
    public Asset downloadAsset(String namePrefix) {
        var uri = URI.create(LATEST_RELEASES_URI);
        var request = httpBuilder.uri(uri)
                .build();
        var rawReleaseInfo = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        ).body();
        var jsonNode = objectMapper.readTree(rawReleaseInfo);

        return constructAsset(jsonNode, namePrefix);
    }

    private Asset constructAsset(JsonNode releaseNode, String namePrefix) {
        return releaseNode.get("assets")
                .valueStream()
                .filter(jsonAsset -> {
                    var assetName = jsonAsset.get("name").asText();
                    return assetName.startsWith(namePrefix);
                })
                .map(jsonAsset -> {
                    var name = jsonAsset.get("name").asText();
                    var downloadUrl = jsonAsset.get("browser_download_url").asText();
                    var id = jsonAsset.get("id").asInt();

                    return new Asset(name, downloadUrl, id);
                })
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Asset '" + namePrefix + "' is not found on GitHub"));
    }
}
