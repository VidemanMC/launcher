package ru.videmanmc.launcher.bootloader.service;

import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import ru.videmanmc.launcher.bootloader.repository.BinaryRepository;
import ru.videmanmc.launcher.dto.http.BinaryInfo;
import ru.videmanmc.launcher.http_client.github.BinaryClient;

import static ru.videmanmc.launcher.constants.BinariesNamingConstants.LAUNCHER_PREFIX;


/**
 * Gets new {@link ru.videmanmc.launcher.dto.http.Binary} if it's available.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class UpdatingService {

    private final BinaryRepository binaryRepository;

    private final BinaryClient binaryClient;

    /**
     * If it`s needed, does update the launcher
     *
     * @return neither updated nor current launcher file name
     */
    public String update() {
        var binary = binaryRepository.findByPrefix(LAUNCHER_PREFIX);
        var remoteBinaryInfo = binaryClient.getInfoByPrefix(LAUNCHER_PREFIX);

        if (binary == null) {
            return downloadRemoteBinary(remoteBinaryInfo);
        }

        boolean hashesNotEqual = binary.hash()
                                       .hash()
                                       .equals(remoteBinaryInfo.hash());

        if (hashesNotEqual) {
            return downloadRemoteBinary(remoteBinaryInfo);
        }

        return binary.name();
    }

    private String downloadRemoteBinary(BinaryInfo info) {
        var downloadedBinary = binaryClient.getBinary(info);
        binaryRepository.save(downloadedBinary);

        return downloadedBinary.name();
    }

}
