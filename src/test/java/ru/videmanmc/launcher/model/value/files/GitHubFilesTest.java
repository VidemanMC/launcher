package ru.videmanmc.launcher.model.value.files;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.videmanmc.launcher.factory.FilesChecksumFactory;
import ru.videmanmc.launcher.http.GitHubHttpClient;
import ru.videmanmc.launcher.mapper.PathFormatMapper;
import ru.videmanmc.launcher.service.hashing.Md5HashingService;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        assertTrue(() -> {
                    var fileName = checksum.getFileNames().getFirst();
                    var fileHash = checksum.fileHashPair().get(fileName);

                    return "a".equals(fileName) && "123".equals(fileHash);
                },
                "Contents of FileChecksum preceded incorrect: key or value from map is not as expected!");
    }

    private GitHubFiles createGitHubFiles() {
        return new GitHubFiles(httpClient, new PathFormatMapper(), new FilesChecksumFactory(new Md5HashingService(), new PathFormatMapper()));
    }
}
