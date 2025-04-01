package ru.videmanmc.launcher.core.model.value.files;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Describes which client files need not be updated or deleted.
 */
public record IgnoredFiles(List<String> filterRules) {

    public List<String> filter(List<String> nonFilteredFiles) {
        return filterWildcardElements(filterDir(filterFile(nonFilteredFiles))); //todo убрать эту порнушку
    }

    private List<String> filterWildcardElements(List<String> nonFilteredFiles) {
        return nonFilteredFiles.stream()
                .filter(nonFilteredFile ->

                        filterRules.stream()
                                .noneMatch(filterRule -> {
                                    int wildCardIndex = filterRule.indexOf("*");
                                    if (wildCardIndex == -1) return false;

                                    var prefix = filterRule.substring(0, wildCardIndex);

                                    return filterRule.contains("*") && nonFilteredFile.startsWith(prefix);
                                })

                )
                .collect(Collectors.toList());
    }

    private List<String> filterDir(List<String> nonFilteredFiles) {
        return nonFilteredFiles.stream()
                .filter(nonFilteredFile ->

                        filterRules.stream()
                                .noneMatch(filterRule ->
                                        filterRule.endsWith("/") && nonFilteredFile.startsWith(filterRule)
                                )

                )
                .collect(Collectors.toList());
    }

    private List<String> filterFile(List<String> nonFilteredFiles) {
        return nonFilteredFiles.stream()
                .filter(nonFilteredFile ->

                        filterRules.stream()
                                .noneMatch(filterRule ->
                                        !filterRule.endsWith("/") && nonFilteredFile.equals(filterRule)
                                )

                )
                .collect(Collectors.toList());
    }
}
