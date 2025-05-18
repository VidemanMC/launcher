package ru.videmanmc.launcher.http.client.model.value;

public record BearerToken(String bearerToken) {

    public BearerToken(String bearerToken) {
        this.bearerToken = "Bearer " + bearerToken;
    }

}
