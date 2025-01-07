package ru.videmanmc.launcher;

import com.google.inject.Guice;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ru.videmanmc.launcher.configuration.di.General;
import ru.videmanmc.launcher.dto.LauncherVersion;
import ru.videmanmc.launcher.gui.component.MainScreen;
import ru.videmanmc.launcher.repository.SettingsRepository;
import ru.videmanmc.launcher.service.ClientService;

import java.io.IOException;

public class Launcher extends Application {

    private SettingsRepository settingsRepository;
    private LauncherVersion launcherVersion;
    private ClientService clientService;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() {
        var di = Guice.createInjector(new General());

        this.settingsRepository = di.getInstance(SettingsRepository.class);
        this.launcherVersion = di.getInstance(LauncherVersion.class);
        this.clientService = di.getInstance(ClientService.class);
    }

    @Override
    public void start(Stage stage) throws IOException {
        settingsRepository.load();

        stage.setTitle("VidemanMC %s".formatted(
                launcherVersion.version()
        ));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));

        new MainScreen().show(stage);

        //temp
        new Thread(
                () -> clientService.runClient()
        ).start();
    }

    @Override
    public void stop() throws IOException {
        settingsRepository.unload();
    }
}
