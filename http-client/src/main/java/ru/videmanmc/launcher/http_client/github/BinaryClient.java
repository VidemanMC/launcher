package ru.videmanmc.launcher.http_client.github;


import ru.videmanmc.launcher.dto.http.Binary;
import ru.videmanmc.launcher.dto.http.BinaryInfo;
import ru.videmanmc.launcher.http_client.exception.HttpDownloadException;

/**
 * Used for retrieving new launcher binary
 */
public interface BinaryClient {

    BinaryInfo getInfoByPrefix(String namePrefix) throws HttpDownloadException;

    Binary getBinary(BinaryInfo info) throws HttpDownloadException;

}
