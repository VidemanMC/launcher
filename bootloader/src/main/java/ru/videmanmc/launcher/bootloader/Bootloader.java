package ru.videmanmc.launcher.bootloader;

import com.google.inject.Guice;
import com.google.inject.Inject;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import ru.videmanmc.launcher.bootloader.configuration.BasicGuiceConfiguration;
import ru.videmanmc.launcher.bootloader.service.StartingService;
import ru.videmanmc.launcher.http.client.configuration.HttpGuiceConfiguration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URI;
import java.util.Arrays;
import java.util.stream.Collectors;

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
        bootstrap.hide();
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

    public void showErrorModal(Exception exception) {
        var suggestion = new JLabel("Скопируйте текст ошибки ниже (Ctr + C)  и сообщите  нам");

        var errorMessage = new JTextArea();
        errorMessage.append(createUserErrorMessage(exception));
        errorMessage.setEditable(false);

        var openIssuesButton = getButton(URI.create("https://github.com/VidemanMC/launcher/issues"));
        openIssuesButton.setMargin(new Insets(10, 30, 10, 30));

        var panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(suggestion);
        panel.add(new JScrollPane(errorMessage));
        panel.add(openIssuesButton);

        var modal = new JDialog(frame, "ОШИБКА", true);
        modal.setLocationRelativeTo(frame);
        modal.add(panel);
        modal.pack();
        modal.setVisible(true);
    }

    private @NotNull JButton getButton(URI issuesUri) {
        var openIssuesButton = new JButton("Сообщить об ошибке");
        openIssuesButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openWebpage(issuesUri);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                // not used
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // not used
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // not used
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // not used
            }
        });
        return openIssuesButton;
    }

    private String createUserErrorMessage(Exception exception) {
        return Arrays.stream(exception.getStackTrace())
                     .map(StackTraceElement::toString)
                     .collect(Collectors.joining("\n"));
    }

    @SneakyThrows
    private void openWebpage(URI uri) {
        var desktop = Desktop.getDesktop();

        if (desktop.isSupported(Desktop.Action.BROWSE)) {
            desktop.browse(uri);
        } else {
            Runtime.getRuntime()
                   .exec(new String[]{"xdg-open", uri.toString()});
        }
    }

    /**
     * Parent process can`t be stopped until it`s children processes work. This method is workaround.
     */
    void hide() {
        System.exit(0);
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
