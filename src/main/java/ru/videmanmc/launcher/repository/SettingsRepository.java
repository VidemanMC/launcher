package ru.videmanmc.launcher.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.videmanmc.launcher.dto.Settings;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RequiredArgsConstructor
public class SettingsRepository {

    private final ObjectMapper objectMapper;

    private final File file = new File(Settings.MAIN_DIRECTORY_PATH, Settings.LAUNCHER_CONFIG_FILE_NAME);

    @Getter
    private Settings settings;

    public void load() throws IOException {
        if (settingsNotExists()) {
            this.settings = new Settings();
            return;
        }

        this.settings = objectMapper.readValue(file, Settings.class);
    }

    public void unload() throws IOException {
        if (settingsNotExists()) {
            createConfigDirectory();
        }

        objectMapper.writeValue(file, settings);
    }

    private boolean settingsNotExists() {
        return !Files.exists(file.toPath());
    }

    private void createConfigDirectory() throws IOException {
        Files.createDirectory(Path.of(Settings.MAIN_DIRECTORY_PATH));
    }
}
