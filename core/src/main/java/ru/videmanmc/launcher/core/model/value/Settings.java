package ru.videmanmc.launcher.core.model.value;

import lombok.Getter;
import lombok.Setter;

import java.io.File;

/**
 * Settings that contains option for launcher and Minecraft.
 */
@Getter
@Setter
public class Settings {

    public final static String MAIN_DIRECTORY_PATH = System.getProperty("user.home") + "/.videmanmc/";
    public final static String CLIENT_SUBDIRECTORY_PATH = "client/";
    public final static String LAUNCHER_CONFIG_FILE_NAME = "settings.yml";

    private GameSettings game = new GameSettings();

    private LauncherSettings startup = new LauncherSettings();


    @Getter
    @Setter
    public static final class GameSettings {

        private boolean autoJoin;

        private File directory = new File(MAIN_DIRECTORY_PATH, CLIENT_SUBDIRECTORY_PATH);

        private int ramMegabytes = 2;

    }

    @Getter
    @Setter
    public static final class LauncherSettings {

        private boolean offline;
    }

}
