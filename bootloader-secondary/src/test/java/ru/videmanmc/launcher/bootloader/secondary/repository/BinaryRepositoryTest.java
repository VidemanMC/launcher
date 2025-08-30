package ru.videmanmc.launcher.bootloader.secondary.repository;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import org.junit.jupiter.api.Test;
import ru.videmanmc.launcher.http.client.domain.entity.Binary;
import ru.videmanmc.launcher.http.client.domain.value.Hash;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class BinaryRepositoryTest {

    @Test
    void findByPrefix_fileExists_binaryReturned() throws IOException {
        // arrange
        FileSystem fs = Jimfs.newFileSystem(Configuration.unix());
        var root = fs.getPath("");
        var wanted = root.resolve("abc.d");

        Files.createFile(wanted);

        var repository = new BinaryRepository(root);

        // act
        var binary = repository.findByPrefix("a");

        // assert
        assertNotNull(binary);
    }

    @Test
    void findByPrefix_fileNotExists_nullReturned() throws IOException {
        // arrange
        FileSystem fs = Jimfs.newFileSystem(Configuration.unix());
        var root = fs.getPath("");

        Files.createDirectories(root);

        var repository = new BinaryRepository(root);

        // act
        var binary = repository.findByPrefix("a");

        // assert
        assertNull(binary);
    }

    @Test
    void save_savedBinary_hashAndBinaryAppearedInFileSystem() throws IOException {
        // arrange
        FileSystem fs = Jimfs.newFileSystem(Configuration.unix());
        var root = fs.getPath("");
        Files.createDirectories(root);

        var repository = new BinaryRepository(root);
        var binary = new Binary(
                new Hash("0"),
                new byte[]{1},
                "a"
        );

        // act
        repository.save(binary);
        var savedHash = Files.readString(root.resolve(BinaryRepository.HASH_FILE_NAME));
        var savedBinaryContent = Files.readAllBytes(root.resolve(binary.name()));

        // assert
        assertEquals(
                binary.hash()
                      .hash(), savedHash
        );
        assertArrayEquals(binary.content(), savedBinaryContent);
    }
}