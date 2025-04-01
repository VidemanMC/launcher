package ru.videmanmc.launcher.core.model.value;

import java.util.List;

/**
 * @param updateExclude List of files won`t be updated.
 */
public record SyncSettings(String minecraftVersion, List<String> updateExclude) {

}
