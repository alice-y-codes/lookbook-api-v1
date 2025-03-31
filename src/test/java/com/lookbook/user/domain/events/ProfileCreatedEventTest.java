package com.lookbook.user.domain.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class ProfileCreatedEventTest {

    @Test
    void constructor_ShouldInitializeEventCorrectly() {
        // Arrange
        UUID userId = UUID.randomUUID();
        UUID profileId = UUID.randomUUID();
        String displayName = "Test User";

        // Act
        ProfileCreatedEvent event = new ProfileCreatedEvent(userId, profileId, displayName);

        // Assert
        assertNotNull(event.getEventId());
        assertNotNull(event.getOccurredAt());
        assertEquals(userId, event.getUserId());
        assertEquals(profileId, event.getProfileId());
        assertEquals(displayName, event.getDisplayName());
        assertEquals(userId, event.getAggregateId());

        // Verify metadata
        assertTrue(event.getMetadata().containsKey("userId"));
        assertTrue(event.getMetadata().containsKey("profileId"));
        assertTrue(event.getMetadata().containsKey("displayName"));
        assertEquals(userId.toString(), event.getMetadata().get("userId"));
        assertEquals(profileId.toString(), event.getMetadata().get("profileId"));
        assertEquals(displayName, event.getMetadata().get("displayName"));
    }
}