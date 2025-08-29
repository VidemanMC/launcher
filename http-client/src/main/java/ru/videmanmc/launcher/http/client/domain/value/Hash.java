package ru.videmanmc.launcher.http.client.domain.value;

import lombok.NonNull;
import ru.videmanmc.launcher.http.client.domain.entity.Binary;

/**
 * A hash of a {@link Binary}. <br>
 * A hash evaluated based on {@link Binary}, but can exist after the binary was deleted.
 */
public record Hash(@NonNull String hash) {

}
