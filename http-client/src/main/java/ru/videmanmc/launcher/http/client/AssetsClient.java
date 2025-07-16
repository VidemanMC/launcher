package ru.videmanmc.launcher.http.client;

import ru.videmanmc.launcher.http.client.domain.value.Asset;

/**
 * Used for retrieving new launcher assets
 */
public interface AssetsClient {

    Asset downloadAsset(String namePrefix);

}
