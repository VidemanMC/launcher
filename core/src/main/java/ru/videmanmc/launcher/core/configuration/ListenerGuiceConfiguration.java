package ru.videmanmc.launcher.core.configuration;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import fun.bb1.events.abstraction.listener.IEventListener;
import ru.videmanmc.launcher.core.listener.GameRunListener;
import ru.videmanmc.launcher.core.listener.SettingsListener;
import ru.videmanmc.launcher.core.repository.SettingsRepository;
import ru.videmanmc.launcher.core.service.GameRunningService;

import java.util.List;

public class ListenerGuiceConfiguration extends AbstractModule {

    @Provides
    GameRunListener gameRunListener(GameRunningService gameRunningService) {
        var listener = new GameRunListener(gameRunningService);
        listener.register();

        return listener;
    }

    @Provides
    SettingsListener settingsListener(SettingsRepository settingsRepository) {
        var listener = new SettingsListener(settingsRepository);
        listener.register();

        return listener;
    }

    @Provides
    List<IEventListener> listeners(GameRunListener grListener, SettingsListener sListener) {
        return List.of(grListener, sListener);
    }
}
