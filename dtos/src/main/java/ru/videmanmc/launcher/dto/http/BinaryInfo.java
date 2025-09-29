package ru.videmanmc.launcher.dto.http;

/**
 * Info about a remote binary, not the binary itself
 */
public record BinaryInfo(String name, String downloadUrl, String hash) {

}
