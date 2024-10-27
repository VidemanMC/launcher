package ru.videmanmc.launcher.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import io.github.amayaframework.di.ProviderBuilders;
import io.github.amayaframework.di.ServiceProvider;
import ru.videmanmc.launcher.repository.SettingsRepository;

//todo split into multiple configs
public class DIConfiguration {

    public ServiceProvider initDependencies() {
        return ProviderBuilders.createChecked()
                .addSingleton(SettingsRepository.class)
                .addInstance(ObjectMapper.class, objectMapper())
                .build();
    }

    private ObjectMapper objectMapper() {
        return YAMLMapper.builder()
                .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
                .build();
    }

}