package ru.videmanmc.launcher.model.value.files;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.videmanmc.launcher.core.model.value.files.IgnoredFiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

class IgnoredFilesTest {

    @Test
    void filter_fileNameInRules_removesFileName() {
        //arrange
        var nonFilteredNames = List.of(
                "a.txt"
        );

        var rules = List.of(
                "a.txt"
        );

        var ignoredFiles = new IgnoredFiles(rules);


        //act
        var filteredNames = ignoredFiles.filter(nonFilteredNames);


        //assert
        assertFalse(filteredNames.contains(nonFilteredNames.getFirst()));
    }

    @Test
    void filter_dirNameInRules_removesFileNamesByDirPath() {
        //arrange
        var nonFilteredNames = List.of(
                "config/a.txt"
        );

        var rules = List.of(
                "config/"
        );

        var ignoredFiles = new IgnoredFiles(rules);


        //act
        var filteredNames = ignoredFiles.filter(nonFilteredNames);


        //assert
        assertFalse(filteredNames.contains(nonFilteredNames.getFirst()));
    }

    @ParameterizedTest
    @CsvSource({
            "c*, config/a.txt",
            "b*, b.txt"
    })
    void filter_wildCardInRules_removesFitElements(String filterRule, String path) {
        //arrange
        var nonFilteredNames = List.of(path);
        var rules = List.of(filterRule);
        var ignoredFiles = new IgnoredFiles(rules);

        //act
        var filteredNames = ignoredFiles.filter(nonFilteredNames);

        //assert
        assertFalse(filteredNames.contains(nonFilteredNames.getFirst()));
    }

}
