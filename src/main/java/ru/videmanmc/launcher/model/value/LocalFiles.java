package ru.videmanmc.launcher.model.value;

import java.util.Map;

public class LocalFiles implements FileRepository {

    private FilesState filesState = FilesState.ACTUAL;

    @Override
    public Map<String, String> getFiles() {
        return Map.of();
    }

    public boolean isValid() {
        return filesState == FilesState.OUTDATED;
    }

    public enum FilesState {
        /**
         * Client files are up-to-date and do not require updating.
         */
        ACTUAL,

        /**
         * Client files are outdated and need to be updated.
         */
        OUTDATED
    }
}
