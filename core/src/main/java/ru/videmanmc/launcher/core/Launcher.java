package ru.videmanmc.launcher.core;

import com.google.inject.Guice;
import com.google.inject.Inject;
import fun.bb1.events.abstraction.listener.IEventListener;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ru.videmanmc.launcher.core.configuration.CoreGuiceConfiguration;
import ru.videmanmc.launcher.core.configuration.ListenerGuiceConfiguration;
import ru.videmanmc.launcher.core.error_handling.GlobalExceptionHandler;
import ru.videmanmc.launcher.core.repository.SettingsRepository;
import ru.videmanmc.launcher.dto.LauncherVersion;
import ru.videmanmc.launcher.events.LauncherInitialized;
import ru.videmanmc.launcher.events.configuration.EventsGuiceConfiguration;
import ru.videmanmc.launcher.gui.component.MainScreen;
import ru.videmanmc.launcher.http_client.configuration.HttpGuiceConfiguration;

import java.util.List;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class Launcher {

    private final LauncherVersion version;

    private final SettingsRepository settingsRepository;

    @SuppressWarnings("unused")
    private final List<IEventListener> unused; // init listeners by Guice, without it events won't be listened

    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler(new GlobalExceptionHandler());

        var injector = Guice.createInjector(
                new CoreGuiceConfiguration(),
                new HttpGuiceConfiguration(),
                new EventsGuiceConfiguration(),
                new ListenerGuiceConfiguration()
        );
        var launcher = injector.getInstance(Launcher.class);
        launcher.start();
        launcher.stop();
    }

    public void start() {
        var settings = settingsRepository.getOrLoad();
        new MainScreen().register();
        new LauncherInitialized().emit(
                new LauncherInitialized.Payload(settings, version)
        );
    }

    @SneakyThrows
    public void stop() {
        settingsRepository.unload();
    }
}
