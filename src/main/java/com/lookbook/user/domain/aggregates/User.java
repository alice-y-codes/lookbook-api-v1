package com.lookbook.user.domain.aggregates;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import com.lookbook.auth.domain.valueobjects.Password;
import com.lookbook.base.domain.entities.BaseEntity;
import com.lookbook.base.domain.exceptions.ValidationException;
import com.lookbook.user.domain.events.PasswordChangedEvent;
import com.lookbook.user.domain.events.UserActivatedEvent;
import com.lookbook.user.domain.events.UserDeactivatedEvent;
import com.lookbook.user.domain.events.UserRegisteredEvent;
import com.lookbook.user.domain.valueobjects.Email;
import com.lookbook.user.domain.valueobjects.Username;

/**
 * User aggregate root entity.
 * Represents a user in the system with their authentication and profile
 * information.
 */
public class User extends BaseEntity {
    private final Username username;
    private Email email;
    private Password password;
    private UserStatus status;

    /**
     * Creates a new user.
     *
     * @param username The user's unique username
     * @param email    The user's email address
     * @param password The user's password (already hashed)
     */
    private User(Username username, Email email, Password password) {
        super();
        this.username = Objects.requireNonNull(username, "Username cannot be null");
        this.email = Objects.requireNonNull(email, "Email cannot be null");
        this.password = Objects.requireNonNull(password, "Password cannot be null");
        this.status = UserStatus.PENDING;
    }

    /**
     * Creates a user with specific ID and timestamps (for reconstruction from
     * persistence).
     *
     * @param id        The user's ID
     * @param username  The user's username
     * @param email     The user's email
     * @param password  The user's password
     * @param status    The user's status
     * @param createdAt When the user was created
     * @param updatedAt When the user was last updated
     */
    private User(UUID id, Username username, Email email, Password password,
            UserStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(id, createdAt, updatedAt);
        this.username = Objects.requireNonNull(username, "Username cannot be null");
        this.email = Objects.requireNonNull(email, "Email cannot be null");
        this.password = Objects.requireNonNull(password, "Password cannot be null");
        this.status = Objects.requireNonNull(status, "Status cannot be null");
    }

    /**
     * Factory method to register a new user.
     *
     * @param username      The username as string
     * @param email         The email as string
     * @param plainPassword The password as plain text
     * @return A new User entity
     * @throws ValidationException if any parameter is null or invalid
     */
    public static User register(String username, String email, String plainPassword) {
        // Check for null inputs and throw ValidationException
        if (username == null) {
            throw new ValidationException("Username cannot be null");
        }
        if (email == null) {
            throw new ValidationException("Email cannot be null");
        }
        if (plainPassword == null) {
            throw new ValidationException("Password cannot be null");
        }

        // Convert inputs to value objects, which will validate them
        Username usernameVO = Username.of(username);
        Email emailVO = Email.of(email);
        Password passwordVO = Password.create(plainPassword);

        User user = new User(usernameVO, emailVO, passwordVO);

        // Add domain event
        user.addDomainEvent(new UserRegisteredEvent(
                user.getId(),
                user.getUsername().getValue(),
                user.getEmail().getValue()));

        return user;
    }

    /**
     * Factory method to reconstruct a user from persistence.
     *
     * @param id             The user's ID
     * @param username       The username value object
     * @param email          The email value object
     * @param hashedPassword The hashed password
     * @param salt           The salt used for password hashing
     * @param status         The user's status
     * @param createdAt      When the user was created
     * @param updatedAt      When the user was last updated
     * @return A reconstructed User entity
     */
    public static User reconstitute(UUID id, Username username, Email email,
            String hashedPassword, String salt, UserStatus status,
            LocalDateTime createdAt, LocalDateTime updatedAt) {

        Password password = Password.fromHash(hashedPassword, salt);

        return new User(id, username, email, password, status, createdAt, updatedAt);
    }

    /**
     * Activates the user.
     * Valid only if the user is in PENDING or INACTIVE status.
     */
    public void activate() {
        if (status != UserStatus.ACTIVE) {
            status = status.transitionTo(UserStatus.ACTIVE);
            markUpdated();

            // Add domain event
            addDomainEvent(new UserActivatedEvent(getId(), getUsername().getValue()));
        }
    }

    /**
     * Deactivates the user.
     * Valid only if the user is in PENDING or ACTIVE status.
     */
    public void deactivate() {
        if (status != UserStatus.INACTIVE) {
            status = status.transitionTo(UserStatus.INACTIVE);
            markUpdated();

            // Add domain event
            addDomainEvent(new UserDeactivatedEvent(getId(), getUsername().getValue()));
        }
    }

    /**
     * Changes the user's password.
     *
     * @param currentPassword The current password
     * @param newPassword     The new password
     * @throws ValidationException if current password is incorrect
     */
    public void changePassword(String currentPassword, String newPassword) {
        if (!checkPassword(currentPassword)) {
            throw new ValidationException("Current password is incorrect");
        }

        this.password = Password.create(newPassword);
        markUpdated();

        // Add domain event
        addDomainEvent(new PasswordChangedEvent(getId(), getUsername().getValue()));
    }

    /**
     * Checks if the provided password matches the user's password.
     *
     * @param plainPassword The password to check
     * @return true if the password matches
     */
    public boolean checkPassword(String plainPassword) {
        return password.matches(plainPassword);
    }

    /**
     * Updates the user's email.
     *
     * @param newEmail The new email
     */
    public void updateEmail(String newEmail) {
        this.email = Email.of(newEmail);
        markUpdated();
    }

    // Getters

    public Username getUsername() {
        return username;
    }

    public Email getEmail() {
        return email;
    }

    public UserStatus getStatus() {
        return status;
    }

    /**
     * Gets the hashed password value.
     * This is primarily for persistence.
     *
     * @return The hashed password
     */
    public String getHashedPassword() {
        return password.getHashedValue();
    }

    /**
     * Gets the password salt.
     * This is primarily for persistence.
     *
     * @return The password salt
     */
    public String getPasswordSalt() {
        return password.getSalt();
    }
}