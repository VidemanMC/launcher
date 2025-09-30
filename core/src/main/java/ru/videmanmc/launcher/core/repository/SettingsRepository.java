package ru.videmanmc.launcher.core.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ru.videmanmc.launcher.dto.Settings;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static ru.videmanmc.launcher.constants.WorkingDirectoryConstants.LAUNCHER_CONFIG_FILE_NAME;
import static ru.videmanmc.launcher.constants.WorkingDirectoryConstants.MAIN_DIRECTORY_PATH;

@Getter
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class SettingsRepository {

    private final ObjectMapper objectMapper;

    private final File file = new File(MAIN_DIRECTORY_PATH, LAUNCHER_CONFIG_FILE_NAME);

    private Settings settings;

    /**
     * @return new or singleton {@link Settings}
     */
    @SneakyThrows
    public Settings getOrLoad() {
        if (settings != null) {
            return settings;
        }

        return load();
    }

    @SneakyThrows
    public Settings load() {
        if (Files.notExists(file.toPath())) {
            settings = new Settings();
            return settings;
        }

        settings = objectMapper.readValue(file, Settings.class);
        return settings;
    }

    @SneakyThrows
    public void unload(Settings settings) {
        unload0(settings);
    }

    public void unload() {
       unload0(settings);
    }

    @SneakyThrows
    private void unload0(Settings settings) {
        createConfigDirectory();

        objectMapper.writeValue(file, settings);
    }

    private void createConfigDirectory() throws IOException {
        if (Files.exists(file.toPath().getParent())) {
            return;
        }
        Files.createDirectory(Path.of(MAIN_DIRECTORY_PATH));
    }
}
