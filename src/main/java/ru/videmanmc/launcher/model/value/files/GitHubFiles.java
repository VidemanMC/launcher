package ru.videmanmc.launcher.model.value.files;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ru.videmanmc.launcher.http.HttpClient;
import ru.videmanmc.launcher.factory.FilesChecksumFactory;
import ru.videmanmc.launcher.model.value.FilesChecksum;
import ru.videmanmc.launcher.model.value.files.ignored.IgnoredFiles;
import ru.videmanmc.launcher.model.value.SyncSettings;
import ru.videmanmc.launcher.mapper.PathFormatMapper;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class GitHubFiles implements RemoteFiles {

    private final static String SYNC_SETTINGS = "sync-settings.yml";
    private final static String HASH = "hash.txt";

    public final static String PATH_HASH_SEPARATOR = ":";

    private final HttpClient httpClient;

    private final PathFormatMapper pathFormatMapper;

    private final FilesChecksumFactory filesChecksumFactory;

    private final ObjectMapper objectMapper;

    private FilesChecksum cachedChecksum;

    private List<String> cachedRemoteFileNames;

    @Override
    public List<DownloadedFile> download(List<String> abstractFileNames) {
        var mappedFileNames = pathFormatMapper.abstractToRemoteFormat(abstractFileNames, cachedRemoteFileNames);

        return mappedFileNames.stream()
                .map(httpClient::download)
                .toList();
    }

    @SneakyThrows
    @Override
    public IgnoredFiles getIgnoredFiles() {
        var downloadedBytes = httpClient.download(SYNC_SETTINGS).contents();
        var syncSettings = getSyncSettings(new String(downloadedBytes, StandardCharsets.UTF_8));

        return new IgnoredFiles(syncSettings.updateExclude());
    }

    @SneakyThrows
    private SyncSettings getSyncSettings(String yaml) {
        return objectMapper.readValue(yaml, SyncSettings.class);
    }

    @Override
    public FilesChecksum calculateChecksum() {
        if (cachedChecksum != null) {
            return cachedChecksum;
        }

        var downloadedHashesString = new String(httpClient.download(HASH).contents(), StandardCharsets.UTF_8);
        var nameHashPairs = Arrays.stream(downloadedHashesString.split("\n")).toList();
        setCachedChecksum(nameHashPairs);

        this.cachedChecksum = filesChecksumFactory.ofRemote(nameHashPairs);
        return this.cachedChecksum;
    }

    private void setCachedChecksum(List<String> nameHashPairs) {
        this.cachedRemoteFileNames = nameHashPairs.stream()
                .map(str -> {
                    int semicolonIndex = str.indexOf(':');
                    return str.substring(0, semicolonIndex);
                })
                .toList();
    }
}
