package com.lookbook.user.domain.valueobjects;

import com.lookbook.base.domain.exceptions.ValidationException;
import com.lookbook.base.domain.validation.ValidationResult;
import com.lookbook.base.domain.valueobjects.BaseValueObject;

/**
 * Value object representing a user's display name.
 * Must be between 2 and 50 characters long and contain only letters, numbers,
 * spaces, and basic punctuation.
 */
public class DisplayName extends BaseValueObject<DisplayName> {
    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 50;
    private static final String VALID_PATTERN = "^[a-zA-Z0-9\\s.,!?-]+$";

    private final String value;

    private DisplayName(String value) {
        super();
        this.value = value;
        validateSelf();
    }

    /**
     * Creates a new DisplayName from a string value.
     *
     * @param value the display name string
     * @return a new DisplayName instance
     * @throws ValidationException if the value is invalid
     */
    public static DisplayName of(String value) {
        return new DisplayName(value);
    }

    @Override
    public ValidationResult validate() {
        ValidationResult result = ValidationResult.valid();

        if (value == null || value.trim().isEmpty()) {
            return result.addError("Display name cannot be empty");
        }

        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
            result = result.addError(
                    String.format("Display name must be between %d and %d characters", MIN_LENGTH, MAX_LENGTH));
        }

        if (!value.matches(VALID_PATTERN)) {
            result = result.addError(
                    "Display name can only contain letters, numbers, spaces, and basic punctuation");
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
        DisplayName that = (DisplayName) o;
        return value.equals(that.value);
    }

    @Override
    protected int valueHashCode() {
        return value.hashCode();
    }
}