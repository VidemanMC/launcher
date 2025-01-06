package ru.videmanmc.launcher.service;

import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.to2mbn.jmccc.mcdownloader.MinecraftDownloader;
import org.to2mbn.jmccc.mcdownloader.MinecraftDownloaderBuilder;
import org.to2mbn.jmccc.mcdownloader.download.concurrent.CallbackAdapter;
import org.to2mbn.jmccc.mcdownloader.provider.DownloadProviderChain;
import org.to2mbn.jmccc.mcdownloader.provider.forge.ForgeDownloadProvider;
import org.to2mbn.jmccc.mcdownloader.provider.forge.ForgeVersionList;
import org.to2mbn.jmccc.mcdownloader.provider.liteloader.LiteloaderDownloadProvider;
import org.to2mbn.jmccc.mcdownloader.provider.liteloader.LiteloaderVersionList;
import org.to2mbn.jmccc.option.MinecraftDirectory;
import org.to2mbn.jmccc.version.Version;
import ru.videmanmc.launcher.model.entity.Client;
import ru.videmanmc.launcher.repository.ClientRepository;

/**
 * Domain service that initiates client update and game files downloading, saving.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class ClientService {

    private final Client client;

    private final ClientRepository clientRepository;

    public void runClient() {
        var clientPreparedFiles = client.prepareGameFiles();
        clientPreparedFiles.forEach(clientRepository::saveToFile);

        client.run();

        tempMinecraftRunningMethod();
    }

    void tempMinecraftRunningMethod() {
        MinecraftDirectory dir = new MinecraftDirectory("C:\\Users\\Aleks\\.videmanmc\\client");
        ForgeDownloadProvider forgeProvider = new ForgeDownloadProvider();
        MinecraftDownloader downloader = MinecraftDownloaderBuilder.create()
                .providerChain(DownloadProviderChain.create()
                        .addProvider(forgeProvider))
                .build();

        downloader.downloadIncrementally(dir, "1.20.1-forge-47.3.22", new CallbackAdapter<>() {
            @Override
            public void done(Version result) {
                System.out.println("EH EH!");
            }
        });

        downloader.shutdown();
    }

}
