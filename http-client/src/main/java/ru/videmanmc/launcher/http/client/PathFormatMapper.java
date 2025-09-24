package ru.videmanmc.launcher.http.client;

import lombok.RequiredArgsConstructor;

import java.util.List;

import static ru.videmanmc.launcher.constants.WorkingDirectoryConstants.CLIENT_SUBDIRECTORY_PATH;

@RequiredArgsConstructor
public class PathFormatMapper {

    private static final String ABSTRACT_DIR_SEPARATOR = "/";

    private static final String WINDOWS_ESCAPED_DIR_SEPARATOR = "\\";

    /**
     * Finds intersection between abstract files and remote files, then returns result.
     *
     * @return new list of intersected file paths
     */
    public List<String> abstractToRemoteFormat(List<String> abstractFileNames, List<String> remoteFileNamesMask) {
        return remoteFileNamesMask.stream()
                                  .filter(remoteFileName ->
                                                  abstractFileNames.contains(remoteToAbstractFormat(remoteFileName))
                                  )
                                  .toList();
    }

    public String remoteToAbstractFormat(String gitHubPath) {
        return gitHubPath
                .replace("client/", "")
                .replace("generic/", "");
    }

    public List<String> localToAbstractFormat(List<String> localChecksumFileNames) {
        return localChecksumFileNames.stream()
                                     .map(str -> str.replace(WINDOWS_ESCAPED_DIR_SEPARATOR, ABSTRACT_DIR_SEPARATOR))
                                     .map(str -> {
                                         int clientIndex = str.indexOf(CLIENT_SUBDIRECTORY_PATH);
                                         return str.substring(clientIndex + CLIENT_SUBDIRECTORY_PATH.length());
                                     })
                                     .toList();
    }
}
