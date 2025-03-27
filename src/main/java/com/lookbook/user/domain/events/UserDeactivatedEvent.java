package com.lookbook.user.domain.events;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.lookbook.base.domain.events.BaseDomainEvent;

/**
 * Event raised when a user is deactivated.
 */
public class UserDeactivatedEvent extends BaseDomainEvent {

    private final UUID userId;
    private final String username;

    public UserDeactivatedEvent(UUID userId, String username) {
        super(createMetadata(userId, username));
        this.userId = userId;
        this.username = username;
    }

    private static Map<String, Object> createMetadata(UUID userId, String username) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("userId", userId.toString());
        metadata.put("username", username);
        return metadata;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public UUID getAggregateId() {
        return userId;
    }
}