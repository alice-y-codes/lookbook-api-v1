package com.lookbook.user.domain.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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
        assertEquals(userId, event.getMetadata().get("userId"));
        assertEquals(username, event.getMetadata().get("username"));
        assertEquals(email, event.getMetadata().get("email"));
    }

    @Test
    void equals_ShouldReturnTrue_WhenEventsAreTheSame() throws Exception {
        // Arrange
        UUID userId = UUID.randomUUID();
        String username = "testuser";
        String email = "test@example.com";

        UserRegisteredEvent event = new UserRegisteredEvent(userId, username, email);

        // Act & Assert
        assertEquals(event, event);
    }

    @Test
    void equals_ShouldReturnFalse_WhenEventsAreDifferent() {
        // Arrange
        String username = "testuser";
        String email = "test@example.com";

        UserRegisteredEvent event1 = new UserRegisteredEvent(UUID.randomUUID(), username, email);
        UserRegisteredEvent event2 = new UserRegisteredEvent(UUID.randomUUID(), username, email);

        // Act & Assert
        assertNotEquals(event1, event2);
    }

    @Test
    void hashCode_ShouldBeEqual_WhenEventsAreTheSame() throws Exception {
        // Arrange
        UUID userId = UUID.randomUUID();
        String username = "testuser";
        String email = "test@example.com";

        UserRegisteredEvent event = new UserRegisteredEvent(userId, username, email);

        // Act & Assert
        assertEquals(event.hashCode(), event.hashCode());
    }
}