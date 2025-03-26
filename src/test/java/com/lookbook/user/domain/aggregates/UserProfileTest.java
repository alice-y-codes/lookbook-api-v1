package com.lookbook.user.domain.aggregates;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.lookbook.user.domain.valueobjects.Biography;
import com.lookbook.user.domain.valueobjects.DisplayName;
import com.lookbook.user.domain.valueobjects.ProfileImage;

@org.junit.jupiter.api.DisplayName("UserProfile")
class UserProfileTest {

    private UUID userId;
    private DisplayName displayName;
    private Biography biography;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        displayName = DisplayName.of("John Doe");
        biography = Biography.of("A fashion enthusiast who loves creating unique outfits");
        now = LocalDateTime.now();
    }

    @Test
    @org.junit.jupiter.api.DisplayName("should create with valid data")
    void shouldCreateWithValidData() {
        // When
        UserProfile profile = UserProfile.create(userId, displayName, biography, now);

        // Then
        assertNotNull(profile);
        assertNotNull(profile.getId());
        assertEquals(userId, profile.getUserId());
        assertEquals(displayName, profile.getDisplayName());
        assertEquals(biography, profile.getBiography());
        assertNull(profile.getProfileImage());
        assertEquals(now, profile.getCreatedAt());
        assertEquals(now, profile.getUpdatedAt());
    }

    @Test
    @org.junit.jupiter.api.DisplayName("should create with profile image")
    void shouldCreateWithProfileImage() {
        // Given
        ProfileImage profileImage = ProfileImage.of(
                URI.create("https://example.com/profile.jpg"),
                800, 600, "jpg", 1024 * 1024);

        // When
        UserProfile profile = UserProfile.create(userId, displayName, biography, profileImage, now);

        // Then
        assertNotNull(profile);
        assertEquals(profileImage, profile.getProfileImage());
    }

    @Test
    @org.junit.jupiter.api.DisplayName("should update display name")
    void shouldUpdateDisplayName() {
        // Given
        UserProfile profile = UserProfile.create(userId, displayName, biography, now);
        DisplayName newDisplayName = DisplayName.of("Jane Doe");
        LocalDateTime updateTime = LocalDateTime.now();

        // When
        profile.updateDisplayName(newDisplayName, updateTime);

        // Then
        assertEquals(newDisplayName, profile.getDisplayName());
        assertEquals(updateTime, profile.getUpdatedAt());
    }

    @Test
    @org.junit.jupiter.api.DisplayName("should update biography")
    void shouldUpdateBiography() {
        // Given
        UserProfile profile = UserProfile.create(userId, displayName, biography, now);
        Biography newBiography = Biography.of("Updated biography with more details about my fashion journey");
        LocalDateTime updateTime = LocalDateTime.now();

        // When
        profile.updateBiography(newBiography, updateTime);

        // Then
        assertEquals(newBiography, profile.getBiography());
        assertEquals(updateTime, profile.getUpdatedAt());
    }

    @Test
    @org.junit.jupiter.api.DisplayName("should update profile image")
    void shouldUpdateProfileImage() {
        // Given
        UserProfile profile = UserProfile.create(userId, displayName, biography, now);
        ProfileImage newProfileImage = ProfileImage.of(
                URI.create("https://example.com/new-profile.jpg"),
                1000, 800, "jpg", 2 * 1024 * 1024);
        LocalDateTime updateTime = LocalDateTime.now();

        // When
        profile.updateProfileImage(newProfileImage, updateTime);

        // Then
        assertEquals(newProfileImage, profile.getProfileImage());
        assertEquals(updateTime, profile.getUpdatedAt());
    }

    @Test
    @org.junit.jupiter.api.DisplayName("should reject update from different user")
    void shouldRejectUpdateFromDifferentUser() {
        // Given
        UserProfile profile = UserProfile.create(userId, displayName, biography, now);
        UUID differentUserId = UUID.randomUUID();
        DisplayName newDisplayName = DisplayName.of("Jane Doe");
        LocalDateTime updateTime = LocalDateTime.now();

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> profile.updateDisplayName(newDisplayName, updateTime, differentUserId));
        assertTrue(exception.getMessage().contains("not authorized"));
    }
}