package ru.videmanmc.launcher.model.value.files;

import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import ru.videmanmc.launcher.factory.FilesChecksumFactory;
import ru.videmanmc.launcher.http.HttpClient;
import ru.videmanmc.launcher.mapper.PathFormatMapper;
import ru.videmanmc.launcher.model.value.FilesChecksum;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class GitHubFiles implements RemoteFiles {

    private final static String HASH = "hash.txt";
    public final static String SYNC_SETTINGS = "sync-settings.yml";

    public final static String PATH_HASH_SEPARATOR = ":";

    private final HttpClient httpClient;

    private final PathFormatMapper pathFormatMapper;

    private final FilesChecksumFactory filesChecksumFactory;

    private FilesChecksum cachedChecksum;

    private List<String> cachedRemoteFileNames;

    @Override
    public List<DownloadedFile> download(List<String> abstractFileNames) {
        var mappedFileNames = pathFormatMapper.abstractToRemoteFormat(abstractFileNames, cachedRemoteFileNames);

        return mappedFileNames.stream()
                .map(httpClient::download)
                .toList();
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
                    int semicolonIndex = str.indexOf(PATH_HASH_SEPARATOR);
                    return str.substring(0, semicolonIndex);
                })
                .toList();
    }
}
