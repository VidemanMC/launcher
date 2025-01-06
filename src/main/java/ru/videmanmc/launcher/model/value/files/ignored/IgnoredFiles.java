package ru.videmanmc.launcher.model.value.files.ignored;

import java.util.List;

/**
 * Describes which client files need not be updated or deleted.
 */
public record IgnoredFiles(List<String> excludedFiles) {

}
