package com.lookbook.base.domain.events;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Base implementation for domain events.
 * Provides common functionality and enforces immutability.
 */
public abstract class BaseDomainEvent implements DomainEvent {

    private final UUID eventId;
    private final LocalDateTime occurredAt;
    private final Map<String, Object> metadata;

    /**
     * Creates a new domain event with the current timestamp and empty metadata.
     */
    protected BaseDomainEvent() {
        this(new HashMap<>());
    }

    /**
     * Creates a new domain event with the current timestamp and the specified
     * metadata.
     *
     * @param metadata Additional contextual information about the event
     */
    protected BaseDomainEvent(Map<String, Object> metadata) {
        this.eventId = UUID.randomUUID();
        this.occurredAt = LocalDateTime.now();
        this.metadata = Collections.unmodifiableMap(new HashMap<>(
                Objects.requireNonNull(metadata, "Event metadata cannot be null")));
    }

    /**
     * Creates a domain event with specific ID, timestamp, and metadata.
     * Used primarily for reconstruction from persistence.
     *
     * @param eventId    The event's unique identifier
     * @param occurredAt When the event occurred
     * @param metadata   Additional contextual information about the event
     */
    protected BaseDomainEvent(UUID eventId, LocalDateTime occurredAt, Map<String, Object> metadata) {
        this.eventId = Objects.requireNonNull(eventId, "Event ID cannot be null");
        this.occurredAt = Objects.requireNonNull(occurredAt, "Occurred timestamp cannot be null");
        this.metadata = Collections.unmodifiableMap(new HashMap<>(
                Objects.requireNonNull(metadata, "Event metadata cannot be null")));
    }

    @Override
    public UUID getEventId() {
        return eventId;
    }

    @Override
    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }

    @Override
    public String getEventType() {
        return this.getClass().getSimpleName();
    }

    @Override
    public Map<String, Object> getMetadata() {
        return metadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        BaseDomainEvent that = (BaseDomainEvent) o;
        return Objects.equals(eventId, that.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId);
    }
}