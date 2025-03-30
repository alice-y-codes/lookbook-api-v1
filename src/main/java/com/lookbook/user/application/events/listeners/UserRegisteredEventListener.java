package com.lookbook.user.application.events.listeners;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lookbook.user.application.events.CreateUserProfileEvent;
import com.lookbook.user.application.ports.services.ProfileService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Event listener for handling user registration events.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserRegisteredEventListener {
    private final ProfileService profileService;

    /**
     * Handles the CreateUserProfileEvent by creating a new profile for the user.
     *
     * @param event The CreateUserProfileEvent to handle
     */
    @EventListener
    @Transactional
    public void handleCreateUserProfile(CreateUserProfileEvent event) {
        try {
            log.info("Creating profile for user: {}", event.getUserId());
            profileService.createProfile(event.getUserId(), event.getUsername());
            log.info("Profile created successfully for user: {}", event.getUserId());
        } catch (Exception e) {
            log.error("Failed to create profile for user: {}", event.getUserId(), e);
            throw e;
        }
    }
}