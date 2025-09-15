package ru.videmanmc.launcher.core.model.value;

import lombok.Getter;
import lombok.Setter;

import java.io.File;

import static ru.videmanmc.launcher.constants.WorkingDirectoryConstants.CLIENT_SUBDIRECTORY_PATH;
import static ru.videmanmc.launcher.constants.WorkingDirectoryConstants.MAIN_DIRECTORY_PATH;

@Getter
@Setter
public class Settings {

    private GameSettings game = new GameSettings();

    private LauncherSettings startup = new LauncherSettings();


    @Getter
    @Setter
    public static final class GameSettings {

        /**
         * Whether to automatically join to server?
         */
        private boolean autoJoin;

        /**
         * Client`s home dir
         */
        private File directory = new File(MAIN_DIRECTORY_PATH, CLIENT_SUBDIRECTORY_PATH);

        /**
         * Available RAM for running Minecraft.
         */
        private int ramMegabytes = 2;

    }

    @Getter
    @Setter
    public static final class LauncherSettings {

        /**
         * Whether to execute in offline mode? <br>
         * In this mode, launcher will not check updates.
         */
        private boolean offline;
    }

}
