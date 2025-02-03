package ru.videmanmc.launcher.dto;

public record BearerToken(String bearerToken) {

    public BearerToken(String bearerToken) {
        this.bearerToken = "Bearer " + bearerToken;
    }

}
