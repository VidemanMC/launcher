package ru.videmanmc.launcher.dto.http;

public record BearerToken(String bearerToken) {

    public BearerToken(String bearerToken) {
        this.bearerToken = "Bearer " + bearerToken;
    }

}
