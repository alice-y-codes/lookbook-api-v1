package com.lookbook.user.application.ports.repositories;

import java.util.Optional;
import java.util.UUID;

import com.lookbook.base.application.ports.repositories.EntityRepository;
import com.lookbook.user.domain.aggregates.UserProfile;

/**
 * Repository interface for managing UserProfile entities.
 */
public interface ProfileRepository extends EntityRepository<UserProfile> {

    /**
     * Find a user profile by its ID.
     *
     * @param id the profile ID
     * @return an Optional containing the profile if found
     */
    Optional<UserProfile> findById(UUID id);

    /**
     * Find a user profile by the user's ID.
     *
     * @param userId the user ID
     * @return an Optional containing the profile if found
     */
    Optional<UserProfile> findByUserId(UUID userId);

    /**
     * Save a user profile.
     *
     * @param profile the profile to save
     * @return the saved profile
     */
    UserProfile save(UserProfile profile);

    /**
     * Delete a user profile by its ID.
     *
     * @param id the profile ID
     */
    void deleteById(UUID id);
}