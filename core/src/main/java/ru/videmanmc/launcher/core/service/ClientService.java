package ru.videmanmc.launcher.core.service;

import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import ru.videmanmc.launcher.core.exception.AutomaticInstallationImpossible;
import ru.videmanmc.launcher.core.model.entity.Client;
import ru.videmanmc.launcher.core.repository.ClientRepository;
import ru.videmanmc.launcher.http_client.exception.HttpDownloadException;

/**
 * Domain service that initiates client update and game files downloading, saving.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class ClientService {

    private final Client client;

    private final ClientRepository clientRepository;

    public void synchronizeClient() {
        try {
            var updatedFiles = client.update();
            updatedFiles.forEach(clientRepository::saveToFile);
        } catch (HttpDownloadException e) {
            throw new AutomaticInstallationImpossible();
        }
    }

}
