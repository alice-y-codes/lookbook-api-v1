package com.lookbook.auth.domain.valueobjects;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Objects;
import java.util.regex.Pattern;

import com.lookbook.base.domain.validation.ValidationResult;
import com.lookbook.base.domain.valueobjects.BaseValueObject;

/**
 * Password value object that represents a secure password.
 * Enforces password strength rules and handles secure storage.
 */
public class Password extends BaseValueObject<Password> {
    private static final String HASH_ALGORITHM = "SHA-256";
    private static final int SALT_LENGTH = 16;
    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 100;

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

    // These are the fields for storing the password securely
    private final String hashedValue; // The hashed password
    private final String salt; // The salt used for hashing

    // This field is only used temporarily during object creation and validation
    private final transient String rawPassword;

    /**
     * Private constructor for pre-hashed passwords.
     * This constructor is used by the fromHash factory method.
     */
    private Password(String hashedValue, String salt, boolean preHashed) {
        super();
        this.rawPassword = null;
        this.salt = salt;
        this.hashedValue = hashedValue;
        if (!preHashed) {
            validateSelf(); // Only validate for new passwords
        }
    }

    /**
     * Creates a new Password from a plaintext password.
     * Private constructor to enforce factory method usage.
     *
     * @param rawPassword The plaintext password
     * @param salt        The salt to use, or null to generate a new salt
     */
    private Password(String rawPassword, String salt) {
        super(); // Call base constructor
        this.rawPassword = rawPassword;

        // Generate or use provided salt
        this.salt = (salt == null) ? generateSalt() : salt;

        // Hash the password with the salt
        this.hashedValue = (rawPassword != null) ? hashPassword(rawPassword, this.salt) : null;

        validateSelf(); // Validate after initialization
    }

    /**
     * Factory method to create a Password value object from a plaintext password.
     * This should be used when creating a new password.
     *
     * @param rawPassword The plaintext password to hash and store
     * @return A new Password value object
     * @throws ValidationException if the password is invalid
     */
    public static Password create(String rawPassword) {
        return new Password(rawPassword, null);
    }

    /**
     * Factory method to create a Password value object from existing hash and salt.
     * This should be used when reconstructing a password from storage.
     *
     * @param hashedPassword The already-hashed password
     * @param salt           The salt used for hashing
     * @return A new Password value object
     */
    public static Password fromHash(String hashedPassword, String salt) {
        return new Password(hashedPassword, salt, true);
    }

    /**
     * Validates this password according to strength rules.
     * Note: This only validates the raw password during creation.
     *
     * @return ValidationResult with any errors found
     */
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

    /**
     * Checks if the provided plaintext password matches this hashed password.
     *
     * @param plainTextPassword The plaintext password to check
     * @return true if the password matches, false otherwise
     */
    public boolean matches(String plainTextPassword) {
        if (plainTextPassword == null || plainTextPassword.isEmpty()) {
            return false;
        }

        String candidateHash = hashPassword(plainTextPassword, this.salt);
        return MessageDigest.isEqual(
                candidateHash.getBytes(StandardCharsets.UTF_8),
                this.hashedValue.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Gets the hashed password value.
     *
     * @return The hashed password
     */
    public String getHashedValue() {
        return hashedValue;
    }

    /**
     * Gets the salt used for hashing.
     *
     * @return The salt
     */
    public String getSalt() {
        return salt;
    }

    /**
     * Compares this password with another for value equality.
     * Two passwords are equal if they have the same hash and salt.
     *
     * @param other The object to compare with
     * @return true if the passwords have the same value
     */
    @Override
    protected boolean valueEquals(Object other) {
        Password password = (Password) other;
        return Objects.equals(hashedValue, password.hashedValue) &&
                Objects.equals(salt, password.salt);
    }

    /**
     * Generates a hash code based on the password value.
     *
     * @return A hash code derived from the hashed password and salt
     */
    @Override
    protected int valueHashCode() {
        return Objects.hash(hashedValue, salt);
    }

    @Override
    public String toString() {
        // Never reveal the actual password hash in toString
        return "Password[PROTECTED]";
    }

    /**
     * Generates a random salt for password hashing.
     *
     * @return A base64-encoded random salt
     */
    private String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * Hashes a password with the provided salt.
     *
     * @param password The plaintext password to hash
     * @param salt     The salt to use for hashing
     * @return The base64-encoded hashed password
     */
    private String hashPassword(String password, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);

            // Add salt to the digest
            digest.update(Base64.getDecoder().decode(salt));

            // Hash the password
            byte[] hashedBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            // Return the base64-encoded hash
            return Base64.getEncoder().encodeToString(hashedBytes);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to hash password: " + e.getMessage(), e);
        }
    }
}