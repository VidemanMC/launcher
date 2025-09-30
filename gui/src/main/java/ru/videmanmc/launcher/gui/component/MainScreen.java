package ru.videmanmc.launcher.gui.component;

import fun.bb1.events.abstraction.listener.EventHandler;
import fun.bb1.events.abstraction.listener.IEventListener;
import lombok.SneakyThrows;
import ru.videmanmc.launcher.dto.Settings;
import ru.videmanmc.launcher.events.GameLaunchInitiated;
import ru.videmanmc.launcher.events.LauncherInitialized;
import ru.videmanmc.launcher.events.SettingsChanged;

import javax.swing.*;
import java.awt.*;

public class MainScreen implements LauncherScreen, IEventListener {

    private String login;

    private String version;

    private Settings cachedSettings; // cache to avoid obj creation time consumption

    @EventHandler(LauncherInitialized.EVENT_NAME)
    public void on(LauncherInitialized.Payload payload) {
        cachedSettings = payload.settings();
        login = cachedSettings
                .getGame()
                .getLogin();
        version = payload.version()
                         .version();
        show();
    }

    @SneakyThrows
    @Override
    public void show() {
        Font font = new Font("Arial", Font.PLAIN, 18);
        UIManager.put("Label.font", font);
        UIManager.put("TextArea.font", font);
        UIManager.put("TextField.font", font);
        UIManager.put("Button.font", font);
        UIManager.put("Dialog.font", font);

        var frame = new JFrame();
        var imageUrl = getClass().getClassLoader()
                                 .getResource("icon64.png");
        var icon = new ImageIcon(imageUrl);
        frame.setIconImage(icon.getImage());
        frame.setTitle("VidemanMC " + version);
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Панель для центральных компонентов
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Настройка текстового поля
        var textField = new JTextField(login, 20);
        textField.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 20));
        textField.setBackground(Color.BLACK);
        textField.setForeground(Color.WHITE);
        textField.setCaretColor(Color.WHITE);
        textField.setHorizontalAlignment(SwingConstants.CENTER);

        // Checkbox
        var offlineCheckbox = new JCheckBox("Оффлайн-режим");
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

            new Thread(() ->
                               new GameLaunchInitiated().emit(new GameLaunchInitiated.Payload(
                                       textField.getText(),
                                       !offlineCheckbox.isSelected()
                               ))
            ).start();

            saveLogin(textField.getText());
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
        SwingUtilities.invokeLater(textField::requestFocusInWindow);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void saveLogin(String login) {
        cachedSettings.getGame()
                      .setLogin(login);
        new SettingsChanged().emit(cachedSettings);
    }
}
