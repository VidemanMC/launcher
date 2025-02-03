package ru.videmanmc.launcher;

import com.google.inject.Guice;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ru.videmanmc.launcher.configuration.di.General;
import ru.videmanmc.launcher.dto.LauncherVersion;
import ru.videmanmc.launcher.gui.component.MainScreen;
import ru.videmanmc.launcher.repository.SettingsRepository;

import java.io.IOException;

public class Launcher extends Application {

    private SettingsRepository settingsRepository;
    private LauncherVersion launcherVersion;

    private MainScreen mainScreen;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() {
        var di = Guice.createInjector(new General());

        this.settingsRepository = di.getInstance(SettingsRepository.class);
        this.launcherVersion = di.getInstance(LauncherVersion.class);
        this.mainScreen = di.getInstance(MainScreen.class);
    }

    @Override
    public void start(Stage stage) throws IOException {
        settingsRepository.load();

        stage.setTitle("VidemanMC %s".formatted(
                launcherVersion.version()
        ));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));

        mainScreen.show(stage);
    }

    @Override
    public void stop() throws IOException {
        settingsRepository.unload();
    }
}
