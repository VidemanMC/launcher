package ru.videmanmc.launcher.jmccc.forge;

import org.to2mbn.jmccc.mcdownloader.download.combine.CombinedDownloadTask;
import org.to2mbn.jmccc.mcdownloader.provider.MinecraftDownloadProvider;
import org.to2mbn.jmccc.mcdownloader.provider.VersionJsonInstaller;
import org.to2mbn.jmccc.mcdownloader.provider.forge.ForgeDownloadProvider;
import org.to2mbn.jmccc.mcdownloader.provider.forge.ForgeVersion;
import org.to2mbn.jmccc.option.MinecraftDirectory;

/**
 * Wrapper for automatic Forge installation (JMCCC`s one actually don`t work)
 */
public class ForgeDownloadProviderWrapper extends ForgeDownloadProvider {

    private MinecraftDownloadProvider upstreamProvider;

    @Override
    public CombinedDownloadTask<String> gameVersionJson(MinecraftDirectory mcdir, String version) {
        ResolvedForgeVersion forgeInfo = ResolvedForgeVersion.resolve(version);
        return forgeInfo != null ? this.forgeVersion(forgeInfo.getForgeVersion())
                .andThenDownload((forge) ->
                        CombinedDownloadTask.any(this.installerTask(forge.getMavenVersion())
                                        .andThen(new InstallProfileProcessor(mcdir)),
                                this.upstreamProvider.gameVersionJson(mcdir, forge.getMinecraftVersion())
                                        .andThen((superversion) -> this.createForgeVersionJson(mcdir, forge))
                                        .andThen(new VersionJsonInstaller(mcdir))
                        )
                ) : null;
    }

    private CombinedDownloadTask<ForgeVersion> forgeVersion(String forgeVersion) {
        return this.forgeVersionList().andThen((versionList) -> {
            ForgeVersion forge = versionList.get(forgeVersion);
            if (forge == null) {
                throw new IllegalArgumentException("Forge version not found: " + forgeVersion);
            } else {
                return forge;
            }
        });
    }

    @Override
    public void setUpstreamProvider(MinecraftDownloadProvider upstreamProvider) {
        this.upstreamProvider = upstreamProvider;
    }
}
