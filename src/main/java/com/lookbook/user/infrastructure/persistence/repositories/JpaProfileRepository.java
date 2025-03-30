package com.lookbook.user.infrastructure.persistence.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lookbook.user.infrastructure.persistence.entities.JpaProfile;

/**
 * Spring Data JPA repository for Profile entities.
 */
@Repository
public interface JpaProfileRepository extends JpaRepository<JpaProfile, UUID> {
    /**
     * Finds a profile by user ID.
     *
     * @param userId The user's ID
     * @return The profile if found
     */
    Optional<JpaProfile> findByUserId(UUID userId);

    /**
     * Finds a profile by username.
     *
     * @param username The username
     * @return The profile if found
     */
    @Query("SELECT p FROM JpaProfile p JOIN JpaUser u ON p.userId = u.id WHERE u.username = :username")
    Optional<JpaProfile> findByUsername(@Param("username") String username);
}