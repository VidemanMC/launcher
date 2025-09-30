package ru.videmanmc.launcher.events;

import fun.bb1.events.abstraction.Event;
import ru.videmanmc.launcher.dto.Settings;

/**
 * Triggered when the launcher is started
 */
public class LauncherStarted extends Event<Settings> {

    public static final String EVENT_NAME = "LauncherStarted";

    public LauncherStarted() {
        super(Settings.class, EVENT_NAME);
    }
}
