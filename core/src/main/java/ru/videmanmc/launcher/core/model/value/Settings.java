package ru.videmanmc.launcher.core.model.value;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

import static ru.videmanmc.launcher.constants.WorkingDirectoryConstants.CLIENT_SUBDIRECTORY_PATH;
import static ru.videmanmc.launcher.constants.WorkingDirectoryConstants.MAIN_DIRECTORY_PATH;

@Getter
@Setter
public class Settings {

    private static final int MINIMAL_COMFORTABLE_RAM = 2048;

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
        private int ramMegabytes = Math.max(MINIMAL_COMFORTABLE_RAM, calculateAvailableRamInMb());

        @JsonIgnore
        public int calculateAvailableRamInMb() {
            OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
            if (osBean instanceof com.sun.management.OperatingSystemMXBean os) {
                long freeMemoryBytes = os.getFreeMemorySize();
                return Math.toIntExact(Math.round((double) freeMemoryBytes / (1024 * 1024)));
            } else {
                throw new UnsupportedOperationException("Not able to get free RAM");
            }
        }

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
