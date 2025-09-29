package ru.videmanmc.launcher.mapper;

import org.junit.jupiter.api.Test;
import ru.videmanmc.launcher.http_client.github.PathFormatMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PathFormatMapperTest {

    private final PathFormatMapper mapper = new PathFormatMapper();

    @Test
    void localToAbstractFormat_givenLocalAbsolutePaths_returnsAbstractPaths() {
        //arrange
        var localPaths = List.of("C:/Users/Aleks/.videmanmc/client/mods/a.jar");

        //act
        var abstractedLocalPaths = mapper.localToAbstractFormat(localPaths);

        //assert
        assertEquals("mods/a.jar", abstractedLocalPaths.getFirst());
    }

    @Test
    void abstractToRemoteFormat_givenAbstractPaths_returnsRemotePaths() {
        //arrange
        var abstractPaths = List.of("mods/a.jar");
        var remoteOriginalNames = List.of("generic/mods/a.jar");

        //act
        var remotePaths = mapper.abstractToRemoteFormat(abstractPaths, remoteOriginalNames);

        //assert
        assertEquals("generic/mods/a.jar", remotePaths.getFirst());
    }
}
