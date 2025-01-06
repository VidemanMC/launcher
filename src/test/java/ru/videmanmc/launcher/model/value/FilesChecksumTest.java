package ru.videmanmc.launcher.model.value;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FilesChecksumTest {

    @Test
    void difference_sameKeysDifferentValues_returnsAllElements() {

        var mapB = Map.of("1", "O");
        var mapA = mapA();

        var differenceAB = AB_difference(mapB, mapA).getFileNames();

        assertTrue(differenceAB.containsAll(
                List.of("1", "2", "3")
        ));
    }

    @Test
    void difference_sameKeysSameValues_returnsOnly2ElementsFromFirstMap() {
        var mapB = Map.of("3", "C");
        var mapA = mapA();

        var differenceAB = AB_difference(mapB, mapA).getFileNames();

        assertTrue(differenceAB.containsAll(
                List.of("1", "2")
        ));
    }

    @Test
    void difference_differentKeysSameValues_returnsSameValues() {
        var mapB = Map.of("5", "C");
        var mapA = mapA();

        var differenceAB = AB_difference(mapB, mapA).getFileNames();

        assertTrue(differenceAB.containsAll(
                List.of("1", "2", "3")
        ));
    }

    private FilesChecksum AB_difference(Map<String, String> mapB, Map<String, String> mapA) {
        var checkSumA = new FilesChecksum(mapA);
        var checkSumB = new FilesChecksum(mapB);

        return checkSumA.difference(checkSumB);
    }

    static Map<String, String> mapA() {
        return Map.of(
                "1", "A",
                "2", "B",
                "3", "C"
        );
    }
}
