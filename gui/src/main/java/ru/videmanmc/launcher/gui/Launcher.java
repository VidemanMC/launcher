package ru.videmanmc.launcher.gui;

import com.google.inject.Guice;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ru.videmanmc.launcher.core.configuration.GeneralConfiguration;
import ru.videmanmc.launcher.core.dto.LauncherVersion;
import ru.videmanmc.launcher.core.repository.SettingsRepository;
import ru.videmanmc.launcher.gui.component.MainScreen;
import ru.videmanmc.launcher.http.client.configuration.HttpGuiceConfiguration;

import java.io.IOException;

public class Launcher extends Application { //todo rewrite it with clojure

    private SettingsRepository settingsRepository; //todo pass settings in a less coupled way

    private LauncherVersion launcherVersion;

    private MainScreen mainScreen;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() {
        var di = Guice.createInjector(new GeneralConfiguration(), new HttpGuiceConfiguration());

        this.settingsRepository = di.getInstance(SettingsRepository.class);
        this.launcherVersion = di.getInstance(LauncherVersion.class);
        this.mainScreen = di.getInstance(MainScreen.class);
    }

    @Override
    public void start(Stage stage) {
        settingsRepository.getOrLoad();

        stage.setTitle("VidemanMC " + launcherVersion.version());
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/icon64.png")));

        mainScreen.show(stage);
    }

    @Override
    public void stop() throws IOException {
        settingsRepository.unload();
    }
}
