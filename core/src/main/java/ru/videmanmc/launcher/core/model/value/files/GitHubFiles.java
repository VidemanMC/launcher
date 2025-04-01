package ru.videmanmc.launcher.core.model.value.files;

import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import ru.videmanmc.launcher.core.factory.FilesChecksumFactory;
import ru.videmanmc.launcher.core.http.HttpClient;
import ru.videmanmc.launcher.core.model.value.FilesChecksum;
import ru.videmanmc.launcher.core.model.value.RemotePath;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class GitHubFiles implements RemoteFiles {

    private final static String FILE_NAME_HASHES = "hash.txt";

    public final static String SYNC_SETTINGS = "sync-settings.yml";

    public final static String PATH_HASH_SEPARATOR = ":";

    private final HttpClient httpClient;

    private final FilesChecksumFactory filesChecksumFactory;

    private FilesChecksum cachedChecksum;

    private List<String> cachedRemotePaths;

    @Override
    public List<DownloadedFile> download(List<RemotePath> remotePaths) {
        return remotePaths.stream()
                .map(RemotePath::path)
                .map(httpClient::download)
                .toList();
    }

    @Override
    public List<String> listRemotePaths() {
        return cachedRemotePaths;
    }

    @Override
    public FilesChecksum calculateChecksum() {
        if (cachedChecksum != null) {
            return cachedChecksum;
        }

        var downloadedHashesString = new String(httpClient.download(FILE_NAME_HASHES).contents(), StandardCharsets.UTF_8);
        var nameHashPairs = Stream.of(downloadedHashesString.split("\n")).toList();

        setCachedChecksum(nameHashPairs);
        this.cachedChecksum = filesChecksumFactory.ofRemote(nameHashPairs);

        return this.cachedChecksum;
    }

    private void setCachedChecksum(List<String> nameHashPairs) {
        this.cachedRemotePaths = nameHashPairs.stream()
                .map(str -> {
                    int semicolonIndex = str.indexOf(PATH_HASH_SEPARATOR);
                    return str.substring(0, semicolonIndex);
                })
                .toList();
    }
}
