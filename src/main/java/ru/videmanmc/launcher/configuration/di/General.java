package ru.videmanmc.launcher.configuration.di;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import lombok.SneakyThrows;
import ru.videmanmc.launcher.dto.LauncherVersion;
import ru.videmanmc.launcher.factory.FilesChecksumFactory;
import ru.videmanmc.launcher.dto.BearerToken;
import ru.videmanmc.launcher.http.GitHubHttpClient;
import ru.videmanmc.launcher.mapper.PathFormatMapper;
import ru.videmanmc.launcher.model.entity.Client;
import ru.videmanmc.launcher.model.value.files.ignored.IgnoredFiles;
import ru.videmanmc.launcher.model.value.Settings;
import ru.videmanmc.launcher.model.value.files.GitHubFiles;
import ru.videmanmc.launcher.model.value.files.LocalFiles;
import ru.videmanmc.launcher.model.value.files.RemoteFiles;
import ru.videmanmc.launcher.repository.ClientRepository;
import ru.videmanmc.launcher.repository.ClientRepositoryImp;
import ru.videmanmc.launcher.repository.SettingsRepository;
import ru.videmanmc.launcher.service.ClientService;
import ru.videmanmc.launcher.service.hashing.HashingService;
import ru.videmanmc.launcher.service.hashing.Md5HashingService;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.Properties;

import static ru.videmanmc.launcher.http.GitHubHttpClient.RAW_CONTENT_MIME;

@SuppressWarnings("unused")
public class General extends AbstractModule {

    @Override
    protected void configure() {
        bind(Client.class);
        bind(FilesChecksumFactory.class);
        bind(LocalFiles.class);
        bind(RemoteFiles.class).to(GitHubFiles.class);

        bind(SettingsRepository.class);
        bind(ClientRepository.class).to(ClientRepositoryImp.class);

        bind(ClientService.class);
        bind(PathFormatMapper.class).in(Singleton.class);
        bind(HashingService.class).to(Md5HashingService.class);

        bind(ru.videmanmc.launcher.http.HttpClient.class).to(GitHubHttpClient.class);
    }

    @Provides
    ObjectMapper objectMapper() {
        return YAMLMapper.builder()
                .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
                .propertyNamingStrategy(PropertyNamingStrategies.KebabCaseStrategy.INSTANCE)
                .build();
    }

    @Provides
    @SneakyThrows
    LauncherVersion launcherVersion(Properties properties) {
        return new LauncherVersion(properties.getProperty("version"));
    }

    @Provides
    BearerToken authToken(Properties properties) {
        return new BearerToken(properties.getProperty("auth_token"));
    }

    @Provides
    @Singleton
    Properties properties() throws IOException {
        var props = new Properties();
        props.load(
                getClass().getResourceAsStream("/info.properties")
        );

        return props;
    }

    @Provides
    HttpRequest.Builder httpRequestBuilder(BearerToken bearerToken) {
        return HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .header("Accept", RAW_CONTENT_MIME)
                .header("Authorization", bearerToken.bearerToken());
    }

    @Provides
    IgnoredFiles ignoredFiles(RemoteFiles remoteFiles) {
        return remoteFiles.getIgnoredFiles();
    }

    @Provides
    @Singleton
    @SneakyThrows
    Settings settings(SettingsRepository settingsRepository) {
        return settingsRepository.load();
    }

}