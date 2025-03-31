package com.lookbook.auth.domain.valueobjects;

import java.util.Objects;
import java.util.regex.Pattern;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.lookbook.base.domain.validation.ValidationResult;
import com.lookbook.base.domain.valueobjects.BaseValueObject;

/**
 * Password value object that represents a secure password.
 * Enforces password strength rules and handles secure storage using Spring
 * Security's BCrypt.
 */
public class Password extends BaseValueObject<Password> {
    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 100;
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Regex patterns for password validation
    private static final String HAS_LOWERCASE = ".*[a-z].*";
    private static final String HAS_UPPERCASE = ".*[A-Z].*";
    private static final String HAS_DIGIT = ".*\\d.*";
    private static final String HAS_SPECIAL = ".*[!@#$%^&*()\\-_=+\\[\\]{};:,.<>/?].*";

    private static final Pattern LOWERCASE_PATTERN = Pattern.compile(HAS_LOWERCASE);
    private static final Pattern UPPERCASE_PATTERN = Pattern.compile(HAS_UPPERCASE);
    private static final Pattern DIGIT_PATTERN = Pattern.compile(HAS_DIGIT);
    private static final Pattern SPECIAL_PATTERN = Pattern.compile(HAS_SPECIAL);

    private static final String ERROR_TOO_SHORT = "Password is too short (minimum is " + MIN_LENGTH + " characters)";
    private static final String ERROR_TOO_LONG = "Password is too long (maximum is " + MAX_LENGTH + " characters)";
    private static final String ERROR_NO_LOWERCASE = "Password must contain at least one lowercase letter";
    private static final String ERROR_NO_UPPERCASE = "Password must contain at least one uppercase letter";
    private static final String ERROR_NO_DIGIT = "Password must contain at least one digit";
    private static final String ERROR_NO_SPECIAL = "Password must contain at least one special character";

    // The hashed password (includes salt as part of BCrypt)
    private final String hashedValue;

    // This field is only used temporarily during object creation and validation
    private final transient String rawPassword;

    /**
     * Private constructor for pre-hashed passwords.
     * This constructor is used by the fromHash factory method.
     */
    private Password(String hashedValue, boolean preHashed) {
        super();
        this.rawPassword = null;
        this.hashedValue = hashedValue;
        if (!preHashed) {
            validateSelf();
        }
    }

    /**
     * Creates a new Password from a plaintext password.
     * Private constructor to enforce factory method usage.
     *
     * @param rawPassword The plaintext password
     */
    private Password(String rawPassword) {
        super(); // Call base constructor
        this.rawPassword = rawPassword;
        this.hashedValue = passwordEncoder.encode(rawPassword);
        validateSelf(); // Validate after initialization
    }

    public static Password create(String rawPassword) {
        return new Password(rawPassword);
    }

    /**
     * Factory method to create a Password value object from an existing BCrypt
     * hash.
     * This should be used when reconstructing a password from storage.
     *
     * @param hashedPassword The BCrypt-hashed password
     * @return A new Password value object
     */
    public static Password fromHash(String hashedPassword) {
        return new Password(hashedPassword, true);
    }

    @Override
    public ValidationResult validate() {
        ValidationResult result = ValidationResult.valid();

        if (rawPassword.length() < MIN_LENGTH) {
            result = result.addError(ERROR_TOO_SHORT);
        }

        if (rawPassword.length() > MAX_LENGTH) {
            result = result.addError(ERROR_TOO_LONG);
        }

        if (!LOWERCASE_PATTERN.matcher(rawPassword).matches()) {
            result = result.addError(ERROR_NO_LOWERCASE);
        }

        if (!UPPERCASE_PATTERN.matcher(rawPassword).matches()) {
            result = result.addError(ERROR_NO_UPPERCASE);
        }

        if (!DIGIT_PATTERN.matcher(rawPassword).matches()) {
            result = result.addError(ERROR_NO_DIGIT);
        }

        if (!SPECIAL_PATTERN.matcher(rawPassword).matches()) {
            result = result.addError(ERROR_NO_SPECIAL);
        }

        return result;
    }

    public boolean matches(String plainTextPassword) {
        if (plainTextPassword == null || plainTextPassword.isEmpty()) {
            return false;
        }
        return passwordEncoder.matches(plainTextPassword, this.hashedValue);
    }

    /**
     * Gets the hashed password value.
     *
     * @return The BCrypt-hashed password
     */
    public String getHashedValue() {
        return hashedValue;
    }

    /**
     * Compares this password with another for value equality.
     * Two passwords are equal if they have the same hash.
     *
     * @param other The object to compare with
     * @return true if the passwords have the same value
     */
    @Override
    protected boolean valueEquals(Object other) {
        Password password = (Password) other;
        return Objects.equals(hashedValue, password.hashedValue);
    }

    /**
     * Generates a hash code based on the password value.
     *
     * @return A hash code derived from the hashed password
     */
    @Override
    protected int valueHashCode() {
        return Objects.hash(hashedValue);
    }

    @Override
    public String toString() {
        return "Password[PROTECTED]";
    }
}