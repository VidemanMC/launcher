package ru.videmanmc.launcher.http.client.domain.value;

public record BearerToken(String bearerToken) {

    public BearerToken(String bearerToken) {
        this.bearerToken = "Bearer " + bearerToken;
    }

}
