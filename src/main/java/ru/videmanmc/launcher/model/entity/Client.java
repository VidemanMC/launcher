package ru.videmanmc.launcher.model.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.videmanmc.launcher.model.value.LocalFiles;
import ru.videmanmc.launcher.model.value.RemoteFileSource;
import ru.videmanmc.launcher.service.ClientSynchronizationService;

@Getter
@RequiredArgsConstructor
public class Client {

    private final LocalFiles contents;
    private final RemoteFileSource source;

    private final ClientSynchronizationService synchronizationService;

    public void run() {
        synchronizationService.synchronize(this);
        if (!isRunReady()) {
            throw new IllegalStateException("Client is not ready");
        }


        //run game through internals
    }

    private boolean isRunReady() {
        return contents.isValid();
    }

}
