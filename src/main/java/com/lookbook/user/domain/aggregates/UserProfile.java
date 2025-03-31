package com.lookbook.user.domain.aggregates;

import java.time.LocalDateTime;
import java.util.UUID;

import com.lookbook.base.domain.entities.BaseEntity;
import com.lookbook.user.domain.events.ProfileCreatedEvent;
import com.lookbook.user.domain.valueobjects.Biography;
import com.lookbook.user.domain.valueobjects.DisplayName;
import com.lookbook.user.domain.valueobjects.ProfileImage;

/**
 * Represents a user's profile in the Lookbook application.
 * Contains basic information about the user including their display name,
 * biography, and profile image.
 */
public class UserProfile extends BaseEntity {
    private final UUID userId;
    private DisplayName displayName;
    private Biography biography;
    private ProfileImage profileImage;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private UserProfile(UUID id, UUID userId, DisplayName displayName, Biography biography,
            ProfileImage profileImage, LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(id, createdAt, updatedAt);
        this.userId = userId;
        this.displayName = displayName;
        this.biography = biography;
        this.profileImage = profileImage;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Creates a new user profile with the given data.
     *
     * @param userId      the ID of the user this profile belongs to
     * @param displayName the user's display name
     * @param biography   the user's biography
     * @param createdAt   the creation timestamp
     * @return a new UserProfile instance
     */
    public static UserProfile create(UUID userId, DisplayName displayName, Biography biography,
            LocalDateTime createdAt) {
        UserProfile profile = new UserProfile(UUID.randomUUID(), userId, displayName, biography, null, createdAt,
                createdAt);

        // Add domain event
        profile.addDomainEvent(new ProfileCreatedEvent(
                userId,
                profile.getId(),
                displayName.getValue()));

        return profile;
    }

    /**
     * Creates a new user profile with the given data, including a profile image.
     *
     * @param userId       the ID of the user this profile belongs to
     * @param displayName  the user's display name
     * @param biography    the user's biography
     * @param profileImage the user's profile image
     * @param createdAt    the creation timestamp
     * @return a new UserProfile instance
     */
    public static UserProfile create(UUID userId, DisplayName displayName, Biography biography,
            ProfileImage profileImage, LocalDateTime createdAt) {
        UserProfile profile = new UserProfile(UUID.randomUUID(), userId, displayName, biography, profileImage,
                createdAt, createdAt);

        // Add domain event
        profile.addDomainEvent(new ProfileCreatedEvent(
                userId,
                profile.getId(),
                displayName.getValue()));

        return profile;
    }

    /**
     * Reconstructs a user profile from persistence.
     *
     * @param id           the profile's ID
     * @param userId       the ID of the user this profile belongs to
     * @param displayName  the user's display name
     * @param biography    the user's biography
     * @param profileImage the user's profile image
     * @param createdAt    the creation timestamp
     * @param updatedAt    the last update timestamp
     * @return a reconstructed UserProfile instance
     */
    public static UserProfile reconstitute(UUID id, UUID userId, DisplayName displayName,
            Biography biography, ProfileImage profileImage, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new UserProfile(id, userId, displayName, biography, profileImage, createdAt, updatedAt);
    }

    /**
     * Updates the profile's display name.
     *
     * @param newDisplayName the new display name
     * @param updatedAt      the update timestamp
     */
    public void updateDisplayName(DisplayName newDisplayName, LocalDateTime updatedAt) {
        this.displayName = newDisplayName;
        this.updatedAt = updatedAt;
    }

    /**
     * Updates the profile's display name.
     *
     * @param newDisplayName   the new display name
     * @param updatedAt        the update timestamp
     * @param requestingUserId the ID of the user making the request
     * @throws IllegalArgumentException if the requesting user is not the profile
     *                                  owner
     */
    public void updateDisplayName(DisplayName newDisplayName, LocalDateTime updatedAt, UUID requestingUserId) {
        validateOwner(requestingUserId);
        updateDisplayName(newDisplayName, updatedAt);
    }

    /**
     * Updates the profile's biography.
     *
     * @param newBiography the new biography
     * @param updatedAt    the update timestamp
     */
    public void updateBiography(Biography newBiography, LocalDateTime updatedAt) {
        this.biography = newBiography;
        this.updatedAt = updatedAt;
    }

    /**
     * Updates the profile's biography.
     *
     * @param newBiography     the new biography
     * @param updatedAt        the update timestamp
     * @param requestingUserId the ID of the user making the request
     * @throws IllegalArgumentException if the requesting user is not the profile
     *                                  owner
     */
    public void updateBiography(Biography newBiography, LocalDateTime updatedAt, UUID requestingUserId) {
        validateOwner(requestingUserId);
        updateBiography(newBiography, updatedAt);
    }

    /**
     * Updates the profile's image.
     *
     * @param newProfileImage the new profile image
     * @param updatedAt       the update timestamp
     */
    public void updateProfileImage(ProfileImage newProfileImage, LocalDateTime updatedAt) {
        this.profileImage = newProfileImage;
        this.updatedAt = updatedAt;
    }

    /**
     * Updates the profile's image.
     *
     * @param newProfileImage  the new profile image
     * @param updatedAt        the update timestamp
     * @param requestingUserId the ID of the user making the request
     * @throws IllegalArgumentException if the requesting user is not the profile
     *                                  owner
     */
    public void updateProfileImage(ProfileImage newProfileImage, LocalDateTime updatedAt, UUID requestingUserId) {
        validateOwner(requestingUserId);
        updateProfileImage(newProfileImage, updatedAt);
    }

    private void validateOwner(UUID requestingUserId) {
        if (!userId.equals(requestingUserId)) {
            throw new IllegalArgumentException("User is not authorized to update this profile");
        }
    }

    public UUID getUserId() {
        return userId;
    }

    public DisplayName getDisplayName() {
        return displayName;
    }

    public Biography getBiography() {
        return biography;
    }

    public ProfileImage getProfileImage() {
        return profileImage;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}