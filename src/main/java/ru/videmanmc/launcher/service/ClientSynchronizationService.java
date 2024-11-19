package ru.videmanmc.launcher.service;

import ru.videmanmc.launcher.model.entity.Client;

/**
 * This class represents a synchronization operation, that updates the client against remote source before every run.
 */
public interface ClientSynchronizationService {

    void synchronize(Client client);
}
