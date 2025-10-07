package ru.videmanmc.launcher.dto.http;

/**
 * Represents file of a game client.
 * @param contents file data
 * @param fullPath full file name with directory parent and extension
 */
public record GameFile(byte[] contents, String fullPath) {
}
