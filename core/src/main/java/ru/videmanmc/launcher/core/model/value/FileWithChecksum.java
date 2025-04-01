package ru.videmanmc.launcher.core.model.value;

/**
 * This interface is used for getting files from some source.
 */
public interface FileWithChecksum {

    FilesChecksum calculateChecksum();
}
