package com.lookbook.base.domain.exceptions;

/**
 * Exception thrown when attempting to create or update an entity that would
 * violate
 * a uniqueness constraint.
 */
public class DuplicateEntityException extends DomainException {

    private final Class<?> entityType;
    private final String field;
    private final Object value;

    /**
     * Creates a new duplicate entity exception.
     *
     * @param entityType The type of entity that caused the duplicate
     * @param field      The field that caused the uniqueness violation
     * @param value      The value that already exists
     */
    public DuplicateEntityException(Class<?> entityType, String field, Object value) {
        super("DUP_ENT",
                String.format("%s with %s '%s' already exists",
                        entityType.getSimpleName(),
                        field,
                        value.toString()));
        this.entityType = entityType;
        this.field = field;
        this.value = value;
    }

    /**
     * Creates a new duplicate entity exception with a custom message.
     *
     * @param entityType The type of entity that caused the duplicate
     * @param field      The field that caused the uniqueness violation
     * @param value      The value that already exists
     * @param message    A custom error message
     */
    public DuplicateEntityException(Class<?> entityType, String field, Object value, String message) {
        super("DUP_ENT", message);
        this.entityType = entityType;
        this.field = field;
        this.value = value;
    }

    /**
     * Gets the type of entity that caused the duplicate.
     *
     * @return The entity class
     */
    public Class<?> getEntityType() {
        return entityType;
    }

    /**
     * Gets the field that caused the uniqueness violation.
     *
     * @return The field name
     */
    public String getField() {
        return field;
    }

    /**
     * Gets the value that already exists.
     *
     * @return The duplicate value
     */
    public Object getValue() {
        return value;
    }
}