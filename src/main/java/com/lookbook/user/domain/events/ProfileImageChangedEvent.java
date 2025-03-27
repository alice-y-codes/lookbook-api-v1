package com.lookbook.user.domain.events;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.lookbook.base.domain.events.BaseDomainEvent;

/**
 * Event raised when a user's profile image is changed.
 */
public class ProfileImageChangedEvent extends BaseDomainEvent {

    private final UUID userId;
    private final UUID profileId;
    private final URI newImageUrl;
    private final String imageFormat;
    private final int imageWidth;
    private final int imageHeight;

    public ProfileImageChangedEvent(UUID userId, UUID profileId, URI newImageUrl,
            String imageFormat, int imageWidth, int imageHeight) {
        super(createMetadata(userId, profileId, newImageUrl, imageFormat, imageWidth, imageHeight));
        this.userId = userId;
        this.profileId = profileId;
        this.newImageUrl = newImageUrl;
        this.imageFormat = imageFormat;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
    }

    private static Map<String, Object> createMetadata(UUID userId, UUID profileId, URI newImageUrl,
            String imageFormat, int imageWidth, int imageHeight) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("userId", userId.toString());
        metadata.put("profileId", profileId.toString());
        metadata.put("newImageUrl", newImageUrl.toString());
        metadata.put("imageFormat", imageFormat);
        metadata.put("imageWidth", imageWidth);
        metadata.put("imageHeight", imageHeight);
        return metadata;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getProfileId() {
        return profileId;
    }

    public URI getNewImageUrl() {
        return newImageUrl;
    }

    public String getImageFormat() {
        return imageFormat;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    @Override
    public UUID getAggregateId() {
        return userId;
    }
}