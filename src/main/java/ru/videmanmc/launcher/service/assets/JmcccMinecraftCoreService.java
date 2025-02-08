package ru.videmanmc.launcher.service.assets;

import lombok.SneakyThrows;
import org.to2mbn.jmccc.auth.OfflineAuthenticator;
import org.to2mbn.jmccc.launch.LauncherBuilder;
import org.to2mbn.jmccc.mcdownloader.CacheOption;
import org.to2mbn.jmccc.mcdownloader.MinecraftDownloader;
import org.to2mbn.jmccc.mcdownloader.MinecraftDownloaderBuilder;
import org.to2mbn.jmccc.mcdownloader.download.concurrent.CallbackAdapter;
import org.to2mbn.jmccc.mcdownloader.download.concurrent.DownloadCallback;
import org.to2mbn.jmccc.mcdownloader.download.tasks.DownloadTask;
import org.to2mbn.jmccc.mcdownloader.provider.DownloadProviderChain;
import org.to2mbn.jmccc.option.LaunchOption;
import org.to2mbn.jmccc.option.MinecraftDirectory;
import org.to2mbn.jmccc.version.Version;
import ru.videmanmc.launcher.jmccc.forge.ForgeDownloadProviderWrapper;

import static ru.videmanmc.launcher.model.value.Settings.CLIENT_SUBDIRECTORY_PATH;
import static ru.videmanmc.launcher.model.value.Settings.MAIN_DIRECTORY_PATH;

public class JmcccMinecraftCoreService implements MinecraftCoreService {

    @Override
    public void download(String minecraftVersion) {
        var dir = new MinecraftDirectory(MAIN_DIRECTORY_PATH + CLIENT_SUBDIRECTORY_PATH);

        downloadVanilla(stripVanillaVersion(minecraftVersion), dir); // костыль, чтобы сркыть ошибку с отсутствием 1.20.1.json
        downloadForge(minecraftVersion, dir);
    }

    private String stripVanillaVersion(String minecraftVersion) {
        int index = minecraftVersion.indexOf('-');

        if (index == -1) return minecraftVersion;

        return minecraftVersion.substring(0, index);
    }

    @SneakyThrows
    private void downloadVanilla(String vanillaVersion, MinecraftDirectory dir) {
        var downloader = MinecraftDownloaderBuilder.buildDefault();
        downloader.downloadIncrementally(dir, vanillaVersion, createCallback(downloader), CacheOption.CACHE).get();
    }

    @SneakyThrows
    private void downloadForge(String forgeVersion, MinecraftDirectory dir) {
        var downloader = MinecraftDownloaderBuilder.create()
                .providerChain(DownloadProviderChain.create()
                        .addProvider(
                                new ForgeDownloadProviderWrapper()
                        )
                )
                .build();
        downloader.downloadIncrementally(dir, forgeVersion, createCallback(downloader), CacheOption.CACHE).get();

    }

    @SneakyThrows
    @Override
    public void run(String minecraftVersion, String nickname) {
        var dir = new MinecraftDirectory(MAIN_DIRECTORY_PATH + CLIENT_SUBDIRECTORY_PATH);
        LauncherBuilder.buildDefault()
                .launch(
                        new LaunchOption(
                                minecraftVersion,
                                OfflineAuthenticator.name(nickname),
                                dir
                        )
                );
    }

    private CallbackAdapter<Version> createCallback(MinecraftDownloader downloader) {
        return new CallbackAdapter<>() {

            @Override
            public void done(Version result) {
                downloader.shutdown();
            }

            @Override
            public void failed(Throwable e) {
                System.out.println("FAILED: " + e); //todo add proper logging (+ event system)
            }

            @Override
            public <R> DownloadCallback<R> taskStart(DownloadTask<R> task) {
                return new DownloadCallback<>() {

                    @Override
                    public void updateProgress(long l, long l1) {
                        System.out.println(l+"/"+l1);
                    }

                    @Override
                    public void retry(Throwable throwable, int i, int i1) {

                    }

                    @Override
                    public void done(R r) {

                    }

                    @Override
                    public void failed(Throwable throwable) {
                        System.out.println("FAILED: " + throwable);
                    }

                    @Override
                    public void cancelled() {

                    }
                };
            }
        };
    }
}
