package com.lookbook.user.infrastructure.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lookbook.base.domain.exceptions.ValidationException;
import com.lookbook.base.infrastructure.api.controllers.BaseController;
import com.lookbook.base.infrastructure.api.response.ApiResponse;
import com.lookbook.user.application.dtos.ProfileResponse;
import com.lookbook.user.application.mappers.ProfileMapper;
import com.lookbook.user.application.ports.services.ProfileImageService;
import com.lookbook.user.domain.aggregates.UserProfile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * REST controller for profile image operations.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/profiles/me/image")
@RequiredArgsConstructor
public class ProfileImageController extends BaseController {

    private final ProfileImageService profileImageService;

    /**
     * Upload a new profile image.
     *
     * @param file the image file to upload
     * @return the updated profile
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ProfileResponse>> uploadProfileImage(
            @RequestParam("file") MultipartFile file) {
        Authentication authentication = getCurrentUser();
        if (authentication == null) {
            throw new ValidationException("Not authenticated");
        }

        String username = authentication.getName();
        UserProfile profile = profileImageService.uploadProfileImage(username, file);

        return ResponseEntity.ok(createSuccessResponse(ProfileMapper.toProfileResponse(profile)));
    }

    /**
     * Remove the current profile image.
     *
     * @return the updated profile
     */
    @DeleteMapping
    public ResponseEntity<ApiResponse<ProfileResponse>> removeProfileImage() {
        Authentication authentication = getCurrentUser();
        if (authentication == null) {
            throw new ValidationException("Not authenticated");
        }

        String username = authentication.getName();
        UserProfile profile = profileImageService.removeProfileImage(username);

        return ResponseEntity.ok(createSuccessResponse(ProfileMapper.toProfileResponse(profile)));
    }
}