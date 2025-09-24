package ru.videmanmc.launcher.core.model.value.files;

import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import ru.videmanmc.launcher.core.repository.ClientRepository;
import ru.videmanmc.launcher.http.client.FilesChecksumFactory;
import ru.videmanmc.launcher.http.client.LocalChecksumCalculator;
import ru.videmanmc.launcher.http.client.domain.value.FilesChecksum;

import java.util.List;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class LocalFiles implements LocalChecksumCalculator {

    private final ClientRepository clientRepository;
    private final FilesChecksumFactory filesChecksumFactory;

    @Override
    public FilesChecksum calculateChecksum() {
        return filesChecksumFactory.ofLocal(clientRepository.listDirectoryFileNames());
    }

    public void delete(List<String> fileNames) {
        clientRepository.deleteByNames(fileNames);
    }
}
