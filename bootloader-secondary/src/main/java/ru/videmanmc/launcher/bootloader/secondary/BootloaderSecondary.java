package ru.videmanmc.launcher.bootloader.secondary;

import javax.swing.*;

public class BootloaderSecondary {

    public static void main(String[] args) {

        var bootstrap = new BootloaderSecondary();
        bootstrap.show();
    }

    void show() {
        var progress = new JProgressBar();
        progress.setIndeterminate(true);

        var frame = new JFrame("Обновление лаунчера...");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 100);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        frame.add(progress);

        frame.setVisible(true);
    }

}
