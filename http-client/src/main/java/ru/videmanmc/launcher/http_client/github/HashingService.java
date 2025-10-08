package ru.videmanmc.launcher.http_client.github;

public interface HashingService {

    String calculateHash(byte[] raw);
}
