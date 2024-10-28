package ru.videmanmc.launcher.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import io.github.amayaframework.di.ProviderBuilders;
import io.github.amayaframework.di.ServiceProvider;
import lombok.SneakyThrows;
import ru.videmanmc.launcher.dto.LauncherInfo;
import ru.videmanmc.launcher.repository.SettingsRepository;

import java.util.Properties;

//todo split into multiple configs
public class DIConfiguration {

    public ServiceProvider initDependencies() {
        return ProviderBuilders.createChecked()
                .addSingleton(SettingsRepository.class)
                .addInstance(ObjectMapper.class, objectMapper())
                .addInstance(LauncherInfo.class, launcherInfo())

                .build();
    }

    private ObjectMapper objectMapper() {
        return YAMLMapper.builder()
                .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
                .build();
    }

    @SneakyThrows
    private LauncherInfo launcherInfo() {
        var props = new Properties();
        props.load(getClass().getResourceAsStream("/info.properties"));

        return new LauncherInfo(props.getProperty("version"));
    }

}