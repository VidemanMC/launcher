package ru.videmanmc.launcher.events.configuration;

import com.google.inject.AbstractModule;
import fun.bb1.events.bus.EventBus;
import ru.videmanmc.launcher.dto.Settings;
import ru.videmanmc.launcher.events.GameLaunchInitiated;
import ru.videmanmc.launcher.events.LauncherInitialized;
import ru.videmanmc.launcher.events.SettingsChanged;

public class EventsGuiceConfiguration extends AbstractModule {

    @Override
    protected void configure() {
        EventBus.DEFAULT_BUS.publishRoute(GameLaunchInitiated.EVENT_NAME, GameLaunchInitiated.Payload.class);
        EventBus.DEFAULT_BUS.publishRoute(LauncherInitialized.EVENT_NAME, LauncherInitialized.Payload.class);
        EventBus.DEFAULT_BUS.publishRoute(SettingsChanged.EVENT_NAME, Settings.class);
    }
}
