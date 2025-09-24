package ru.videmanmc.launcher.core.service.hashing;

import lombok.SneakyThrows;
import ru.videmanmc.launcher.http.client.HashingService;

import java.math.BigInteger;
import java.security.MessageDigest;

public class Md5HashingService implements HashingService {

    private final MessageDigest messageDigest;

    @SneakyThrows
    public Md5HashingService() {
        this.messageDigest = MessageDigest.getInstance("MD5");
    }

    @Override
    public String calculateHash(byte[] raw) {
        var hashedBytes = messageDigest.digest(raw);
        var bigInteger = new BigInteger(1, hashedBytes);

        return bigInteger.toString(16);
    }
}
