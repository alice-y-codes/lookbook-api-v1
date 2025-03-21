package com.lookbook.user.application.ports.repositories;

import java.util.Optional;

import com.lookbook.base.application.ports.repositories.EntityRepository;
import com.lookbook.user.domain.aggregates.User;
import com.lookbook.user.domain.valueobjects.Email;
import com.lookbook.user.domain.valueobjects.Username;

/**
 * Repository interface for User domain entities.
 * Extends EntityRepository to inherit standard CRUD and time-based operations.
 */
public interface UserRepository extends EntityRepository<User> {

    /**
     * Finds a user by their username.
     *
     * @param username The username to search for
     * @return An Optional containing the user if found, empty otherwise
     */
    Optional<User> findByUsername(Username username);

    /**
     * Finds a user by their email address.
     *
     * @param email The email to search for
     * @return An Optional containing the user if found, empty otherwise
     */
    Optional<User> findByEmail(Email email);

    /**
     * Checks if a username is already taken.
     *
     * @param username The username to check
     * @return true if the username exists, false otherwise
     */
    boolean existsByUsername(Username username);

    /**
     * Checks if an email is already registered.
     *
     * @param email The email to check
     * @return true if the email exists, false otherwise
     */
    boolean existsByEmail(Email email);
}
