package ru.videmanmc.launcher.http.client.model.value;

import java.util.List;

/**
 * Represents a release on GitHub
 */
public record Release(int id, List<Asset> assets) {

    /**
     * Represents an asset in the release.
     */
    public record Asset(String name, String contentType, String downloadUrl) {}

}
