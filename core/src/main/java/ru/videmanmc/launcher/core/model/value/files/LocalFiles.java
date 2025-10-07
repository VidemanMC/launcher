package ru.videmanmc.launcher.core.model.value.files;

import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import ru.videmanmc.launcher.core.repository.ClientRepository;
import ru.videmanmc.launcher.dto.http.FilesChecksum;
import ru.videmanmc.launcher.http_client.github.FilesChecksumFactory;
import ru.videmanmc.launcher.http_client.github.LocalChecksumCalculator;

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
