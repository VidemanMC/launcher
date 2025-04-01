package ru.videmanmc.launcher.service.hashing;

import org.junit.jupiter.api.Test;
import ru.videmanmc.launcher.core.service.hashing.Md5HashingService;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Md5HashingServiceTest {

    @Test
    void calculateHash_givenValue_hashedCorrectly() {
        //arrange
        var md5HashingService = new Md5HashingService();
        var value = "алексей".getBytes(StandardCharsets.UTF_8);

        //act
        var hashedValue = md5HashingService.calculateHash(value);

        //assert
        assertEquals("791f7502c1741a921f916f81cf5786b", hashedValue);
    }

}
