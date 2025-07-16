package ru.videmanmc.launcher.http.client.domain.value;

/**
 * Represents an asset in a release on GitHub.
 */
public record Asset(String name, String downloadUrl, int id) {

}
