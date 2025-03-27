package com.lookbook.user.domain.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.util.UUID;

import org.junit.jupiter.api.Test;

class ProfileImageChangedEventTest {

    @Test
    void constructor_ShouldInitializeEventCorrectly() {
        // Arrange
        UUID userId = UUID.randomUUID();
        UUID profileId = UUID.randomUUID();
        URI newImageUrl = URI.create("https://example.com/image.jpg");
        String imageFormat = "JPEG";
        int imageWidth = 800;
        int imageHeight = 600;

        // Act
        ProfileImageChangedEvent event = new ProfileImageChangedEvent(
                userId, profileId, newImageUrl, imageFormat, imageWidth, imageHeight);

        // Assert
        assertNotNull(event.getEventId());
        assertNotNull(event.getOccurredAt());
        assertEquals(userId, event.getUserId());
        assertEquals(profileId, event.getProfileId());
        assertEquals(newImageUrl, event.getNewImageUrl());
        assertEquals(imageFormat, event.getImageFormat());
        assertEquals(imageWidth, event.getImageWidth());
        assertEquals(imageHeight, event.getImageHeight());
        assertEquals(userId, event.getAggregateId());

        // Verify metadata
        assertTrue(event.getMetadata().containsKey("userId"));
        assertTrue(event.getMetadata().containsKey("profileId"));
        assertTrue(event.getMetadata().containsKey("newImageUrl"));
        assertTrue(event.getMetadata().containsKey("imageFormat"));
        assertTrue(event.getMetadata().containsKey("imageWidth"));
        assertTrue(event.getMetadata().containsKey("imageHeight"));
        assertEquals(userId.toString(), event.getMetadata().get("userId"));
        assertEquals(profileId.toString(), event.getMetadata().get("profileId"));
        assertEquals(newImageUrl.toString(), event.getMetadata().get("newImageUrl"));
        assertEquals(imageFormat, event.getMetadata().get("imageFormat"));
        assertEquals(imageWidth, event.getMetadata().get("imageWidth"));
        assertEquals(imageHeight, event.getMetadata().get("imageHeight"));
    }
}