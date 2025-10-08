package ru.videmanmc.launcher.core.listener;

import com.google.inject.Inject;
import fun.bb1.events.abstraction.listener.EventHandler;
import fun.bb1.events.abstraction.listener.IEventListener;
import lombok.RequiredArgsConstructor;
import ru.videmanmc.launcher.core.repository.SettingsRepository;
import ru.videmanmc.launcher.dto.Settings;
import ru.videmanmc.launcher.events.SettingsChanged;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class SettingsListener implements IEventListener {

    private final SettingsRepository settingsRepository;

    @EventHandler(SettingsChanged.EVENT_NAME)
    public void on(Settings settings) {
        settingsRepository.unload(settings);
    }

}
