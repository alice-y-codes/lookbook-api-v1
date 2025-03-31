package com.lookbook.user.infrastructure.adapters.services;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lookbook.base.domain.exceptions.ValidationException;
import com.lookbook.user.application.ports.services.ProfileService;
import com.lookbook.user.domain.aggregates.UserProfile;
import com.lookbook.user.domain.repositories.ProfileRepository;
import com.lookbook.user.domain.valueobjects.Biography;
import com.lookbook.user.domain.valueobjects.DisplayName;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Adapter implementation of the ProfileService interface.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProfileServiceAdapter implements ProfileService {
    private final ProfileRepository profileRepository;

    @Override
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

    @Override
    public UserProfile updateProfile(UUID userId, String displayName, String biography) {
        UserProfile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ValidationException("Profile not found for user: " + userId));

        profile.updateDisplayName(DisplayName.of(displayName), LocalDateTime.now());
        profile.updateBiography(Biography.of(biography), LocalDateTime.now());

        return profileRepository.save(profile);
    }

    @Override
    public Optional<UserProfile> findByUsername(String username) {
        return profileRepository.findByUsername(username);
    }
}