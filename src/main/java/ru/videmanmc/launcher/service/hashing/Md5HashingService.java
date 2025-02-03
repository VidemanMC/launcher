package ru.videmanmc.launcher.service.hashing;

import lombok.SneakyThrows;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
