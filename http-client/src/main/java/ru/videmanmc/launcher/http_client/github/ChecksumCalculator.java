package ru.videmanmc.launcher.http_client.github;


import ru.videmanmc.launcher.dto.http.FilesChecksum;

public sealed interface ChecksumCalculator permits RemoteChecksumCalculator, LocalChecksumCalculator {

    /**
     * Calculate checksum of all files
     */
    FilesChecksum calculateChecksum();
}
