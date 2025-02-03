package ru.videmanmc.launcher.service;

import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import ru.videmanmc.launcher.model.entity.Client;
import ru.videmanmc.launcher.repository.ClientRepository;

/**
 * Domain service that initiates client update and game files downloading, saving.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class ClientService {

    private final Client client;

    private final ClientRepository clientRepository;

    public void synchronizeClient() {
        var clientPreparedFiles = client.prepareGameFiles();
        clientPreparedFiles.forEach(clientRepository::saveToFile);
    }

}
