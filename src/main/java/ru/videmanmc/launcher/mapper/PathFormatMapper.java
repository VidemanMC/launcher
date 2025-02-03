package ru.videmanmc.launcher.mapper;

import ru.videmanmc.launcher.model.value.Settings;

import java.util.List;
import java.util.stream.Collectors;

public class PathFormatMapper {

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
                .map(str -> str.replaceAll("\\\\", "/"))
                .map(str -> {
                    int clientIndex = str.indexOf(Settings.CLIENT_SUBDIRECTORY_PATH);
                    return str.substring(clientIndex + Settings.CLIENT_SUBDIRECTORY_PATH.length());
                })
                .toList();
    }
}
