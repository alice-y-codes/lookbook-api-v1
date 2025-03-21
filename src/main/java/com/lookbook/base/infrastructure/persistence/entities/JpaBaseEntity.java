package com.lookbook.base.infrastructure.persistence.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import com.lookbook.base.domain.entities.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

/**
 * Base class for JPA entities that maps the domain BaseEntity properties.
 * This class handles the persistence concerns while keeping the domain model
 * clean.
 */
@MappedSuperclass
public abstract class JpaBaseEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Default constructor for JPA.
     */
    protected JpaBaseEntity() {
        // Required by JPA
    }

    /**
     * Creates a new JPA entity from a domain entity.
     *
     * @param entity The domain entity to map
     */
    protected JpaBaseEntity(BaseEntity entity) {
        this.id = entity.getId();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }

    /**
     * Sets creation and update timestamps before persisting.
     */
    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID();
        }
        LocalDateTime now = LocalDateTime.now();
        if (createdAt == null) {
            createdAt = now;
        }
        updatedAt = now;
    }

    /**
     * Updates the updated_at timestamp before updating.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * Gets the entity ID.
     *
     * @return The entity ID
     */
    public UUID getId() {
        return id;
    }

    /**
     * Gets the creation timestamp.
     *
     * @return The creation timestamp
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Gets the last update timestamp.
     *
     * @return The last update timestamp
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Sets the entity ID.
     *
     * @param id The entity ID
     */
    protected void setId(UUID id) {
        this.id = id;
    }

    /**
     * Sets the creation timestamp.
     *
     * @param createdAt The creation timestamp
     */
    protected void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Sets the last update timestamp.
     *
     * @param updatedAt The last update timestamp
     */
    protected void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}