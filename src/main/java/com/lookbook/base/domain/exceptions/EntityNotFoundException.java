package com.lookbook.base.domain.exceptions;

import java.util.UUID;

/**
 * Exception thrown when an entity cannot be found by its ID.
 * Used to signal lookup failures for domain entities.
 */
public class EntityNotFoundException extends DomainException {

    private final Class<?> entityType;
    private final Object entityId;

    /**
     * Creates a new entity not found exception.
     *
     * @param entityType The type of entity that could not be found
     * @param entityId   The ID that was used for the lookup
     */
    public EntityNotFoundException(Class<?> entityType, Object entityId) {
        super("ENT_NOT_FOUND",
                String.format("%s with ID %s not found",
                        entityType.getSimpleName(),
                        entityId.toString()));
        this.entityType = entityType;
        this.entityId = entityId;
    }

    /**
     * Creates a new entity not found exception with a UUID ID.
     *
     * @param entityType The type of entity that could not be found
     * @param entityId   The UUID that was used for the lookup
     */
    public EntityNotFoundException(Class<?> entityType, UUID entityId) {
        this(entityType, (Object) entityId);
    }

    /**
     * Creates a new entity not found exception with a custom message.
     *
     * @param entityType The type of entity that could not be found
     * @param entityId   The ID that was used for the lookup
     * @param message    A custom error message
     */
    public EntityNotFoundException(Class<?> entityType, Object entityId, String message) {
        super("ENT_NOT_FOUND", message);
        this.entityType = entityType;
        this.entityId = entityId;
    }

    /**
     * Gets the type of entity that could not be found.
     *
     * @return The entity class
     */
    public Class<?> getEntityType() {
        return entityType;
    }

    /**
     * Gets the ID that was used for the lookup.
     *
     * @return The entity ID
     */
    public Object getEntityId() {
        return entityId;
    }
}