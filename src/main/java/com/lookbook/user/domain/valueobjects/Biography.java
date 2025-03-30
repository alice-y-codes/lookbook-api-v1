package com.lookbook.user.domain.valueobjects;

import com.lookbook.base.domain.exceptions.ValidationException;
import com.lookbook.base.domain.validation.ValidationResult;
import com.lookbook.base.domain.valueobjects.BaseValueObject;

/**
 * Value object representing a user's biography.
 * Must be between 10 and 500 characters long.
 */
public class Biography extends BaseValueObject<Biography> {
    private static final int MIN_LENGTH = 10;
    private static final int MAX_LENGTH = 500;

    private final String value;

    private Biography(String value) {
        super();
        this.value = value;
        validateSelf();
    }

    /**
     * Creates a new Biography from a string value.
     *
     * @param value the biography text
     * @return a new Biography instance
     * @throws ValidationException if the value is invalid
     */
    public static Biography of(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException("Biography cannot be empty");
        }

        String trimmedValue = value.trim();
        if (trimmedValue.length() < MIN_LENGTH || trimmedValue.length() > MAX_LENGTH) {
            throw new ValidationException(
                    String.format("Biography must be between %d and %d characters", MIN_LENGTH, MAX_LENGTH));
        }

        return new Biography(trimmedValue);
    }

    /**
     * Creates an empty biography with a default placeholder text.
     *
     * @return a new Biography instance with placeholder text
     */
    public static Biography empty() {
        return new Biography("Tell us about yourself...");
    }

    @Override
    public ValidationResult validate() {
        ValidationResult result = ValidationResult.valid();

        if (value == null || value.trim().isEmpty()) {
            return result.addError("Biography cannot be empty");
        }

        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
            result = result.addError(
                    String.format("Biography must be between %d and %d characters", MIN_LENGTH, MAX_LENGTH));
        }

        return result;
    }

    public String getValue() {
        return value;
    }

    @Override
    protected boolean valueEquals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Biography that = (Biography) o;
        return value.equals(that.value);
    }

    @Override
    protected int valueHashCode() {
        return value.hashCode();
    }
}