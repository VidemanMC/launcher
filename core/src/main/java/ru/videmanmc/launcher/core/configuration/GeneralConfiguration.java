package ru.videmanmc.launcher.core.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import lombok.SneakyThrows;
import ru.videmanmc.launcher.core.dto.LauncherVersion;
import ru.videmanmc.launcher.core.factory.FilesChecksumFactory;
import ru.videmanmc.launcher.core.factory.RemotePathFactory;
import ru.videmanmc.launcher.core.mapper.PathFormatMapper;
import ru.videmanmc.launcher.core.model.entity.Client;
import ru.videmanmc.launcher.core.model.value.Settings;
import ru.videmanmc.launcher.core.model.value.SyncSettings;
import ru.videmanmc.launcher.core.model.value.files.GitHubFiles;
import ru.videmanmc.launcher.core.model.value.files.IgnoredFiles;
import ru.videmanmc.launcher.core.model.value.files.LocalFiles;
import ru.videmanmc.launcher.core.model.value.files.RemoteFiles;
import ru.videmanmc.launcher.core.repository.ClientRepository;
import ru.videmanmc.launcher.core.repository.SettingsRepository;
import ru.videmanmc.launcher.core.service.ClientService;
import ru.videmanmc.launcher.core.service.GameRunningService;
import ru.videmanmc.launcher.core.service.assets.JmcccMinecraftCoreService;
import ru.videmanmc.launcher.core.service.assets.MinecraftCoreService;
import ru.videmanmc.launcher.core.service.hashing.HashingService;
import ru.videmanmc.launcher.core.service.hashing.Md5HashingService;
import ru.videmanmc.launcher.http.client.GameFilesClient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@SuppressWarnings("unused")
public class GeneralConfiguration extends AbstractModule {

    @Override
    protected void configure() {
        bind(Client.class);
        bind(FilesChecksumFactory.class);
        bind(LocalFiles.class);
        bind(RemoteFiles.class)
                .to(GitHubFiles.class)
                .in(Singleton.class);

        bind(SettingsRepository.class)
                .in(Singleton.class);
        bind(ClientRepository.class);

        bind(ClientService.class);
        bind(PathFormatMapper.class);
        bind(RemotePathFactory.class);
        bind(HashingService.class).to(Md5HashingService.class);
        bind(MinecraftCoreService.class).to(JmcccMinecraftCoreService.class);
        bind(GameRunningService.class);
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
    LauncherVersion launcherVersion(@InfoProperties Properties properties) {
        return new LauncherVersion(properties.getProperty("version"));
    }

    @Provides
    @InfoProperties
    @Singleton
    Properties properties() throws IOException {
        var props = new Properties();
        props.load(
                getClass().getResourceAsStream("/info.properties")
        );

        return props;
    }

    @Provides
    IgnoredFiles ignoredFiles(SyncSettings syncSettings) {
        return new IgnoredFiles(syncSettings.updateExclude());
    }

    @Provides
    @Singleton
    SyncSettings syncSettings(ObjectMapper objectMapper, GameFilesClient gameFilesClient) throws JsonProcessingException {
        var downloadedBytes = gameFilesClient.download(GitHubFiles.SYNC_SETTINGS)
                                             .contents(); //todo Exit method if offline mode is enabled

        return objectMapper.readValue(
                new String(downloadedBytes, StandardCharsets.UTF_8),
                SyncSettings.class
        );
    }

    @Provides
    Settings settings(SettingsRepository settingsRepository) {
        return settingsRepository.getOrLoad();
    }

}