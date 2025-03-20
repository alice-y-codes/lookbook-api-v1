package com.lookbook.base.domain.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.lookbook.base.domain.exceptions.ValidationException;

/**
 * Represents the result of a validation operation.
 * Collects validation errors and provides methods to check validity.
 */
public class ValidationResult {

    private final List<String> errors;

    /**
     * Creates a new validation result with no errors.
     */
    public ValidationResult() {
        this.errors = new ArrayList<>();
    }

    /**
     * Creates a validation result with the given errors.
     *
     * @param errors A list of error messages
     */
    public ValidationResult(List<String> errors) {
        this.errors = new ArrayList<>(errors);
    }

    /**
     * Creates a validation result with a single error.
     *
     * @param errorMessage The error message
     * @return A new validation result with the error
     */
    public static ValidationResult error(String errorMessage) {
        ValidationResult result = new ValidationResult();
        result.addError(errorMessage);
        return result;
    }

    /**
     * Creates a validation result with no errors.
     *
     * @return A new, valid validation result
     */
    public static ValidationResult valid() {
        return new ValidationResult();
    }

    /**
     * Checks if the validation passed (no errors).
     *
     * @return true if valid, false if there are errors
     */
    public boolean isValid() {
        return errors.isEmpty();
    }

    /**
     * Adds an error to the validation result.
     *
     * @param errorMessage The error message to add
     * @return This validation result for method chaining
     */
    public ValidationResult addError(String errorMessage) {
        errors.add(errorMessage);
        return this;
    }

    /**
     * Adds multiple errors to the validation result.
     *
     * @param errorMessages The error messages to add
     * @return This validation result for method chaining
     */
    public ValidationResult addErrors(List<String> errorMessages) {
        errors.addAll(errorMessages);
        return this;
    }

    /**
     * Combines this validation result with another one.
     *
     * @param other The other validation result to combine with
     * @return A new validation result with all errors from both
     */
    public ValidationResult combine(ValidationResult other) {
        ValidationResult result = new ValidationResult();
        result.addErrors(this.errors);
        result.addErrors(other.errors);
        return result;
    }

    /**
     * Gets all validation errors.
     *
     * @return An unmodifiable list of error messages
     */
    public List<String> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    /**
     * Throws a ValidationException if this result has errors.
     *
     * @throws ValidationException if the validation failed
     */
    public void throwIfInvalid() {
        if (!isValid()) {
            throw new ValidationException(errors);
        }
    }
}