package com.lookbook.base.domain.entities;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Base class for all domain entities.
 * Provides common functionality for identity and timestamps.
 */
public abstract class BaseEntity {

    private final UUID id;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Creates a new entity with a generated ID and current timestamps.
     */
    protected BaseEntity() {
        this.id = UUID.randomUUID();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    /**
     * Creates an entity with a specific ID and timestamps.
     * Used primarily for reconstruction from persistence.
     *
     * @param id        The entity's unique identifier
     * @param createdAt When the entity was created
     * @param updatedAt When the entity was last updated
     */
    protected BaseEntity(UUID id, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = Objects.requireNonNull(id, "Entity ID cannot be null");
        this.createdAt = Objects.requireNonNull(createdAt, "Created timestamp cannot be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "Updated timestamp cannot be null");
    }

    /**
     * Gets the entity's unique identifier.
     *
     * @return The entity ID
     */
    public UUID getId() {
        return id;
    }

    /**
     * Gets when the entity was created.
     *
     * @return The creation timestamp
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Gets when the entity was last updated.
     *
     * @return The last update timestamp
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Marks the entity as updated.
     * Should be called whenever the entity state changes.
     */
    protected void markUpdated() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        BaseEntity that = (BaseEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}