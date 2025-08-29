package ru.videmanmc.launcher.http.client.domain.entity;


import ru.videmanmc.launcher.http.client.domain.value.Hash;

/**
 * A launcher`s binary.
 */
public record Binary(Hash hash, byte[] content, String name) {

}
