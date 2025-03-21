package com.lookbook.user.application.ports.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.lookbook.user.domain.aggregates.User;

/**
 * Service interface for user operations.
 */
public interface UserService {

    /**
     * Find a user by their ID.
     *
     * @param id the user ID
     * @return the user if found
     */
    Optional<User> findById(UUID id);

    /**
     * Find a user by their username.
     *
     * @param username the username
     * @return the user if found
     */
    Optional<User> findByUsername(String username);

    /**
     * Find a user by their email.
     *
     * @param email the email
     * @return the user if found
     */
    Optional<User> findByEmail(String email);

    /**
     * Get all users.
     *
     * @return a list of all users
     */
    List<User> findAll();

    /**
     * Activate a user.
     *
     * @param id the user ID
     * @return the updated user
     */
    User activate(UUID id);

    /**
     * Deactivate a user.
     *
     * @param id the user ID
     * @return the updated user
     */
    User deactivate(UUID id);

    /**
     * Update a user's email.
     *
     * @param id       the user ID
     * @param newEmail the new email
     * @return the updated user
     */
    User updateEmail(UUID id, String newEmail);

    /**
     * Change a user's password.
     *
     * @param id              the user ID
     * @param currentPassword the current password
     * @param newPassword     the new password
     * @return the updated user
     */
    User changePassword(UUID id, String currentPassword, String newPassword);
}