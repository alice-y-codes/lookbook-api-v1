package com.lookbook.base.domain.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link BaseDomainEvent} class.
 */
class BaseDomainEventTest {

    /**
     * A concrete domain event implementation for testing.
     */
    private static class TestEvent extends BaseDomainEvent {
        private final String data;

        public TestEvent(String data) {
            this.data = data;
        }

        public TestEvent(String data, Map<String, Object> metadata) {
            super(metadata);
            this.data = data;
        }

        public TestEvent(UUID eventId, LocalDateTime occurredAt, Map<String, Object> metadata, String data) {
            super(eventId, occurredAt, metadata);
            this.data = data;
        }

        public String getData() {
            return data;
        }
    }

    /**
     * Another event type for testing type discrimination.
     */
    private static class OtherTestEvent extends BaseDomainEvent {
        public OtherTestEvent() {
            super();
        }
    }

    @Test
    void constructor_Default_ShouldInitializeWithEmptyMetadata() {
        TestEvent event = new TestEvent("test-data");

        assertNotNull(event.getEventId());
        assertNotNull(event.getOccurredAt());
        assertEquals("test-data", event.getData());
        assertTrue(event.getMetadata().isEmpty());
        assertEquals("TestEvent", event.getEventType());

        // Verify timestamp is recent
        LocalDateTime now = LocalDateTime.now();
        long secondsDiff = ChronoUnit.SECONDS.between(event.getOccurredAt(), now);
        assertTrue(secondsDiff < 5, "Event timestamp should be within 5 seconds of now");
    }

    @Test
    void constructor_WithMetadata_ShouldInitializeCorrectly() {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("key1", "value1");
        metadata.put("key2", 123);

        TestEvent event = new TestEvent("test-data", metadata);

        assertEquals(2, event.getMetadata().size());
        assertEquals("value1", event.getMetadata().get("key1"));
        assertEquals(123, event.getMetadata().get("key2"));
    }

    @Test
    void constructor_WithAllParameters_ShouldUseProvidedValues() {
        UUID id = UUID.randomUUID();
        LocalDateTime timestamp = LocalDateTime.now().minusHours(1);
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("key", "value");

        TestEvent event = new TestEvent(id, timestamp, metadata, "test-data");

        assertEquals(id, event.getEventId());
        assertEquals(timestamp, event.getOccurredAt());
        assertEquals(1, event.getMetadata().size());
        assertEquals("value", event.getMetadata().get("key"));
        assertEquals("test-data", event.getData());
    }

    @Test
    void constructor_WithNullMetadata_ShouldThrowNullPointerException() {
        assertThrows(NullPointerException.class,
                () -> new TestEvent(UUID.randomUUID(), LocalDateTime.now(), null, "test-data"));
    }

    @Test
    void constructor_WithNullId_ShouldThrowNullPointerException() {
        assertThrows(NullPointerException.class,
                () -> new TestEvent(null, LocalDateTime.now(), new HashMap<>(), "test-data"));
    }

    @Test
    void constructor_WithNullTimestamp_ShouldThrowNullPointerException() {
        assertThrows(NullPointerException.class,
                () -> new TestEvent(UUID.randomUUID(), null, new HashMap<>(), "test-data"));
    }

    @Test
    void metadata_ShouldBeImmutable() {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("key", "value");
        TestEvent event = new TestEvent("test-data", metadata);

        assertThrows(UnsupportedOperationException.class,
                () -> event.getMetadata().put("newKey", "newValue"));
    }

    @Test
    void equals_SameEvent_ShouldBeEqual() {
        TestEvent event = new TestEvent("test-data");
        assertEquals(event, event);
    }

    @Test
    void equals_EventsWithSameId_ShouldBeEqual() {
        UUID id = UUID.randomUUID();
        LocalDateTime time1 = LocalDateTime.now().minusHours(2);
        LocalDateTime time2 = LocalDateTime.now().minusHours(1);
        Map<String, Object> metadata1 = new HashMap<>();
        Map<String, Object> metadata2 = new HashMap<>();
        metadata1.put("key1", "value1");
        metadata2.put("key2", "value2");

        TestEvent event1 = new TestEvent(id, time1, metadata1, "data1");
        TestEvent event2 = new TestEvent(id, time2, metadata2, "data2");

        assertEquals(event1, event2);
        assertEquals(event1.hashCode(), event2.hashCode());
    }

    @Test
    void equals_EventsWithDifferentIds_ShouldNotBeEqual() {
        TestEvent event1 = new TestEvent("data1");
        TestEvent event2 = new TestEvent("data1");

        assertNotEquals(event1, event2);
        assertNotEquals(event1.hashCode(), event2.hashCode());
    }

    @Test
    void equals_DifferentTypes_ShouldNotBeEqual() {
        TestEvent testEvent = new TestEvent("test-data");
        OtherTestEvent otherEvent = new OtherTestEvent();

        assertNotEquals(testEvent, otherEvent);
    }

    @Test
    void equals_WithNull_ShouldNotBeEqual() {
        TestEvent event = new TestEvent("test-data");
        assertNotEquals(event, null);
    }

    @Test
    void eventType_ShouldReturnSimpleClassName() {
        TestEvent testEvent = new TestEvent("test-data");
        OtherTestEvent otherEvent = new OtherTestEvent();

        assertEquals("TestEvent", testEvent.getEventType());
        assertEquals("OtherTestEvent", otherEvent.getEventType());
    }
}