package com.lookbook.user.domain.repositories;

import java.util.Optional;
import java.util.UUID;

import com.lookbook.base.domain.repositories.EntityRepository;
import com.lookbook.user.domain.aggregates.UserProfile;

/**
 * Repository interface for Profile entities.
 */
public interface ProfileRepository extends EntityRepository<UserProfile> {

    /**
     * Finds a profile by user ID.
     *
     * @param userId The user's ID
     * @return The profile if found
     */
    Optional<UserProfile> findByUserId(UUID userId);

    /**
     * Finds a profile by username.
     *
     * @param username The username
     * @return The profile if found
     */
    Optional<UserProfile> findByUsername(String username);
}