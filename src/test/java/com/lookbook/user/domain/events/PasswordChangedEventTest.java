package com.lookbook.user.domain.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class PasswordChangedEventTest {

    @Test
    void constructor_ShouldInitializeEventCorrectly() {
        // Arrange
        UUID userId = UUID.randomUUID();
        String username = "testuser";

        // Act
        PasswordChangedEvent event = new PasswordChangedEvent(userId, username);

        // Assert
        assertNotNull(event.getEventId());
        assertNotNull(event.getOccurredAt());
        assertEquals(userId, event.getUserId());
        assertEquals(username, event.getUsername());
        assertEquals(userId, event.getAggregateId());

        // Verify metadata
        assertTrue(event.getMetadata().containsKey("userId"));
        assertTrue(event.getMetadata().containsKey("username"));
        assertEquals(userId.toString(), event.getMetadata().get("userId"));
        assertEquals(username, event.getMetadata().get("username"));
    }
}