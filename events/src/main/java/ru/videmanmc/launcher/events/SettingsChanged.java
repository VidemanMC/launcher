package ru.videmanmc.launcher.events;

import fun.bb1.events.abstraction.Event;
import ru.videmanmc.launcher.dto.Settings;

/**
 * Triggered when {@link Settings} are changed
 */
public class SettingsChanged extends Event<Settings> {

    public static final String EVENT_NAME = "SettingsChanged";

    public SettingsChanged() {
        super(Settings.class, EVENT_NAME);
    }
}
