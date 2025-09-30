package ru.videmanmc.launcher.core.listener;

import com.google.inject.Inject;
import fun.bb1.events.abstraction.listener.EventHandler;
import fun.bb1.events.abstraction.listener.IEventListener;
import lombok.RequiredArgsConstructor;
import ru.videmanmc.launcher.core.error_handling.GlobalExceptionHandler;
import ru.videmanmc.launcher.core.exception.AutomaticInstallationImpossible;
import ru.videmanmc.launcher.core.service.GameRunningService;
import ru.videmanmc.launcher.events.GameLaunchInitiated;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class GameRunListener implements IEventListener {

    private final GameRunningService gameRunningService;

    @EventHandler(GameLaunchInitiated.EVENT_NAME)
    public void on(GameLaunchInitiated.Payload payload) {
        try {
            gameRunningService.run(payload.login(), payload.isOnline());
        } catch (AutomaticInstallationImpossible e) {
            new GlobalExceptionHandler().uncaughtException(null, e); // for unknown reason GlobalExceptionHandler set in main method doesn't catch exceptions from ui threads
        }
    }

}
