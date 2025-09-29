package ru.videmanmc.launcher.http_client.github;


import ru.videmanmc.launcher.dto.http.Binary;
import ru.videmanmc.launcher.dto.http.BinaryInfo;

/**
 * Used for retrieving new launcher binary
 */
public interface BinaryClient {

    BinaryInfo getInfoByPrefix(String namePrefix);

    Binary getBinary(BinaryInfo info);

}
