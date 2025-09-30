package ru.videmanmc.launcher.http_client.configuration;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import ru.videmanmc.launcher.dto.http.BearerToken;
import ru.videmanmc.launcher.http_client.Md5HashingService;
import ru.videmanmc.launcher.http_client.github.*;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.Properties;

@SuppressWarnings("unused")
public class HttpGuiceConfiguration extends AbstractModule {

    @Override
    protected void configure() {
        bind(GameFilesClient.class)
                .to(GitHubHttpClient.class)
                .in(Singleton.class);
        bind(BinaryClient.class)
                .to(GitHubHttpClient.class)
                .in(Singleton.class);
        bind(RemoteChecksumCalculator.class)
                .to(GitHubHttpClient.class)
                .in(Singleton.class);

        bind(HashingService.class).to(Md5HashingService.class);
        bind(PathFormatMapper.class);
    }

    @Provides
    HttpRequest.Builder authorizedHttpRequestBuilder(BearerToken bearerToken) {
        return HttpRequest.newBuilder()
                          .version(HttpClient.Version.HTTP_1_1)
                          .header("Accept", GitHubHttpClient.RAW_CONTENT_MIME)
                          .header("Authorization", bearerToken.bearerToken());
    }

    @Provides
    BearerToken authToken(@AuthProperties Properties properties) {
        return new BearerToken(properties.getProperty("auth_token"));
    }

    @Provides
    @AuthProperties
    @Singleton
    Properties properties() throws IOException {
        var props = new Properties();
        props.load(getClass()
                           .getResourceAsStream("/credentials.properties"));

        return props;
    }
}
