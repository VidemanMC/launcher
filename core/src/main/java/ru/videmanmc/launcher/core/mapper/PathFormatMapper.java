package ru.videmanmc.launcher.core.mapper;

import java.util.List;
import java.util.stream.Collectors;

import static ru.videmanmc.launcher.constants.WorkingDirectoryConstants.CLIENT_SUBDIRECTORY_PATH;

public class PathFormatMapper {

    private final String ABSTRACT_DIR_SEPARATOR = "/";

    private final String WINDOWS_ESCAPED_DIR_SEPARATOR = "\\\\";

    public List<String> abstractToRemoteFormat(List<String> abstractFileNames, List<String> remoteFileNamesMask) {
        return remoteFileNamesMask.stream()
                .filter(remoteFileName ->
                        abstractFileNames.contains(remoteToAbstractFormat(remoteFileName))
                )
                .collect(Collectors.toList());
    }

    public String remoteToAbstractFormat(String gitHubPath) {
        return gitHubPath
                .replace("client/", "")
                .replace("generic/", "");
    }

    public List<String> localToAbstractFormat(List<String> localChecksumFileNames) {
        return localChecksumFileNames.stream()
                .map(str -> str.replaceAll(WINDOWS_ESCAPED_DIR_SEPARATOR, ABSTRACT_DIR_SEPARATOR))
                .map(str -> {
                    int clientIndex = str.indexOf(CLIENT_SUBDIRECTORY_PATH);
                    return str.substring(clientIndex + CLIENT_SUBDIRECTORY_PATH.length());
                })
                .toList();
    }
}
