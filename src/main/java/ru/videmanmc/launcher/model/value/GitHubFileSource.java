package ru.videmanmc.launcher.model.value;

import lombok.RequiredArgsConstructor;

import java.net.http.HttpClient;
import java.util.Map;

@RequiredArgsConstructor
public class GitHubFileSource implements RemoteFileSource {

    private final HttpClient httpClient;

    @Override
    public void synchronize(LocalFiles localFiles) {

    }

    @Override
    public Map<String, String> getFiles() {
        return Map.of();
    }
}
