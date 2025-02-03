package ru.videmanmc.launcher.jmccc.forge;

import lombok.SneakyThrows;
import org.to2mbn.jmccc.internal.org.json.JSONObject;
import org.to2mbn.jmccc.internal.org.json.JSONTokener;
import org.to2mbn.jmccc.mcdownloader.download.tasks.ResultProcessor;
import org.to2mbn.jmccc.option.MinecraftDirectory;
import org.to2mbn.jmccc.util.IOUtils;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A little bit refactored copy of org.to2mbn.jmccc.mcdownloader.provider.forge.InstallProfileProcessor
 */
class InstallProfileProcessor implements ResultProcessor<byte[], String> {

    private final MinecraftDirectory mcdir;

    public InstallProfileProcessor(MinecraftDirectory mcdir) {
        this.mcdir = mcdir;
    }

    /**
     * @return version to create by the name subdirectory in /versions
     */
    @Override
    public String process(byte[] forgeInstallerBytes) throws Exception {
        Path installerPath = mcdir.get("forge-installer.jar");
        Files.write(installerPath, forgeInstallerBytes);

        prepareInstall();
        installForge(installerPath);

        var newProfileVersion = extractProfileVersion(installerPath);

        Files.delete(installerPath);
        return newProfileVersion;
    }
    @SneakyThrows
    private String extractProfileVersion(Path installerPath) {
        try (var urlClassLoader = new URLClassLoader(new URL[] {installerPath.toUri().toURL()})) {
            var profileStream = urlClassLoader.getResourceAsStream("install_profile.json");
            var bytes = IOUtils.toByteArray(profileStream);
            JSONObject installProfile = new JSONObject(new JSONTokener(new String(bytes)));

            return installProfile.optString("version");
        }
    }

    @SneakyThrows
    private void prepareInstall() {
        Path launcherProfile = mcdir.get("launcher_profiles.json");
        if (!Files.exists(launcherProfile)) {
            Files.writeString(launcherProfile, "{}");
        }
    }

    private void installForge(Path installerJar) throws Exception {
        try (URLClassLoader cl = new URLClassLoader(new URL[]{installerJar.toUri().toURL()})) {
            Class<?> installer = cl.loadClass("net.minecraftforge.installer.SimpleInstaller");
            Method main = installer.getMethod("main", String[].class);
            main.invoke(null, (Object) new String[]{"--installClient", mcdir.getAbsolutePath()});
        }
    }
}
