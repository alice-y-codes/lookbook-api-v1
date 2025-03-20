package com.lookbook.base.domain.valueobjects;

import com.lookbook.base.domain.validation.SelfValidating;
import com.lookbook.base.domain.validation.ValidationResult;

/**
 * Base class for all value objects.
 * Provides common functionality for value equality and immutability.
 * Value objects should:
 * 1. Be immutable
 * 2. Have value equality (not identity equality)
 * 3. Be self-validating
 *
 * @param <T> The type of the implementing class
 */
public abstract class BaseValueObject<T extends BaseValueObject<T>> implements SelfValidating<T> {

    /**
     * Protected constructor to ensure all value objects are immutable.
     * Note: Subclasses must call validateSelf() after initializing their fields.
     */
    protected BaseValueObject() {
        // Validation is now the responsibility of subclasses
    }

    /**
     * Validates that the value object satisfies its invariants.
     * Each value object should implement this to enforce its own rules.
     * 
     * @return The validation result
     */
    @Override
    public abstract ValidationResult validate();

    /**
     * Validates this object's state.
     * Must be called by subclasses after field initialization.
     */
    protected final void validateSelf() {
        validateAndThrow();
    }

    /**
     * Compares the value equality of this object with another.
     * Must be implemented by subclasses to check field-by-field equality.
     *
     * @param other The other object to compare with
     * @return true if the values are equal, false otherwise
     */
    protected abstract boolean valueEquals(Object other);

    /**
     * Generates a hashCode based on the value fields.
     * Must be implemented by subclasses.
     *
     * @return A hashCode based on value fields
     */
    protected abstract int valueHashCode();

    @Override
    public final boolean equals(Object other) {
        if (this == other)
            return true;
        if (other == null || getClass() != other.getClass())
            return false;
        return valueEquals(other);
    }

    @Override
    public final int hashCode() {
        return valueHashCode();
    }
}