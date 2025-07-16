package ru.videmanmc.launcher.model.value.files;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.Preconditions;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.videmanmc.launcher.core.factory.FilesChecksumFactory;
import ru.videmanmc.launcher.core.mapper.PathFormatMapper;
import ru.videmanmc.launcher.core.model.value.files.GitHubFiles;
import ru.videmanmc.launcher.core.service.hashing.Md5HashingService;
import ru.videmanmc.launcher.http.client.GitHubHttpClient;
import ru.videmanmc.launcher.http.client.domain.value.DownloadedFile;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GitHubFilesTest {

    @Mock
    private GitHubHttpClient httpClient;

    @BeforeEach
    void setUp() {
        var downloadedFile = new DownloadedFile("""
                client/a:123
                """.getBytes(StandardCharsets.UTF_8),
                "hash.txt");

        when(httpClient.download("hash.txt")).thenReturn(downloadedFile);
    }

    @Test
    void calculateChecksum_returnsNotEmpty() {
        //arrange
        var gitHubFiles = createGitHubFiles();

        //act
        var checksum = gitHubFiles.calculateChecksum();

        //assert
        assertNotEquals(0, checksum.getFileNames().size());
    }

    @Test
    void calculateChecksum_checkChecksumKeyValue_correct() {
        //arrange
        var gitHubFiles = createGitHubFiles();

        //act
        var checksum = gitHubFiles.calculateChecksum();

        //assert
        assertDoesNotThrow(() -> {
            var fileName = checksum.getFileNames().getFirst();
            var fileHash = checksum.fileHashPair().get(fileName);

            Preconditions.condition("a".equals(fileName), "name is not correct");
            Preconditions.condition("123".equals(fileHash), "hash is not correct");
        });
    }

    private GitHubFiles createGitHubFiles() {
        return new GitHubFiles(httpClient, new FilesChecksumFactory(new Md5HashingService(), new PathFormatMapper()));
    }
}
