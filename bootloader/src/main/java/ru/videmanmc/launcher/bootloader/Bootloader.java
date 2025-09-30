package ru.videmanmc.launcher.bootloader;

import com.google.inject.Guice;
import com.google.inject.Inject;
import lombok.SneakyThrows;
import ru.videmanmc.launcher.bootloader.configuration.BasicGuiceConfiguration;
import ru.videmanmc.launcher.bootloader.service.StartingService;
import ru.videmanmc.launcher.gui.component.ExceptionDialog;
import ru.videmanmc.launcher.http_client.configuration.HttpGuiceConfiguration;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.stream.Collectors;

import static ru.videmanmc.launcher.constants.ErrorMessageConstants.GENERAL_ERROR;

public class Bootloader {

    private final StartingService startingService;

    private final JFrame frame;

    @Inject
    public Bootloader(StartingService service) {
        startingService = service;
        frame = buildGui();
    }

    public static void main(String[] args) {
        var injector = Guice.createInjector(
                new HttpGuiceConfiguration(),
                new BasicGuiceConfiguration()
        );

        var bootstrap = injector.getInstance(Bootloader.class);
        bootstrap.show();
        bootstrap.start();
    }

    void show() {
        frame.setVisible(true);
    }

    void start() {
        try {
            startingService.start();
        } catch (Exception e) {
            showErrorModal(e);
        }
    }

    public void showErrorModal(Exception e) {
        new ExceptionDialog(formatGeneral(e));
    }

    private String formatGeneral(Throwable throwable) {
        var stacktrace = Arrays.stream(throwable.getStackTrace())
                               .map(StackTraceElement::toString)
                               .collect(Collectors.joining("<br>"));
        return GENERAL_ERROR
                .formatted(
                        throwable.getMessage(),
                        stacktrace
                );
    }

    @SneakyThrows
    private JFrame buildGui() {
        Font font = new Font("Arial", Font.PLAIN, 18);
        UIManager.put("Label.font", font);
        UIManager.put("TextArea.font", font);
        UIManager.put("TextField.font", font);
        UIManager.put("Button.font", font);
        UIManager.put("Dialog.font", font);

        var progress = new JProgressBar();
        progress.setIndeterminate(true);

        var localFrame = new JFrame("Обновление лаунчера...");
        localFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        localFrame.setSize(450, 100);
        localFrame.setLocationRelativeTo(null);
        localFrame.setResizable(false);
        localFrame.add(progress);

        var imageUrl = getClass().getClassLoader().getResource("icon64.png");
        var icon = new ImageIcon(imageUrl);
        localFrame.setIconImage(icon.getImage());

        return localFrame;
    }
}
