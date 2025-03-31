package com.lookbook.base.domain.events;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * Interface for all domain events.
 * Domain events represent something significant that happened in the domain.
 */
public interface DomainEvent {
    /**
     * Gets the unique identifier of the event.
     *
     * @return The event's UUID
     */
    UUID getEventId();

    /**
     * Gets when the event occurred.
     *
     * @return The timestamp of the event
     */
    LocalDateTime getOccurredAt();

    /**
     * Gets the ID of the aggregate root that generated this event.
     *
     * @return The aggregate root's UUID
     */
    UUID getAggregateId();

    /**
     * Gets the type of event.
     *
     * @return A string representing the event type
     */
    String getEventType();

    /**
     * Gets any additional metadata for the event.
     *
     * @return A map of metadata key-value pairs
     */
    Map<String, Object> getMetadata();
}