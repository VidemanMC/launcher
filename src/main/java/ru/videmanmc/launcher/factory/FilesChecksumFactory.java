package ru.videmanmc.launcher.factory;

import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import ru.videmanmc.launcher.model.value.FilesChecksum;
import ru.videmanmc.launcher.mapper.PathFormatMapper;
import ru.videmanmc.launcher.model.value.files.GitHubFiles;
import ru.videmanmc.launcher.service.hashing.HashingService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class FilesChecksumFactory {

    private final HashingService hashingService;

    private final PathFormatMapper pathFormatMapper;

    /**
     * Creates {@link FilesChecksum} from file path and newly calculated hash.
     *
     * @param absolutePaths list of absolute client`s files paths
     */
    public FilesChecksum ofLocal(List<String> absolutePaths) {
        var abstractPaths = pathFormatMapper.localToAbstractFormat(absolutePaths);
        var nameHashPairs = new HashMap<String, String>();

        for (int i = 0; i < abstractPaths.size(); i++) {

            try {
                var absolutePath = absolutePaths.get(i);
                var fileBytes = Files.readAllBytes(Path.of(absolutePath));
                var hash = hashingService.calculateHash(fileBytes);

                var abstractPath = abstractPaths.get(i);
                nameHashPairs.put(abstractPath, hash);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        return new FilesChecksum(nameHashPairs);
    }

    /**
     * Creates {@link FilesChecksum} from already existed file path and hash.
     *
     * @param fileHashPairString list of file path and hash, separated by semicolon (:)
     */
    public FilesChecksum ofRemote(List<String> fileHashPairString) {
        var nameHashMap = fileHashPairString.stream()
                .map(s -> s.split(GitHubFiles.PATH_HASH_SEPARATOR))
                .collect(
                        Collectors.toMap(
                                nameHashArray -> pathFormatMapper.remoteToAbstractFormat(nameHashArray[0]),
                                nameHashArray -> nameHashArray[1]
                        )

                );

        return new FilesChecksum(nameHashMap);
    }
}
