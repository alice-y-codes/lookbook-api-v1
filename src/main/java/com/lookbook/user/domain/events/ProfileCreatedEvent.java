package com.lookbook.user.domain.events;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.lookbook.base.domain.events.BaseDomainEvent;

/**
 * Event raised when a new user profile is created.
 */
public class ProfileCreatedEvent extends BaseDomainEvent {

    private final UUID userId;
    private final UUID profileId;
    private final String displayName;

    public ProfileCreatedEvent(UUID userId, UUID profileId, String displayName) {
        super(createMetadata(userId, profileId, displayName));
        this.userId = userId;
        this.profileId = profileId;
        this.displayName = displayName;
    }

    private static Map<String, Object> createMetadata(UUID userId, UUID profileId, String displayName) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("userId", userId.toString());
        metadata.put("profileId", profileId.toString());
        metadata.put("displayName", displayName);
        return metadata;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getProfileId() {
        return profileId;
    }

    public String getDisplayName() {
        return displayName;
    }
}