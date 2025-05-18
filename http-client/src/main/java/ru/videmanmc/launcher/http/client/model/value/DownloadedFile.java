package ru.videmanmc.launcher.http.client.model.value;

/**
 * Represents file being downloaded.
 * @param contents file data
 * @param filePath full file name with directory parent and extension
 */
public record DownloadedFile(byte[] contents, String filePath) {
}
