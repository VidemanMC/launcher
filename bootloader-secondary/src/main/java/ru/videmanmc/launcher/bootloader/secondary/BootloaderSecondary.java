package ru.videmanmc.launcher.bootloader.secondary;

import com.google.inject.Guice;
import com.google.inject.Inject;
import ru.videmanmc.launcher.bootloader.secondary.configuration.BasicGuiceConfiguration;
import ru.videmanmc.launcher.bootloader.secondary.service.StartingService;
import ru.videmanmc.launcher.http.client.configuration.HttpGuiceConfiguration;

import javax.swing.*;

public class BootloaderSecondary {

    private final StartingService startingService;

    private final JFrame frame;

    @Inject
    public BootloaderSecondary(StartingService service) {
        startingService = service;
        frame = buildGui();
    }

    public static void main(String[] args) {
        var injector = Guice.createInjector(
                new HttpGuiceConfiguration(),
                new BasicGuiceConfiguration()
        );

        var bootstrap = injector.getInstance(BootloaderSecondary.class);
        bootstrap.show();
        bootstrap.start();
        bootstrap.hide();
    }

    void show() {
        frame.setVisible(true);
    }

    void start() {
        startingService.start();
    }

    /**
     * Parent process can`t be stopped until it`s children processes work. This method is workaround.
     */
    void hide() {
        frame.setVisible(false);
        System.exit(0);
    }

    private JFrame buildGui() {
        var progress = new JProgressBar();
        progress.setIndeterminate(true);

        var frame = new JFrame("Обновление лаунчера...");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 100);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        frame.add(progress);

        return frame;
    }
}
