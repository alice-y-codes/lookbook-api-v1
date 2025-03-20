package com.lookbook.base.infrastructure.persistence.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import com.lookbook.base.domain.entities.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

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

    protected JpaBaseEntity() {
        // Required by JPA
    }

    protected JpaBaseEntity(BaseEntity entity) {
        this.id = entity.getId();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }

    public UUID getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    protected void setId(UUID id) {
        this.id = id;
    }

    protected void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    protected void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}