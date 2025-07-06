package ru.videmanmc.launcher.http.client.di;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import ru.videmanmc.launcher.http.client.ContentsClient;
import ru.videmanmc.launcher.http.client.GitHubHttpClient;
import ru.videmanmc.launcher.http.client.ReleasesClient;
import ru.videmanmc.launcher.http.client.model.value.BearerToken;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.util.Properties;

@SuppressWarnings("unused")
public class HttpDependencies extends AbstractModule {

    @Override
    protected void configure() {
        bind(ContentsClient.class)
                .to(GitHubHttpClient.class)
                .in(Singleton.class);
        bind(ReleasesClient.class)
                .to(GitHubHttpClient.class)
                .in(Singleton.class);
    }

    @Provides
    HttpRequest.Builder httpRequestBuilder(BearerToken bearerToken) {
        return HttpRequest.newBuilder()
                .version(java.net.http.HttpClient.Version.HTTP_2)
                .header("Accept", GitHubHttpClient.RAW_CONTENT_MIME)
                .header("Authorization", bearerToken.bearerToken());
    }

    @Provides
    BearerToken authToken(Properties properties) {
        return new BearerToken(properties.getProperty("auth_token"));
    }

    @Provides
    @Singleton
    Properties properties() throws IOException {
        var props = new Properties();
        props.load(getClass()
                .getResourceAsStream("/credentials.properties"));

        return props;
    }
}
