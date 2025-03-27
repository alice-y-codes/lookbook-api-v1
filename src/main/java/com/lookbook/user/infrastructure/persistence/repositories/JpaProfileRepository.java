package com.lookbook.user.infrastructure.persistence.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
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
}