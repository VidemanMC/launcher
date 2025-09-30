package ru.videmanmc.launcher.events;

import fun.bb1.events.abstraction.Event;

/**
 * Triggered when the user starts the game launching
 */
public class GameLaunchInitiated extends Event<GameLaunchInitiated.Payload> {

    public static final String EVENT_NAME = "GameLaunchInitiated";

    public GameLaunchInitiated() {
        super(Payload.class, EVENT_NAME);
    }

    /**
     *
     * @param login user login
     * @param isOffline whether to run game in offline mode (skip update process)
     */
    public record Payload(String login, boolean isOffline) {}
}
