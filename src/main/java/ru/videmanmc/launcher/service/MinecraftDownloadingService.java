package ru.videmanmc.launcher.service;

import org.to2mbn.jmccc.launch.LaunchException;
import org.to2mbn.jmccc.mcdownloader.MinecraftDownloader;
import org.to2mbn.jmccc.mcdownloader.MinecraftDownloaderBuilder;
import org.to2mbn.jmccc.mcdownloader.download.concurrent.CallbackAdapter;
import org.to2mbn.jmccc.mcdownloader.download.concurrent.DownloadCallback;
import org.to2mbn.jmccc.mcdownloader.download.tasks.DownloadTask;
import org.to2mbn.jmccc.mcdownloader.provider.DownloadProviderChain;
import org.to2mbn.jmccc.mcdownloader.provider.forge.ForgeDownloadProvider;
import org.to2mbn.jmccc.option.MinecraftDirectory;
import org.to2mbn.jmccc.version.Version;

import java.io.File;
import java.io.IOException;

public class MinecraftDownloadingService {


    public void download(File to) throws IOException, LaunchException {
        var dir = new MinecraftDirectory(to);
        ForgeDownloadProvider forgeProvider = new ForgeDownloadProvider();
        MinecraftDownloader downloader = MinecraftDownloaderBuilder.create()
                .providerChain(DownloadProviderChain.create()
                        .addProvider(forgeProvider))
                .build();
        downloader.downloadIncrementally(dir, "1.20.1", new CallbackAdapter<>() {

            @Override
            public void failed(Throwable e) {
                // when the task fails
            }

            @Override
            public void done(Version result) {
                System.out.println("MinecraftDownloadingService.done");
                downloader.shutdown();
            }

            @Override
            public void cancelled() {
                // when the task cancels
            }

            @Override
            public <R> DownloadCallback<R> taskStart(DownloadTask<R> task) {
                // when a new sub download task starts
                // return a DownloadCallback to listen the status of the task
                return new CallbackAdapter<R>() {

                    {
                        System.out.println(task.getURI());
                    }

                    @Override
                    public void done(R result) {
                        // when the sub download task finishes
                    }

                    @Override
                    public void failed(Throwable e) {
                        // when the sub download task fails
                    }

                    @Override
                    public void cancelled() {
                        // when the sub download task cancels
                    }

                    @Override
                    public void updateProgress(long done, long total) {
                        var dd = ((float) done);
                        var td = ((float) total);
                        // when the progress of the sub download task has updated
                        System.out.println(
                               dd / td*100 + "%"
                        );
                    }

                    @Override
                    public void retry(Throwable e, int current, int max) {
                        // when the sub download task fails, and the downloader decides to retry the task
                        // in this case, failed() won't be called
                    }
                };
            }
        });
    }
}
