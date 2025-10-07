package ru.videmanmc.launcher.dto.http;

import lombok.NonNull;

/**
 * A hash of a {@link Binary}. <br>
 * A hash evaluated based on {@link Binary}, but can exist after the binary was deleted.
 */
public record Hash(@NonNull String hash) {

}
