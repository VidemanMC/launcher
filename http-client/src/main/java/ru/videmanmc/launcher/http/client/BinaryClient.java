package ru.videmanmc.launcher.http.client;

import ru.videmanmc.launcher.http.client.domain.entity.Binary;
import ru.videmanmc.launcher.http.client.domain.value.BinaryInfo;

/**
 * Used for retrieving new launcher binary
 */
public interface BinaryClient {

    BinaryInfo getInfoByPrefix(String namePrefix);

    Binary getBinary(BinaryInfo info);

}
