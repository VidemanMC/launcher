package ru.videmanmc.launcher.http.client;

import ru.videmanmc.launcher.http.client.model.value.Release;

/**
 * Used for retrieving data about new launcher releases
 */
public interface ReleasesClient {

    Release searchLatestRelease();

}
