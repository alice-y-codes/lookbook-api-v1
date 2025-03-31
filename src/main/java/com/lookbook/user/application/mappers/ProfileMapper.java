package com.lookbook.user.application.mappers;

import java.time.LocalDateTime;
import java.util.UUID;

import com.lookbook.user.application.dtos.CreateProfileRequest;
import com.lookbook.user.application.dtos.ProfileResponse;
import com.lookbook.user.application.dtos.UpdateProfileRequest;
import com.lookbook.user.domain.aggregates.UserProfile;
import com.lookbook.user.domain.valueobjects.Biography;
import com.lookbook.user.domain.valueobjects.DisplayName;

/**
 * Mapper class for converting between Profile domain objects and DTOs.
 */
public class ProfileMapper {

    private ProfileMapper() {
        // Private constructor to prevent instantiation
    }

    /**
     * Maps a UserProfile domain entity to a ProfileResponse DTO.
     *
     * @param profile the domain entity
     * @return the response DTO
     */
    public static ProfileResponse toProfileResponse(UserProfile profile) {
        if (profile == null) {
            return null;
        }

        return new ProfileResponse(
                profile.getId(),
                profile.getUserId(),
                profile.getDisplayName().getValue(),
                profile.getBiography().getValue(),
                profile.getProfileImage() != null ? profile.getProfileImage().getUrl().toString() : null,
                profile.getCreatedAt(),
                profile.getUpdatedAt());
    }

    /**
     * Creates a UserProfile domain entity from a CreateProfileRequest DTO.
     *
     * @param request the request DTO
     * @param userId  the ID of the user creating the profile
     * @return the domain entity
     */
    public static UserProfile fromCreateRequest(CreateProfileRequest request, UUID userId) {
        return UserProfile.create(
                userId,
                DisplayName.of(request.getDisplayName()),
                Biography.of(request.getBiography()),
                LocalDateTime.now());
    }

    /**
     * Updates a UserProfile domain entity using an UpdateProfileRequest DTO.
     *
     * @param profile the profile to update
     * @param request the update request
     */
    public static void updateFromRequest(UserProfile profile, UpdateProfileRequest request) {
        LocalDateTime now = LocalDateTime.now();

        if (request.getDisplayName() != null) {
            profile.updateDisplayName(DisplayName.of(request.getDisplayName()), now);
        }

        if (request.getBiography() != null) {
            profile.updateBiography(Biography.of(request.getBiography()), now);
        }
    }
}