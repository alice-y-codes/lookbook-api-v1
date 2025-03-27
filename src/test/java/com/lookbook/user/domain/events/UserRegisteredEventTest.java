package com.lookbook.user.domain.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class UserRegisteredEventTest {

    @Test
    void constructor_ShouldInitializeEventCorrectly() {
        // Arrange
        UUID userId = UUID.randomUUID();
        String username = "testuser";
        String email = "test@example.com";

        // Act
        UserRegisteredEvent event = new UserRegisteredEvent(userId, username, email);

        // Assert
        assertNotNull(event.getEventId());
        assertNotNull(event.getOccurredAt());
        assertEquals(userId, event.getUserId());
        assertEquals(username, event.getUsername());
        assertEquals(email, event.getEmail());
        assertEquals(userId, event.getAggregateId());

        // Verify metadata
        assertTrue(event.getMetadata().containsKey("userId"));
        assertTrue(event.getMetadata().containsKey("username"));
        assertTrue(event.getMetadata().containsKey("email"));
        assertEquals(userId.toString(), event.getMetadata().get("userId"));
        assertEquals(username, event.getMetadata().get("username"));
        assertEquals(email, event.getMetadata().get("email"));
    }
}