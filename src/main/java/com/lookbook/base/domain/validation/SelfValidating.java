package com.lookbook.base.domain.validation;

/**
 * Interface for domain objects that validate themselves.
 * Typically implemented by value objects to enforce their invariants.
 *
 * @param <T> The type of the implementing class
 */
public interface SelfValidating<T> {

    /**
     * Validates this object and returns a validation result.
     *
     * @return A ValidationResult indicating whether the object is valid
     */
    ValidationResult validate();

    /**
     * Validates this object and throws an exception if invalid.
     *
     * @throws com.lookbook.base.domain.exceptions.ValidationException if the object
     *                                                                 is invalid
     */
    default void validateAndThrow() {
        ValidationResult result = validate();
        result.throwIfInvalid();
    }
}