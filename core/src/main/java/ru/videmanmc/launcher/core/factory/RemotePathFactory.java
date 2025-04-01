package ru.videmanmc.launcher.core.factory;


import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import ru.videmanmc.launcher.core.mapper.PathFormatMapper;
import ru.videmanmc.launcher.core.model.value.RemotePath;

import java.util.List;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class RemotePathFactory {

    private final PathFormatMapper pathFormatMapper;

    public List<RemotePath> create(List<String> paths, List<String> originalRemoteFileNames) {
        return pathFormatMapper.abstractToRemoteFormat(paths, originalRemoteFileNames)
                .stream()
                .map(RemotePath::new)
                .toList();
    }

}
