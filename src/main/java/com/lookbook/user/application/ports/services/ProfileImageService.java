package com.lookbook.user.application.ports.services;

import org.springframework.web.multipart.MultipartFile;

import com.lookbook.user.domain.aggregates.UserProfile;

/**
 * Service interface for profile image operations.
 */
public interface ProfileImageService {
    /**
     * Uploads and sets a new profile image for a user.
     *
     * @param username the username
     * @param file     the image file to upload
     * @return the updated profile
     */
    UserProfile uploadProfileImage(String username, MultipartFile file);

    /**
     * Removes the current profile image for a user.
     *
     * @param username the username
     * @return the updated profile
     */
    UserProfile removeProfileImage(String username);
}