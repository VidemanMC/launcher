package ru.videmanmc.launcher.dto.http;

/**
 * A launcher`s binary.
 */
public record Binary(Hash hash, byte[] content, String name) {

}
