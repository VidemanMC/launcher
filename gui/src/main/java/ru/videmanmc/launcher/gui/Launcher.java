package ru.videmanmc.launcher.gui;

import com.google.inject.Guice;
import com.google.inject.Inject;
import lombok.SneakyThrows;
import ru.videmanmc.launcher.core.configuration.GeneralConfiguration;
import ru.videmanmc.launcher.core.repository.SettingsRepository;
import ru.videmanmc.launcher.gui.component.MainScreen;
import ru.videmanmc.launcher.gui.configuration.UiGuiceConfiguration;
import ru.videmanmc.launcher.http.client.configuration.HttpGuiceConfiguration;

import javax.swing.*;
import java.awt.*;

public class Launcher { //todo rewrite it with clojure

    private final SettingsRepository settingsRepository; //todo pass settings in a less coupled way

    private final MainScreen mainScreen;

    private final JFrame frame;

    @Inject
    public Launcher(MainScreen screen, SettingsRepository settingsRepository) {
        mainScreen = screen;
        this.settingsRepository = settingsRepository;
        frame = buildGui();
    }

    public static void main(String[] args) {
        var injector = Guice.createInjector(
                new HttpGuiceConfiguration(),
                new GeneralConfiguration(),
                new UiGuiceConfiguration()
        );

        var launcher = injector.getInstance(Launcher.class);
        launcher.start();
        launcher.stop();
    }

    @SneakyThrows
    private JFrame buildGui() {
        Font font = new Font("Arial", Font.PLAIN, 18);
        UIManager.put("Label.font", font);
        UIManager.put("TextArea.font", font);
        UIManager.put("TextField.font", font);
        UIManager.put("Button.font", font);
        UIManager.put("Dialog.font", font);

        var localFrame = new JFrame();
        var imageUrl = getClass().getClassLoader().getResource("icon64.png");
        var icon = new ImageIcon(imageUrl);
        localFrame.setIconImage(icon.getImage());

        return localFrame;
    }

    public void start() {
        settingsRepository.getOrLoad();
        mainScreen.show(frame);
    }

    @SneakyThrows
    public void stop() {
        settingsRepository.unload();
    }
}
