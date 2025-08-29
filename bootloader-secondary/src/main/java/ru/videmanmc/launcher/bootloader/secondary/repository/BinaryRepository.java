package ru.videmanmc.launcher.bootloader.secondary.repository;

import lombok.SneakyThrows;
import ru.videmanmc.launcher.constants.WorkingDirectoryConstants;
import ru.videmanmc.launcher.http.client.domain.entity.Binary;
import ru.videmanmc.launcher.http.client.domain.value.Hash;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class BinaryRepository {

    private final Path CACHED_HASH = Path.of(WorkingDirectoryConstants.MAIN_DIRECTORY_PATH + "hash");

    /**
     * Searches a binary with the prefix in the binary name.
     *
     * @param prefix prefix to search by
     * @return found binary
     */
    public Binary findByPrefix(String prefix) {
        var homePath = Path.of(WorkingDirectoryConstants.MAIN_DIRECTORY_PATH);
        try (Stream<Path> stream = Files.walk(homePath)) {
            return stream
                    .filter(Files::isRegularFile)
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .filter(fileName -> fileName.startsWith(prefix))
                    .findFirst()
                    .map(this::construct)
                    .orElse(null);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    private Binary construct(String binaryName) {
        var content = Files.readAllBytes(Path.of(WorkingDirectoryConstants.MAIN_DIRECTORY_PATH + binaryName));
        var hash = readCachedHash();

        return new Binary(
                new Hash(hash),
                content,
                binaryName
        );
    }

    @SneakyThrows
    private String readCachedHash() {
        if (Files.exists(CACHED_HASH)) {
            return Files.readString(CACHED_HASH);
        }

        return "";
    }

    @SneakyThrows
    public void save(Binary binary) {
        var path = Path.of(WorkingDirectoryConstants.MAIN_DIRECTORY_PATH + binary.name());
        Files.write(path, binary.content());
        cacheHash(binary.hash().hash());
    }

    @SneakyThrows
    private void cacheHash(String hash) {
        Files.write(CACHED_HASH, hash.getBytes());
    }
}
