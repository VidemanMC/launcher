package ru.videmanmc.launcher.bootloader.secondary.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.videmanmc.launcher.bootloader.secondary.repository.BinaryRepository;
import ru.videmanmc.launcher.http.client.GitHubHttpClient;
import ru.videmanmc.launcher.http.client.domain.entity.Binary;
import ru.videmanmc.launcher.http.client.domain.value.BinaryInfo;
import ru.videmanmc.launcher.http.client.domain.value.Hash;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.videmanmc.launcher.constants.BinariesNamingConstants.LAUNCHER_PREFIX;

@ExtendWith(MockitoExtension.class)
class UpdatingServiceTest {

    @Mock
    private BinaryRepository binaryRepository;

    @Mock
    private GitHubHttpClient binaryClient;

    @InjectMocks
    private UpdatingService updatingService;


    @Test
    void update_binaryNotExists_downloadsBinary() {
        // arrange
        var binaryInfo = binaryInfo();
        var binary = localBinary();

        when(binaryRepository.findByPrefix(LAUNCHER_PREFIX))
                .thenReturn(null);
        when(binaryClient.getBinary(binaryInfo))
                .thenReturn(binary);
        when(binaryClient.getInfoByPrefix(LAUNCHER_PREFIX))
                .thenReturn(binaryInfo);

        // act
        updatingService.update();

        // assert
        verify(binaryClient).getBinary(binaryInfo);
    }

    @Test
    void update_binaryExistsAndHashesDifferent_downloadsBinary() {
        // arrange
        var binaryInfo = binaryInfo();
        var localBinary = localBinary();
        var remoteBinary = remoteBinary();

        when(binaryRepository.findByPrefix(LAUNCHER_PREFIX))
                .thenReturn(localBinary);
        when(binaryClient.getBinary(binaryInfo))
                .thenReturn(remoteBinary);
        when(binaryClient.getInfoByPrefix(LAUNCHER_PREFIX))
                .thenReturn(binaryInfo);

        // act
        updatingService.update();

        // assert
        verify(binaryClient).getBinary(binaryInfo);
    }


    @Test
    void update_binaryExistsAndHashesEqual_returnsLocalBinaryName() {
        // arrange
        var binaryInfo = binaryInfo();
        var localBinary = localBinary();

        when(binaryRepository.findByPrefix(LAUNCHER_PREFIX))
                .thenReturn(null);
        when(binaryClient.getInfoByPrefix(LAUNCHER_PREFIX))
                .thenReturn(binaryInfo);
        when(binaryClient.getBinary(binaryInfo))
                .thenReturn(localBinary);

        // act
        var updatedBinaryName = updatingService.update();

        // assert
        assertEquals(localBinary.name(), updatedBinaryName);
    }

    private BinaryInfo binaryInfo() {
        return new BinaryInfo("a", "b", "c");
    }

    private Binary localBinary() {
        return new Binary(new Hash("c"), new byte[0], "a");
    }

    private Binary remoteBinary() {
        return new Binary(new Hash("cc"), new byte[0], "a");
    }

}