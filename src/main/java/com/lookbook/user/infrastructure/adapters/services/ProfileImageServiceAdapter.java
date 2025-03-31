package com.lookbook.user.infrastructure.adapters.services;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.lookbook.base.domain.exceptions.ValidationException;
import com.lookbook.user.application.ports.services.ProfileImageService;
import com.lookbook.user.domain.aggregates.UserProfile;
import com.lookbook.user.domain.repositories.FileStorageRepository;
import com.lookbook.user.domain.repositories.ProfileRepository;
import com.lookbook.user.domain.valueobjects.ProfileImage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Adapter implementation of the ProfileImageService interface.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProfileImageServiceAdapter implements ProfileImageService {
    private final ProfileRepository profileRepository;
    private final FileStorageRepository fileStorageRepository;

    @Override
    public UserProfile uploadProfileImage(String username, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ValidationException("No file provided");
        }

        // Get user profile
        UserProfile profile = profileRepository.findByUsername(username)
                .orElseThrow(() -> new ValidationException("Profile not found for user: " + username));

        try {
            // Store the file
            String fileName = String.format("profiles/%s/%s", profile.getUserId(), UUID.randomUUID());
            URI fileUri = fileStorageRepository.storeFile(file.getInputStream(), fileName, file.getContentType());

            // Create profile image value object
            ProfileImage profileImage = ProfileImage.of(
                    fileUri,
                    800, // TODO: Get actual image dimensions
                    600,
                    file.getContentType(),
                    file.getSize());

            // Update profile
            profile.updateProfileImage(profileImage, LocalDateTime.now());
            return profileRepository.save(profile);

        } catch (Exception e) {
            log.error("Failed to upload profile image for user: {}", username, e);
            throw new ValidationException("Failed to upload profile image: " + e.getMessage());
        }
    }

    @Override
    public UserProfile removeProfileImage(String username) {
        UserProfile profile = profileRepository.findByUsername(username)
                .orElseThrow(() -> new ValidationException("Profile not found for user: " + username));

        if (profile.getProfileImage() != null) {
            try {
                // Delete the file
                fileStorageRepository.deleteFile(profile.getProfileImage().getUrl());

                // Update profile
                profile.updateProfileImage(null, LocalDateTime.now());
                return profileRepository.save(profile);

            } catch (Exception e) {
                log.error("Failed to remove profile image for user: {}", username, e);
                throw new ValidationException("Failed to remove profile image: " + e.getMessage());
            }
        }

        return profile;
    }
}