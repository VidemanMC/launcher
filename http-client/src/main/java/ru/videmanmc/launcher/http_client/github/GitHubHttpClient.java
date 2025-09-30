package ru.videmanmc.launcher.http_client.github;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.uwyn.urlencoder.UrlEncoder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ru.videmanmc.launcher.dto.http.*;
import ru.videmanmc.launcher.http_client.exception.HttpDownloadException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class GitHubHttpClient implements GameFilesClient, BinaryClient, RemoteChecksumCalculator {

    public static final String RAW_CONTENT_MIME = "application/vnd.github.raw+json";

    private static final String MODPACK_BASE_URL = "https://api.github.com/repos/VidemanMC/modpack";

    private static final String DOWNLOAD_URI_TEMPLATE = MODPACK_BASE_URL + "/contents/{path}";

    private static final String LAUNCHER_BASE_URL = "https://api.github.com/repos/VidemanMC/launcher";

    private static final String LATEST_RELEASES_URI = LAUNCHER_BASE_URL + "/releases/latest";

    private static final String FILE_NAME_HASHES = "hash.txt";

    public static final String SYNC_SETTINGS = "sync-settings.yml";

    public static final String PATH_HASH_SEPARATOR = ":";

    private final HttpClient httpClient = HttpClient.newBuilder()
                                                    .followRedirects(HttpClient.Redirect.ALWAYS)
                                                    .build();

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final HttpRequest.Builder httpBuilder;

    private final PathFormatMapper pathFormatMapper;

    private final FilesChecksumFactory filesChecksumFactory;

    @Override
    @SneakyThrows
    public GameFile download(String filePath) throws HttpDownloadException {
        var uri = URI.create(
                DOWNLOAD_URI_TEMPLATE.replace(
                        "{path}",
                        UrlEncoder.encode(filePath)
                ));
        var request = httpBuilder.uri(uri)
                                 .build();
        byte[] response = httpClient.send(
                                            request,
                                            HttpResponse.BodyHandlers.ofByteArray()
                                    )
                                    .body();
        validateStatusCode(response);

        var abstractFilePath = pathFormatMapper.remoteToAbstractFormat(filePath);

        return new GameFile(response, abstractFilePath);
    }

    @Override
    @SneakyThrows
    public BinaryInfo getInfoByPrefix(String namePrefix) throws HttpDownloadException {
        var uri = URI.create(LATEST_RELEASES_URI);
        var request = httpBuilder.uri(uri)
                                 .build();
        byte[] response = httpClient.send(
                                            request,
                                            HttpResponse.BodyHandlers.ofByteArray()
                                    )
                                    .body();
        validateStatusCode(response);

        var jsonNode = objectMapper.readTree(response);

        return constructBinaryInfo(jsonNode, namePrefix);
    }

    private BinaryInfo constructBinaryInfo(JsonNode releaseNode, String namePrefix) {
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
    public Binary getBinary(BinaryInfo info) throws HttpDownloadException {
        var request = HttpRequest.newBuilder(URI.create(
                                         info.downloadUrl()
                                 ))
                                 .build();
        byte[] response = httpClient.send(
                                            request,
                                            HttpResponse.BodyHandlers.ofByteArray()
                                    )
                                    .body();

        validateStatusCode(response);

        var hash = trim(info.hash());

        return new Binary(
                new Hash(hash),
                response,
                info.name()
        );
    }

    private String trim(String hash) {
        var githubAlgNamePrefixIndex = 7;
        return hash.substring(githubAlgNamePrefixIndex);
    }

    /**
     * Validate status codes referencing <a href="https://docs.github.com/en/rest/using-the-rest-api/troubleshooting-the-rest-api?apiVersion=2022-11-28">GitHub Rest Api Docs</a>
     */
    private void validateStatusCode(byte[] response) throws HttpDownloadException {
        try {
            var json = objectMapper.readTree(response);
            var statusNode = json.get("status");

            if (statusNode == null) {
                return;
            }

            var status = statusNode.asText();
            switch (status) {
                case "403", "429" -> throw new HttpDownloadException();
                case "404" -> throw new IllegalStateException("Existing resource not found");
                default -> {
                    // ok, keep working
                }
            }
        } catch (JsonProcessingException e) {
            // ok, content is binary file
        } catch (IOException e) {
            throw new HttpDownloadException();
        }
    }

    @Override
    public List<String> getFilePaths() {
        return getCachedFilePaths();
    }

    private List<String> cachedFilePaths;

    private List<String> getCachedFilePaths() {
        if (cachedFilePaths == null) {
            cachedFilePaths = getCachedNameHashPairs().stream()
                                                      .map(this::extractNamePart)
                                                      .toList();
        }

        return cachedFilePaths;
    }

    private String extractNamePart(String nameHashPair) {
        int semicolonIndex = nameHashPair.indexOf(PATH_HASH_SEPARATOR);
        return nameHashPair.substring(0, semicolonIndex);
    }

    private FilesChecksum cachedChecksum;

    @Override
    public FilesChecksum calculateChecksum() {
        if (cachedChecksum != null) {
            return cachedChecksum;
        }
        this.cachedChecksum = filesChecksumFactory.ofRemote(getCachedNameHashPairs());

        return this.cachedChecksum;
    }

    private List<String> cachedHashNamePairs;

    private List<String> getCachedNameHashPairs() {
        if (cachedHashNamePairs == null) {
            cachedHashNamePairs = downloadNameHashPairs();
        }

        return cachedHashNamePairs;
    }

    @SneakyThrows
    private List<String> downloadNameHashPairs() {
        var downloadedHashesString = new String(
                download(FILE_NAME_HASHES)
                        .contents(), StandardCharsets.UTF_8
        );
        return Stream.of(downloadedHashesString.split("\n"))
                     .toList();
    }
}
