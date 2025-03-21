package com.lookbook.user.infrastructure.persistence.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lookbook.user.infrastructure.persistence.entities.JpaUser;

/**
 * Spring Data JPA repository for User entities.
 */
@Repository
public interface UserJpaRepository extends JpaRepository<JpaUser, UUID> {

    /**
     * Finds a user by their username.
     *
     * @param username The username to search for
     * @return An Optional containing the user if found, empty otherwise
     */
    Optional<JpaUser> findByUsername(String username);

    /**
     * Finds a user by their email.
     *
     * @param email The email to search for
     * @return An Optional containing the user if found, empty otherwise
     */
    Optional<JpaUser> findByEmail(String email);

    /**
     * Checks if a username exists.
     *
     * @param username The username to check
     * @return true if the username exists, false otherwise
     */
    boolean existsByUsername(String username);

    /**
     * Checks if an email exists.
     *
     * @param email The email to check
     * @return true if the email exists, false otherwise
     */
    boolean existsByEmail(String email);
}