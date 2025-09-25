package ru.videmanmc.launcher.core.jmccc.forge;

import lombok.SneakyThrows;
import org.to2mbn.jmccc.internal.org.json.JSONObject;
import org.to2mbn.jmccc.internal.org.json.JSONTokener;
import org.to2mbn.jmccc.mcdownloader.download.tasks.ResultProcessor;
import org.to2mbn.jmccc.option.MinecraftDirectory;
import org.to2mbn.jmccc.util.IOUtils;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A little bit refactored copy of org.to2mbn.jmccc.mcdownloader.provider.forge.InstallProfileProcessor
 */
class InstallProfileProcessor implements ResultProcessor<byte[], String> {

    private static final String FORGE_INSTALLER_NAME = "forge-installer.jar";

    private final MinecraftDirectory mcdir;

    public InstallProfileProcessor(MinecraftDirectory mcdir) {
        this.mcdir = mcdir;
    }

    /**
     * @return version to create by the name subdirectory in /versions
     */
    @Override
    public String process(byte[] forgeInstallerBytes) throws IOException, InterruptedException {
        System.out.println("0110");

        Path installerPath = mcdir.get(FORGE_INSTALLER_NAME);
        Files.write(installerPath, forgeInstallerBytes);
        prepareInstall();
        installForge();

        return extractProfileVersion(installerPath);
    }

    /**
     * In that file installer writes forge`s profile
     */
    @SneakyThrows
    private void prepareInstall() {
        Path launcherProfile = mcdir.get("launcher_profiles.json");
        if (!Files.exists(launcherProfile)) {
            Files.writeString(launcherProfile, "{}");
        }
    }

    private void installForge() throws IOException, InterruptedException {
        var home = Path.of(mcdir.getAbsolutePath());
        new ProcessBuilder(
                "java",
                "-cp",
                home.resolve(FORGE_INSTALLER_NAME)
                    .toString(),
                "net.minecraftforge.installer.SimpleInstaller",
                "--installClient",
                home.toString()
        )
                .inheritIO()
                .start()
                .waitFor();
    }

    private String extractProfileVersion(Path installerPath) throws IOException {
        try (var urlClassLoader = new URLClassLoader(new URL[]{installerPath.toUri().toURL()})) {
            var profileStream = urlClassLoader.getResourceAsStream("install_profile.json");
            var bytes = IOUtils.toByteArray(profileStream);
            JSONObject installProfile = new JSONObject(new JSONTokener(new String(bytes)));

            return installProfile.optString("version");
        }
    }
}
