package ru.videmanmc.launcher.bootloader.secondary.repository;

import lombok.SneakyThrows;
import org.jetbrains.annotations.Nullable;
import ru.videmanmc.launcher.http.client.domain.entity.Binary;
import ru.videmanmc.launcher.http.client.domain.value.Hash;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static ru.videmanmc.launcher.constants.WorkingDirectoryConstants.MAIN_DIRECTORY_PATH;

public class BinaryRepository {

    private final Path home;

    public static final String HASH_FILE_NAME = "hash";

    private final Path cachedHash;

    public BinaryRepository(Path home) {
        this.home = home;
        this.cachedHash = home.resolve(HASH_FILE_NAME);
    }

    @SuppressWarnings("unused") // actually used by Guice
    public BinaryRepository() {
        this.home = Path.of(MAIN_DIRECTORY_PATH);
        this.cachedHash = home.resolve(HASH_FILE_NAME);
    }

    /**
     * Searches a binary with the prefix in the binary name.
     *
     * @param prefix prefix to search by
     * @return found binary, null otherwise
     */
    @Nullable
    public Binary findByPrefix(String prefix) {
        try (Stream<Path> stream = Files.walk(home, 1)) {
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
        var content = Files.readAllBytes(home.resolve(binaryName));
        var hash = readCachedHash();

        return new Binary(
                new Hash(hash),
                content,
                binaryName
        );
    }

    @SneakyThrows
    private String readCachedHash() {
        if (Files.exists(cachedHash)) {
            return Files.readString(cachedHash);
        }

        return "";
    }

    @SneakyThrows
    public void save(Binary binary) {
        var path = home.resolve(binary.name());
        Files.write(path, binary.content());
        cacheHash(binary.hash()
                        .hash());
    }

    @SneakyThrows
    private void cacheHash(String hash) {
        Files.write(cachedHash, hash.getBytes());
    }
}
