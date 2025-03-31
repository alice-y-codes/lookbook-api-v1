package com.lookbook.user.application.services;

import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lookbook.base.domain.events.DomainEvent;
import com.lookbook.base.domain.exceptions.ValidationException;
import com.lookbook.communication.application.events.SendAccountActivationEmailEvent;
import com.lookbook.communication.application.events.SendWelcomeEmailEvent;
import com.lookbook.user.application.events.CreateUserProfileEvent;
import com.lookbook.user.domain.aggregates.User;
import com.lookbook.user.domain.events.PasswordChangedEvent;
import com.lookbook.user.domain.events.UserActivatedEvent;
import com.lookbook.user.domain.events.UserDeactivatedEvent;
import com.lookbook.user.domain.events.UserRegisteredEvent;
import com.lookbook.user.domain.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Application service for handling user-related operations.
 * Manages the coordination between domain events and application events.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserApplicationService implements IUserApplicationService {
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * Registers a new user and handles the resulting domain events.
     *
     * @param username The username
     * @param email    The email
     * @param password The password
     * @return The created user
     * @throws ValidationException if validation fails
     */
    public User registerUser(String username, String email, String password) {
        try {
            // Create user (this will trigger domain events)
            User user = User.register(username, email, password);

            // Save user
            user = userRepository.save(user);

            // Handle domain events
            handleDomainEvents(user);

            return user;
        } catch (Exception e) {
            log.error("Failed to register user: {}", username, e);
            throw e;
        }
    }

    /**
     * Activates a user and handles the resulting domain events.
     *
     * @param userId The user's ID
     * @return The activated user
     */
    public User activateUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ValidationException("User not found"));

        user.activate();
        user = userRepository.save(user);
        handleDomainEvents(user);

        return user;
    }

    /**
     * Deactivates a user and handles the resulting domain events.
     *
     * @param userId The user's ID
     * @return The deactivated user
     */
    public User deactivateUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ValidationException("User not found"));

        user.deactivate();
        user = userRepository.save(user);
        handleDomainEvents(user);

        return user;
    }

    /**
     * Changes a user's password and handles the resulting domain events.
     *
     * @param userId          The user's ID
     * @param currentPassword The current password
     * @param newPassword     The new password
     * @return The updated user
     */
    public User changePassword(UUID userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ValidationException("User not found"));

        user.changePassword(currentPassword, newPassword);
        user = userRepository.save(user);
        handleDomainEvents(user);

        return user;
    }

    /**
     * Handles domain events and publishes corresponding application events.
     *
     * @param user The user whose domain events should be handled
     */
    private void handleDomainEvents(User user) {
        for (DomainEvent event : user.getDomainEvents()) {
            try {
                if (event instanceof UserRegisteredEvent) {
                    handleUserRegistered((UserRegisteredEvent) event);
                } else if (event instanceof UserActivatedEvent) {
                    handleUserActivated((UserActivatedEvent) event);
                } else if (event instanceof UserDeactivatedEvent) {
                    handleUserDeactivated((UserDeactivatedEvent) event);
                } else if (event instanceof PasswordChangedEvent) {
                    handlePasswordChanged((PasswordChangedEvent) event);
                }
            } catch (Exception e) {
                log.error("Failed to handle domain event: {}", event.getEventType(), e);
                throw e;
            }
        }
        user.clearDomainEvents();
    }

    private void handleUserRegistered(UserRegisteredEvent event) {
        log.info("Handling user registration event for user: {}", event.getUserId());

        // Publish application events
        eventPublisher.publishEvent(new SendWelcomeEmailEvent(
                event.getEmail(),
                event.getUsername()));

        eventPublisher.publishEvent(new SendAccountActivationEmailEvent(
                event.getEmail(),
                generateActivationToken()));

        eventPublisher.publishEvent(new CreateUserProfileEvent(
                event.getUserId(),
                event.getUsername()));
    }

    private void handleUserActivated(UserActivatedEvent event) {
        log.info("Handling user activation event for user: {}", event.getUserId());
        // Add any additional application events for user activation
    }

    private void handleUserDeactivated(UserDeactivatedEvent event) {
        log.info("Handling user deactivation event for user: {}", event.getUserId());
        // Add any additional application events for user deactivation
    }

    private void handlePasswordChanged(PasswordChangedEvent event) {
        log.info("Handling password change event for user: {}", event.getUserId());
        // Add any additional application events for password change
    }

    private String generateActivationToken() {
        // TODO: Implement proper token generation
        return UUID.randomUUID().toString();
    }
}