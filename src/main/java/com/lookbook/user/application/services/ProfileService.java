package com.lookbook.user.application.services;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lookbook.base.domain.exceptions.ValidationException;
import com.lookbook.user.domain.aggregates.UserProfile;
import com.lookbook.user.domain.repositories.ProfileRepository;
import com.lookbook.user.domain.valueobjects.Biography;
import com.lookbook.user.domain.valueobjects.DisplayName;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Application service for handling profile-related operations.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;

    /**
     * Creates a new profile for a user.
     *
     * @param userId   The user's ID
     * @param username The username to use as initial display name
     * @return The created profile
     */
    public UserProfile createProfile(UUID userId, String username) {
        try {
            log.info("Creating profile for user: {}", userId);

            // Create profile with username as initial display name
            DisplayName displayName = DisplayName.of(username);
            Biography biography = Biography.empty();
            LocalDateTime now = LocalDateTime.now();

            UserProfile profile = UserProfile.create(userId, displayName, biography, now);

            // Save profile
            profile = profileRepository.save(profile);

            log.info("Profile created successfully for user: {}", userId);
            return profile;
        } catch (Exception e) {
            log.error("Failed to create profile for user: {}", userId, e);
            throw e;
        }
    }

    /**
     * Updates a user's profile.
     *
     * @param userId      The user's ID
     * @param displayName The new display name
     * @param biography   The new biography
     * @return The updated profile
     */
    public UserProfile updateProfile(UUID userId, String displayName, String biography) {
        UserProfile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ValidationException("Profile not found for user: " + userId));

        profile.updateDisplayName(DisplayName.of(displayName), LocalDateTime.now());
        profile.updateBiography(Biography.of(biography), LocalDateTime.now());

        return profileRepository.save(profile);
    }
}