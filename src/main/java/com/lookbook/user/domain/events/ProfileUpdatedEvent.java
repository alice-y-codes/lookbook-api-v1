package com.lookbook.user.domain.events;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.lookbook.base.domain.events.BaseDomainEvent;

/**
 * Event raised when a user profile is updated.
 */
public class ProfileUpdatedEvent extends BaseDomainEvent {

    private final UUID userId;
    private final UUID profileId;
    private final String displayName;
    private final String biography;

    public ProfileUpdatedEvent(UUID userId, UUID profileId, String displayName, String biography) {
        super(createMetadata(userId, profileId, displayName, biography));
        this.userId = userId;
        this.profileId = profileId;
        this.displayName = displayName;
        this.biography = biography;
    }

    private static Map<String, Object> createMetadata(UUID userId, UUID profileId, String displayName,
            String biography) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("userId", userId.toString());
        metadata.put("profileId", profileId.toString());
        metadata.put("displayName", displayName);
        metadata.put("biography", biography);
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

    public String getBiography() {
        return biography;
    }
}