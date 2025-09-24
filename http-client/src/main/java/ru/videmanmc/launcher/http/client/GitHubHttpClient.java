package ru.videmanmc.launcher.http.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ru.videmanmc.launcher.http.client.domain.entity.Binary;
import ru.videmanmc.launcher.http.client.domain.value.BinaryInfo;
import ru.videmanmc.launcher.http.client.domain.value.GameFile;
import ru.videmanmc.launcher.http.client.domain.value.Hash;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.util.function.UnaryOperator;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class GitHubHttpClient implements GameFilesClient, BinaryClient {

    public static final String RAW_CONTENT_MIME = "application/vnd.github.raw+json";

    private static final String MODPACK_BASE_URL = "https://api.github.com/repos/VidemanMC/modpack";

    private static final String DOWNLOAD_URI_TEMPLATE = MODPACK_BASE_URL + "/contents/{path}";

    private static final String LAUNCHER_BASE_URL = "https://api.github.com/repos/VidemanMC/launcher";

    private static final String LATEST_RELEASES_URI = LAUNCHER_BASE_URL + "/releases/latest";


    private final HttpClient httpClient = HttpClient.newBuilder()
                                                    .followRedirects(HttpClient.Redirect.ALWAYS)
                                                    .build();

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
    public GameFile download(String filePath, UnaryOperator<String> abstractPathFormatting) {
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
                              )
                              .body();
        var abstractFilePath = abstractPathFormatting.apply(filePath);

        return new GameFile(bytes, abstractFilePath);
    }

    private String encodeUri(String original) {
        return URLEncoder.encode(original, Charset.defaultCharset());
    }

    @Override
    @SneakyThrows
    public BinaryInfo getInfoByPrefix(String namePrefix) {
        var uri = URI.create(LATEST_RELEASES_URI);
        var request = httpBuilder.uri(uri)
                                 .build();
        var rawReleaseInfo = httpClient.send(
                                               request,
                                               HttpResponse.BodyHandlers.ofString()
                                       )
                                       .body();
        var jsonNode = objectMapper.readTree(rawReleaseInfo);

        return constructBinary(jsonNode, namePrefix);
    }

    private BinaryInfo constructBinary(JsonNode releaseNode, String namePrefix) {
        return releaseNode.get("assets")
                          .valueStream()
                          .filter(jsonAsset -> {
                              var assetName = jsonAsset.get("name")
                                                       .asText();
                              return assetName.startsWith(namePrefix);
                          })
                          .map(jsonAsset -> {
                              var name = jsonAsset.get("name")
                                                  .asText();
                              var downloadUrl = jsonAsset.get("browser_download_url")
                                                         .asText();
                              var hash = jsonAsset.get("digest")
                                                  .asText();

                              return new BinaryInfo(name, downloadUrl, hash);
                          })
                          .findFirst()
                          .orElseThrow(() -> new IllegalStateException("Asset '" + namePrefix + "*' is not found on GitHub"));
    }

    @SneakyThrows
    public Binary getBinary(BinaryInfo info) {
        var request = HttpRequest.newBuilder(URI.create(
                                         info.downloadUrl()
                                 ))
                                 .build();
        HttpResponse<byte[]> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofByteArray()
        );
        var hash = trim(info.hash());

        return new Binary(
                new Hash(hash),
                response.body(),
                info.name()
        );
    }

    private String trim(String hash) {
        var githubAlgNamePrefixIndex = 7;
        return hash.substring(githubAlgNamePrefixIndex);
    }
}
