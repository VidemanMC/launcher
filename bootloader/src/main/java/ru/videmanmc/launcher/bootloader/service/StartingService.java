package ru.videmanmc.launcher.bootloader.service;


import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import ru.videmanmc.launcher.constants.WorkingDirectoryConstants;

import java.io.File;
import java.io.IOException;

/**
 * Starts the launcher process
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class StartingService {

    private final UpdatingService updatingService;

    public void start() {
        String launcherName = updatingService.update();
        spawnProcess(launcherName);
    }

    private void spawnProcess(String launcherFileName) {
        try {
            System.out.println("Starting process: " + launcherFileName);

            var jarDir = new File(WorkingDirectoryConstants.MAIN_DIRECTORY_PATH);

            new ProcessBuilder("java", "-jar", launcherFileName)
                    .directory(jarDir)
                    .inheritIO()
                    .start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
