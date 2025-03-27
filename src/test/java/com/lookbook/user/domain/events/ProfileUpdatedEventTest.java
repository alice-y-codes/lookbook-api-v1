package com.lookbook.user.domain.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class ProfileUpdatedEventTest {

    @Test
    void constructor_ShouldInitializeEventCorrectly() {
        // Arrange
        UUID userId = UUID.randomUUID();
        UUID profileId = UUID.randomUUID();
        String displayName = "Updated Name";
        String biography = "Updated bio";

        // Act
        ProfileUpdatedEvent event = new ProfileUpdatedEvent(userId, profileId, displayName, biography);

        // Assert
        assertNotNull(event.getEventId());
        assertNotNull(event.getOccurredAt());
        assertEquals(userId, event.getUserId());
        assertEquals(profileId, event.getProfileId());
        assertEquals(displayName, event.getDisplayName());
        assertEquals(biography, event.getBiography());
        assertEquals(userId, event.getAggregateId());

        // Verify metadata
        assertTrue(event.getMetadata().containsKey("userId"));
        assertTrue(event.getMetadata().containsKey("profileId"));
        assertTrue(event.getMetadata().containsKey("displayName"));
        assertTrue(event.getMetadata().containsKey("biography"));
        assertEquals(userId.toString(), event.getMetadata().get("userId"));
        assertEquals(profileId.toString(), event.getMetadata().get("profileId"));
        assertEquals(displayName, event.getMetadata().get("displayName"));
        assertEquals(biography, event.getMetadata().get("biography"));
    }
}