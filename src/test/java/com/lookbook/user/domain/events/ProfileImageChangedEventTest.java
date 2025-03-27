package com.lookbook.user.domain.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ProfileImageChangedEvent")
class ProfileImageChangedEventTest {

    private UUID userId;
    private UUID profileId;
    private URI newImageUrl;
    private String imageFormat;
    private int imageWidth;
    private int imageHeight;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        profileId = UUID.randomUUID();
        newImageUrl = URI.create("https://example.com/profile.jpg");
        imageFormat = "jpg";
        imageWidth = 800;
        imageHeight = 600;
    }

    @Test
    @DisplayName("should create event with valid data")
    void shouldCreateEventWithValidData() {
        // When
        ProfileImageChangedEvent event = new ProfileImageChangedEvent(userId, profileId, newImageUrl,
                imageFormat, imageWidth, imageHeight);

        // Then
        assertNotNull(event);
        assertEquals(userId, event.getUserId());
        assertEquals(profileId, event.getProfileId());
        assertEquals(newImageUrl, event.getNewImageUrl());
        assertEquals(imageFormat, event.getImageFormat());
        assertEquals(imageWidth, event.getImageWidth());
        assertEquals(imageHeight, event.getImageHeight());
    }

    @Test
    @DisplayName("should create metadata with all properties")
    void shouldCreateMetadataWithAllProperties() {
        // When
        ProfileImageChangedEvent event = new ProfileImageChangedEvent(userId, profileId, newImageUrl,
                imageFormat, imageWidth, imageHeight);
        Map<String, Object> metadata = event.getMetadata();

        // Then
        assertEquals(userId.toString(), metadata.get("userId"));
        assertEquals(profileId.toString(), metadata.get("profileId"));
        assertEquals(newImageUrl.toString(), metadata.get("newImageUrl"));
        assertEquals(imageFormat, metadata.get("imageFormat"));
        assertEquals(imageWidth, metadata.get("imageWidth"));
        assertEquals(imageHeight, metadata.get("imageHeight"));
    }
}