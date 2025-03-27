package com.lookbook.user.domain.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ProfileUpdatedEvent")
class ProfileUpdatedEventTest {

    private UUID userId;
    private UUID profileId;
    private String displayName;
    private String biography;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        profileId = UUID.randomUUID();
        displayName = "John Doe";
        biography = "A fashion enthusiast who loves creating unique outfits";
    }

    @Test
    @DisplayName("should create event with valid data")
    void shouldCreateEventWithValidData() {
        // When
        ProfileUpdatedEvent event = new ProfileUpdatedEvent(userId, profileId, displayName, biography);

        // Then
        assertNotNull(event);
        assertEquals(userId, event.getUserId());
        assertEquals(profileId, event.getProfileId());
        assertEquals(displayName, event.getDisplayName());
        assertEquals(biography, event.getBiography());
    }

    @Test
    @DisplayName("should create metadata with all properties")
    void shouldCreateMetadataWithAllProperties() {
        // When
        ProfileUpdatedEvent event = new ProfileUpdatedEvent(userId, profileId, displayName, biography);
        Map<String, Object> metadata = event.getMetadata();

        // Then
        assertEquals(userId.toString(), metadata.get("userId"));
        assertEquals(profileId.toString(), metadata.get("profileId"));
        assertEquals(displayName, metadata.get("displayName"));
        assertEquals(biography, metadata.get("biography"));
    }
}