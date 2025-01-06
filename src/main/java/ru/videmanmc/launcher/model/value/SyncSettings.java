package ru.videmanmc.launcher.model.value;

import java.util.List;

public record SyncSettings(String minecraftVersion, List<String> updateExclude) {

}
