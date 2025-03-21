package com.lookbook.user.domain.valueobjects;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import com.lookbook.base.domain.validation.ValidationResult;
import com.lookbook.base.domain.valueobjects.BaseValueObject;

/**
 * Username value object that represents a valid username.
 * Enforces format validation and normalization rules.
 */
public class Username extends BaseValueObject<Username> {
    // Username pattern: 3-20 characters, alphanumeric, underscores and hyphens
    // allowed
    private static final String USERNAME_PATTERN = "^[a-zA-Z0-9_-]{3,20}$";
    private static final Pattern pattern = Pattern.compile(USERNAME_PATTERN);

    private static final String ERROR_EMPTY = "Username cannot be empty";
    private static final String ERROR_TOO_SHORT = "Username is too short (minimum is 3 characters)";
    private static final String ERROR_TOO_LONG = "Username is too long (maximum is 20 characters)";
    private static final String ERROR_INVALID_FORMAT = "Username format is invalid (only letters, numbers, underscores and hyphens are allowed)";

    private static final List<String> RESERVED_USERNAMES = Arrays.asList(
            "admin", "administrator", "system", "user", "moderator", "support", "help");

    private final String value;

    /**
     * Creates a new Username from a string value.
     * Private constructor to enforce factory method usage.
     *
     * @param value The username (will be trimmed)
     */
    private Username(String value) {
        super(); // Call base constructor
        this.value = value != null ? value.trim() : null;
        validateSelf(); // Validate after initialization
    }

    /**
     * Factory method to create a Username value object.
     *
     * @param username The username string to create from
     * @return A new Username value object
     * @throws ValidationException if the username is invalid
     */
    public static Username of(String username) {
        return new Username(username);
    }

    /**
     * Gets the username value.
     *
     * @return The normalized username
     */
    public String getValue() {
        return value;
    }

    /**
     * Validates this username according to format rules.
     *
     * @return ValidationResult with any errors found
     */
    @Override
    public ValidationResult validate() {
        ValidationResult result = ValidationResult.valid();

        if (value == null || value.isEmpty()) {
            return result.addError(ERROR_EMPTY);
        }

        if (value.length() < 3) {
            result = result.addError(ERROR_TOO_SHORT);
        }

        if (value.length() > 20) {
            result = result.addError(ERROR_TOO_LONG);
        }

        if (!pattern.matcher(value).matches()) {
            result = result.addError(ERROR_INVALID_FORMAT);
        }

        if (RESERVED_USERNAMES.contains(value.toLowerCase())) {
            result = result.addError("Username is reserved and cannot be used");
        }

        return result;
    }

    /**
     * Compares this username with another for value equality.
     *
     * @param other The object to compare with
     * @return true if the usernames have the same value
     */
    @Override
    protected boolean valueEquals(Object other) {
        Username username = (Username) other;
        return Objects.equals(value, username.value);
    }

    /**
     * Generates a hash code based on the username value.
     *
     * @return A hash code derived from the username value
     */
    @Override
    protected int valueHashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}