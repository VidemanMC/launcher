package ru.videmanmc.launcher.bootloader.configuration;

import com.google.inject.AbstractModule;
import ru.videmanmc.launcher.bootloader.Bootloader;
import ru.videmanmc.launcher.bootloader.repository.BinaryRepository;
import ru.videmanmc.launcher.bootloader.service.StartingService;
import ru.videmanmc.launcher.bootloader.service.UpdatingService;

public class BasicGuiceConfiguration extends AbstractModule {

    @Override
    protected void configure() {
        bind(Bootloader.class);
        bind(StartingService.class);
        bind(UpdatingService.class);
        bind(BinaryRepository.class);
    }
}
