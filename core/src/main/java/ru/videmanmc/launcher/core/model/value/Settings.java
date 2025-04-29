package ru.videmanmc.launcher.core.model.value;

import lombok.Getter;
import lombok.Setter;

import java.io.File;

import static ru.videmanmc.launcher.constants.WorkingDirectoryConstants.CLIENT_SUBDIRECTORY_PATH;
import static ru.videmanmc.launcher.constants.WorkingDirectoryConstants.MAIN_DIRECTORY_PATH;

/**
 * Settings that contains option for launcher and Minecraft.
 */
@Getter
@Setter
public class Settings {

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
