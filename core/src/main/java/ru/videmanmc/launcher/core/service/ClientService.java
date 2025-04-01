package ru.videmanmc.launcher.core.service;

import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import ru.videmanmc.launcher.core.model.entity.Client;
import ru.videmanmc.launcher.core.repository.ClientRepository;

/**
 * Domain service that initiates client update and game files downloading, saving.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class ClientService {

    private final Client client;

    private final ClientRepository clientRepository;

    public void synchronizeClient() {
        var updatedFiles = client.update();
        updatedFiles.forEach(clientRepository::saveToFile);
    }

}
