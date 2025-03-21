package com.lookbook.user.domain.valueobjects;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import com.lookbook.base.domain.validation.ValidationResult;
import com.lookbook.base.domain.valueobjects.BaseValueObject;

/**
 * Email value object that represents a valid email address.
 * Enforces format validation and normalization.
 */
public class Email extends BaseValueObject<Email> {
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private static final String ERROR_EMPTY = "Email cannot be empty";
    private static final String ERROR_TOO_LONG = "Email is too long (maximum is 255 characters)";
    private static final String ERROR_INVALID_FORMAT = "Email format is invalid";
    private static final List<String> BLOCKED_DOMAINS = Arrays.asList(
            "tempmail.com", "disposable.com");

    private final String value;

    /**
     * Creates a new Email from a string value.
     * Private constructor to enforce factory method usage.
     *
     * @param value The email address (will be trimmed and converted to lowercase)
     */
    private Email(String value) {
        super(); // Call base constructor
        this.value = value != null ? value.toLowerCase().trim() : null;
        validateSelf(); // Validate after initialization
    }

    /**
     * Factory method to create an Email value object.
     *
     * @param email The email string to create from
     * @return A new Email value object
     * @throws ValidationException if the email is invalid
     */
    public static Email of(String email) {
        return new Email(email);
    }

    /**
     * Gets the email address value.
     *
     * @return The normalized email address
     */
    public String getValue() {
        return value;
    }

    /**
     * Validates this email address according to format rules.
     *
     * @return ValidationResult with any errors found
     */
    @Override
    public ValidationResult validate() {
        ValidationResult result = ValidationResult.valid();

        if (value == null || value.isEmpty()) {
            return result.addError(ERROR_EMPTY);
        }

        if (value.length() > 255) {
            result = result.addError(ERROR_TOO_LONG);
        }

        if (!pattern.matcher(value).matches()) {
            result = result.addError(ERROR_INVALID_FORMAT);
        }

        String domain = extractDomain(value);
        if (BLOCKED_DOMAINS.contains(domain)) {
            result = result.addError("Email domain is not allowed");
        }

        return result;
    }

    /**
     * Compares this email with another for value equality.
     *
     * @param other The object to compare with
     * @return true if the emails have the same value
     */
    @Override
    protected boolean valueEquals(Object other) {
        Email email = (Email) other;
        return Objects.equals(value, email.value);
    }

    /**
     * Generates a hash code based on the email value.
     *
     * @return A hash code derived from the email value
     */
    @Override
    protected int valueHashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }

    private String extractDomain(String email) {
        return email.substring(email.lastIndexOf('@') + 1);
    }
}
