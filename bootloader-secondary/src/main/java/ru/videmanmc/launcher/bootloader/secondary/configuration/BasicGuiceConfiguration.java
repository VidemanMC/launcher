package ru.videmanmc.launcher.bootloader.secondary.configuration;

import com.google.inject.AbstractModule;
import ru.videmanmc.launcher.bootloader.secondary.BootloaderSecondary;
import ru.videmanmc.launcher.bootloader.secondary.repository.BinaryRepository;
import ru.videmanmc.launcher.bootloader.secondary.service.StartingService;
import ru.videmanmc.launcher.bootloader.secondary.service.UpdatingService;

public class BasicGuiceConfiguration extends AbstractModule {

    @Override
    protected void configure() {
        bind(BootloaderSecondary.class);
        bind(StartingService.class);
        bind(UpdatingService.class);
        bind(BinaryRepository.class);
    }
}
