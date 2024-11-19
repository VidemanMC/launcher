package ru.videmanmc.launcher.model.value;

/**
 * This class represents a http client, by which up-to-date client files is downloaded.
 */
public interface RemoteFileSource extends FileRepository {

    void synchronize(LocalFiles localFiles);
}
