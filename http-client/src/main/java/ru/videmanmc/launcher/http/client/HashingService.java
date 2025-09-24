package ru.videmanmc.launcher.http.client;

public interface HashingService {

    String calculateHash(byte[] raw);
}
