package ru.videmanmc.launcher.core.dto;

public record BearerToken(String bearerToken) {

    public BearerToken(String bearerToken) {
        this.bearerToken = "Bearer " + bearerToken;
    }

}
