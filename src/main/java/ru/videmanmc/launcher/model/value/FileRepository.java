package ru.videmanmc.launcher.model.value;

import java.util.Map;

/**
 * This interface is used for getting files from some source.
 */
public interface FileRepository {

    Map<String, String> getFiles();
}
