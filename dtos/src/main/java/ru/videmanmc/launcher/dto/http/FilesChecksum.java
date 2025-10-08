package ru.videmanmc.launcher.dto.http;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Checksum of all files in the repository.
 *
 * @param fileHashPair map of pairs with file name in key, and file hash in value.
 */
public record FilesChecksum(Map<String, String> fileHashPair) {

    /**
     * Find difference between this checksum and other by file name and hash
     */
    public FilesChecksum difference(FilesChecksum other) {
        var otherHashPair = other.fileHashPair();
        var filesChecksumMap = fileHashPair.entrySet()
                .stream()
                .filter(entry -> {
                    var otherValue = otherHashPair.get(entry.getKey());
                    return Objects.isNull(otherValue) || !otherValue.equals(entry.getValue());
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return new FilesChecksum(filesChecksumMap);
    }

    public List<String> getFileNames() {
        return fileHashPair.keySet()
                .stream()
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll); //for mutability
    }
}
