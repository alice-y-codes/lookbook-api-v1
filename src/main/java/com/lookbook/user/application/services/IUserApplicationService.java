package com.lookbook.user.application.services;

import java.util.UUID;

import com.lookbook.base.domain.exceptions.ValidationException;
import com.lookbook.user.domain.aggregates.User;

/**
 * Interface defining the contract for user-related application operations.
 */
public interface IUserApplicationService {
    /**
     * Registers a new user and handles the resulting domain events.
     *
     * @param username The username
     * @param email    The email
     * @param password The password
     * @return The created user
     * @throws ValidationException if validation fails
     */
    User registerUser(String username, String email, String password);

    /**
     * Activates a user and handles the resulting domain events.
     *
     * @param userId The user's ID
     * @return The activated user
     */
    User activateUser(UUID userId);

    /**
     * Deactivates a user and handles the resulting domain events.
     *
     * @param userId The user's ID
     * @return The deactivated user
     */
    User deactivateUser(UUID userId);

    /**
     * Changes a user's password and handles the resulting domain events.
     *
     * @param userId          The user's ID
     * @param currentPassword The current password
     * @param newPassword     The new password
     * @return The updated user
     */
    User changePassword(UUID userId, String currentPassword, String newPassword);
}