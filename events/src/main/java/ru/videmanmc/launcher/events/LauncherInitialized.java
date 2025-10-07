package ru.videmanmc.launcher.events;

import fun.bb1.events.abstraction.Event;
import ru.videmanmc.launcher.dto.LauncherVersion;
import ru.videmanmc.launcher.dto.Settings;

/**
 * Triggered when the launcher is started
 */
public class LauncherInitialized extends Event<LauncherInitialized.Payload> {

    public static final String EVENT_NAME = "LauncherInitialized";

    public LauncherInitialized() {
        super(Payload.class, EVENT_NAME);
    }

    public record Payload(
            Settings settings,
            LauncherVersion version
    ) {}
}
