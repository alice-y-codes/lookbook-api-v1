package com.lookbook.auth.domain.valueobjects;

import java.util.Objects;
import java.util.regex.Pattern;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.lookbook.base.domain.validation.ValidationResult;
import com.lookbook.base.domain.valueobjects.BaseValueObject;

/**
 * Password value object that represents a secure password.
 * Enforces password strength rules and handles secure storage using BCrypt.
 */
public class Password extends BaseValueObject<Password> {
    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 100;
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // Regex patterns for password validation
    private static final String HAS_LOWERCASE = ".*[a-z].*";
    private static final String HAS_UPPERCASE = ".*[A-Z].*";
    private static final String HAS_DIGIT = ".*\\d.*";
    private static final String HAS_SPECIAL = ".*[!@#$%^&*()\\-_=+\\[\\]{};:,.<>/?].*";

    private static final Pattern LOWERCASE_PATTERN = Pattern.compile(HAS_LOWERCASE);
    private static final Pattern UPPERCASE_PATTERN = Pattern.compile(HAS_UPPERCASE);
    private static final Pattern DIGIT_PATTERN = Pattern.compile(HAS_DIGIT);
    private static final Pattern SPECIAL_PATTERN = Pattern.compile(HAS_SPECIAL);

    private static final String ERROR_EMPTY = "Password cannot be empty";
    private static final String ERROR_TOO_SHORT = "Password is too short (minimum is " + MIN_LENGTH + " characters)";
    private static final String ERROR_TOO_LONG = "Password is too long (maximum is " + MAX_LENGTH + " characters)";
    private static final String ERROR_NO_LOWERCASE = "Password must contain at least one lowercase letter";
    private static final String ERROR_NO_UPPERCASE = "Password must contain at least one uppercase letter";
    private static final String ERROR_NO_DIGIT = "Password must contain at least one digit";
    private static final String ERROR_NO_SPECIAL = "Password must contain at least one special character";

    private final String hashedValue;
    private final transient String rawPassword;

    private Password(String hashedValue, boolean preHashed) {
        super();
        this.rawPassword = null;
        this.hashedValue = hashedValue;
        if (!preHashed) {
            validateSelf();
        }
    }

    private Password(String rawPassword) {
        super();
        this.rawPassword = rawPassword;
        this.hashedValue = encoder.encode(rawPassword);
        validateSelf();
    }

    public static Password create(String rawPassword) {
        return new Password(rawPassword);
    }

    public static Password fromHash(String hashedPassword) {
        return new Password(hashedPassword, true);
    }

    @Override
    public ValidationResult validate() {
        ValidationResult result = ValidationResult.valid();

        if (rawPassword == null || rawPassword.isEmpty()) {
            return result.addError(ERROR_EMPTY);
        }

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
        return encoder.matches(plainTextPassword, this.hashedValue);
    }

    public String getHashedValue() {
        return hashedValue;
    }

    @Override
    protected boolean valueEquals(Object other) {
        Password password = (Password) other;
        return Objects.equals(hashedValue, password.hashedValue);
    }

    @Override
    protected int valueHashCode() {
        return Objects.hash(hashedValue);
    }

    @Override
    public String toString() {
        return "Password[PROTECTED]";
    }
}