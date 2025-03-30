package com.lookbook.user.domain.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
    void equals_ShouldReturnTrue_WhenEventsAreEqual() throws Exception {
        // Arrange
        UUID userId = UUID.randomUUID();
        String username = "testuser";
        String email = "test@example.com";
        LocalDateTime now = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);

        UserRegisteredEvent event1 = new UserRegisteredEvent(userId, username, email);
        UserRegisteredEvent event2 = new UserRegisteredEvent(userId, username, email);

        // Set the same timestamp for both events
        Field occurredAtField = UserRegisteredEvent.class.getSuperclass().getDeclaredField("occurredAt");
        occurredAtField.setAccessible(true);
        occurredAtField.set(event1, now);
        occurredAtField.set(event2, now);

        // Act & Assert
        assertEquals(event1, event2);
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
    void hashCode_ShouldBeEqual_WhenEventsAreEqual() throws Exception {
        // Arrange
        UUID userId = UUID.randomUUID();
        String username = "testuser";
        String email = "test@example.com";
        LocalDateTime now = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);

        UserRegisteredEvent event1 = new UserRegisteredEvent(userId, username, email);
        UserRegisteredEvent event2 = new UserRegisteredEvent(userId, username, email);

        // Set the same timestamp for both events
        Field occurredAtField = UserRegisteredEvent.class.getSuperclass().getDeclaredField("occurredAt");
        occurredAtField.setAccessible(true);
        occurredAtField.set(event1, now);
        occurredAtField.set(event2, now);

        // Act & Assert
        assertEquals(event1.hashCode(), event2.hashCode());
    }
}