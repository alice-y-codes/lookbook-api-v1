package com.lookbook.user.application.events;

import java.util.UUID;

import lombok.Getter;

/**
 * Application event representing the action of creating a user profile.
 * This event is published after a user successfully registers.
 */
@Getter
public class CreateUserProfileEvent {
    private final UUID userId;
    private final String username;

    public CreateUserProfileEvent(UUID userId, String username) {
        this.userId = userId;
        this.username = username;
    }
}