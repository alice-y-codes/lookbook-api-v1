package com.lookbook.user.domain.events;

import java.util.Map;
import java.util.UUID;

import com.lookbook.base.domain.events.BaseDomainEvent;

import lombok.Getter;

/**
 * Event raised when a new user registers.
 */
@Getter
public class UserRegisteredEvent extends BaseDomainEvent {
    private final UUID userId;
    private final String username;
    private final String email;

    public UserRegisteredEvent(UUID userId, String username, String email) {
        super(Map.of(
                "userId", userId,
                "username", username,
                "email", email));
        this.userId = userId;
        this.username = username;
        this.email = email;
    }

    @Override
    public UUID getAggregateId() {
        return userId;
    }
}