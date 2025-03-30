package com.lookbook.user.infrastructure.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lookbook.base.domain.exceptions.ValidationException;
import com.lookbook.base.infrastructure.api.controllers.BaseController;
import com.lookbook.base.infrastructure.api.response.ApiResponse;
import com.lookbook.user.application.dtos.ProfileResponse;
import com.lookbook.user.application.dtos.UpdateProfileRequest;
import com.lookbook.user.application.mappers.ProfileMapper;
import com.lookbook.user.application.ports.services.ProfileService;
import com.lookbook.user.domain.aggregates.UserProfile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * REST controller for profile operations.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/profiles")
@RequiredArgsConstructor
public class ProfileController extends BaseController {

    private final ProfileService profileService;

    /**
     * Get the current user's profile.
     *
     * @return the user's profile
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<ProfileResponse>> getMyProfile() {
        Authentication authentication = getCurrentUser();
        if (authentication == null) {
            throw new ValidationException("Not authenticated");
        }

        String username = authentication.getName();
        UserProfile profile = profileService.findByUsername(username)
                .orElseThrow(() -> new ValidationException("Profile not found for user: " + username));

        return ResponseEntity.ok(createSuccessResponse(ProfileMapper.toProfileResponse(profile)));
    }

    /**
     * Create a new profile for the current user.
     *
     * @param request the profile creation request
     * @return the created profile
     */
    @PutMapping("/me")
    public ResponseEntity<ApiResponse<ProfileResponse>> updateMyProfile(
            @Valid @RequestBody UpdateProfileRequest request) {
        Authentication authentication = getCurrentUser();
        if (authentication == null) {
            throw new ValidationException("Not authenticated");
        }

        String username = authentication.getName();
        UserProfile profile = profileService.findByUsername(username)
                .orElseThrow(() -> new ValidationException("Profile not found for user: " + username));

        // Update profile
        profileService.updateProfile(profile.getId(), request.getDisplayName(), request.getBiography());

        return ResponseEntity.ok(createSuccessResponse(ProfileMapper.toProfileResponse(profile)));
    }
}