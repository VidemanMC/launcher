package ru.videmanmc.launcher.http.client.domain.value;

/**
 * Info about a remote binary, not the binary itself
 */
public record BinaryInfo(String name, String downloadUrl, String hash) {

}
