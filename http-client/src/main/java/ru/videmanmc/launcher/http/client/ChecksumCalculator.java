package ru.videmanmc.launcher.http.client;

import ru.videmanmc.launcher.http.client.domain.value.FilesChecksum;

public sealed interface ChecksumCalculator permits RemoteChecksumCalculator, LocalChecksumCalculator {

    /**
     * Calculate checksum of all files
     */
    FilesChecksum calculateChecksum();
}
