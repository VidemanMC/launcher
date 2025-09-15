package ru.videmanmc.launcher.gui.configuration;

import com.google.inject.AbstractModule;
import ru.videmanmc.launcher.gui.Launcher;
import ru.videmanmc.launcher.gui.component.MainScreen;

public class UiGuiceConfiguration extends AbstractModule {

    @Override
    protected void configure() {
        bind(Launcher.class);
        bind(MainScreen.class);
    }
}
