package ru.videmanmc.launcher.core.service.assets;

import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
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
import org.to2mbn.jmccc.option.ServerInfo;
import org.to2mbn.jmccc.version.Version;
import ru.videmanmc.launcher.core.jmccc.forge.ForgeDownloadProviderWrapper;
import ru.videmanmc.launcher.core.model.value.Settings;

import static ru.videmanmc.launcher.constants.WorkingDirectoryConstants.CLIENT_SUBDIRECTORY_PATH;
import static ru.videmanmc.launcher.constants.WorkingDirectoryConstants.MAIN_DIRECTORY_PATH;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class JmcccMinecraftCoreService implements MinecraftCoreService {

    private final Settings settings;

    @SneakyThrows
    @Override
    public void download(String minecraftVersion) {
        var dir = new MinecraftDirectory(MAIN_DIRECTORY_PATH + CLIENT_SUBDIRECTORY_PATH);
        var downloader = MinecraftDownloaderBuilder.create()
                                                   .providerChain(DownloadProviderChain.create()
                                                                                       .addProvider(new ForgeDownloadProviderWrapper()))
                                                   .build();
        downloader.downloadIncrementally(
                          dir,
                          minecraftVersion,
                          createCallback(downloader),
                          CacheOption.CACHE
                  )
                  .get();
    }

    @SneakyThrows
    @Override
    public void run(String minecraftVersion, String nickname) {
        var dir = new MinecraftDirectory(MAIN_DIRECTORY_PATH + CLIENT_SUBDIRECTORY_PATH);
        var option = new LaunchOption(
                minecraftVersion,
                OfflineAuthenticator.name(nickname),
                dir
        );

        int ram = settings.getGame()
                          .getRamMegabytes();
        option.setMinMemory(ram);
        option.setMaxMemory(ram);
        option.setServerInfo(new ServerInfo("mods.videmanmc.ru"));

        LauncherBuilder.buildDefault()
                       .launch(option);
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
