package ru.videmanmc.launcher.gui.component;

import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ru.videmanmc.launcher.core.dto.LauncherVersion;
import ru.videmanmc.launcher.core.service.GameRunningService;
import ru.videmanmc.launcher.dto.Settings;

import javax.swing.*;
import java.awt.*;

@RequiredArgsConstructor(onConstructor_ = @__(@Inject))
public class MainScreen implements LauncherScreen {

    private final Settings settings;

    private final LauncherVersion launcherVersion;

    private final GameRunningService gameRunningService; //todo add event system and remove this shit from there

    private JTextField textField;

    private JCheckBox offlineCheckbox;

    @SneakyThrows
    @Override
    public void show(JFrame frame) {
        frame.setTitle("VidemanMC " + launcherVersion.version());
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Панель для центральных компонентов
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Настройка текстового поля
        textField = new JTextField(getCachedLogin(), 20);
        textField.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 20));
        textField.setBackground(Color.BLACK);
        textField.setForeground(Color.WHITE);
        textField.setCaretColor(Color.WHITE);
        textField.setHorizontalAlignment(SwingConstants.CENTER);

        // Checkbox
        offlineCheckbox = new JCheckBox("Оффлайн-режим");
        offlineCheckbox.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 15));

        // Кнопка
        JButton playButton = new JButton("ИГРАТЬ");
        playButton.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 20));
        playButton.setBackground(Color.MAGENTA);
        playButton.setForeground(Color.WHITE);
        // Установка границы
        playButton.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
        // Обработка нажатия
        playButton.addActionListener(e -> {
            if (textField.getText()
                         .isEmpty()) {
                // Фокусируем
                textField.requestFocusInWindow();
                return;
            }
            new Thread(() -> gameRunningService.run(textField.getText(), !offlineCheckbox.isSelected())).start();

            cacheLogin(textField.getText());
        });


        // Reminder label
        JLabel reminderLabel = new JLabel("Всё ещё впереди!");
        reminderLabel.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 16));
        reminderLabel.setForeground(Color.BLACK);
        reminderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);


        // Добавляем компоненты
        panel.add(Box.createVerticalStrut(10));
        panel.add(textField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(playButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(offlineCheckbox);
        panel.add(Box.createVerticalStrut(10));
        panel.add(reminderLabel);
        panel.add(Box.createVerticalStrut(10));

        frame.add(panel, BorderLayout.CENTER);

        // Расположим фокус на текстовом поле после отображения
        SwingUtilities.invokeLater(() -> {
            textField.requestFocusInWindow();
        });

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void cacheLogin(String login) {
        settings.getGame()
                .setLogin(login);
    }

    private String getCachedLogin() {
        return settings.getGame()
                       .getLogin();
    }
}
