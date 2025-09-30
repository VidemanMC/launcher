package ru.videmanmc.launcher.core.listener;

import com.google.inject.Inject;
import fun.bb1.events.abstraction.listener.EventHandler;
import fun.bb1.events.abstraction.listener.IEventListener;
import lombok.RequiredArgsConstructor;
import ru.videmanmc.launcher.core.service.GameRunningService;
import ru.videmanmc.launcher.events.GameLaunchInitiated;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class GameRunListener implements IEventListener {

    private final GameRunningService gameRunningService;

    @EventHandler(GameLaunchInitiated.EVENT_NAME)
    public void on(GameLaunchInitiated.Payload payload) {
        gameRunningService.run(payload.login(), payload.isOnline());
    }

}
