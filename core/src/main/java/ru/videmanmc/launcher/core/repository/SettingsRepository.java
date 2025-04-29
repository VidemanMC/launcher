package ru.videmanmc.launcher.core.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.videmanmc.launcher.core.model.value.Settings;

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

    public Settings load() throws IOException {
        if (Files.notExists(file.toPath())) {
            this.settings = new Settings();
            return this.settings;
        }

        this.settings = objectMapper.readValue(file, Settings.class);
        return this.settings;
    }

    public void unload() throws IOException {
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
