package com.lookbook.user.application.ports.services;

import java.util.Optional;
import java.util.UUID;

import com.lookbook.user.domain.aggregates.UserProfile;

/**
 * Service interface for profile operations.
 */
public interface ProfileService {
    /**
     * Creates a new profile for a user.
     *
     * @param userId   The user's ID
     * @param username The username to use as initial display name
     * @return The created profile
     */
    UserProfile createProfile(UUID userId, String username);

    /**
     * Updates a user's profile.
     *
     * @param userId      The user's ID
     * @param displayName The new display name
     * @param biography   The new biography
     * @return The updated profile
     */
    UserProfile updateProfile(UUID userId, String displayName, String biography);

    /**
     * Finds a profile by username.
     *
     * @param username The username
     * @return The profile if found
     */
    Optional<UserProfile> findByUsername(String username);
}