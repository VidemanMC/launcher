package ru.videmanmc.launcher.service.assets;

/**
 * Responsible for downloading assets and running the game.
 */
public interface MinecraftCoreService {

    void download(String minecraftVersion);

    void run(String minecraftVersion, String nickname);

}
