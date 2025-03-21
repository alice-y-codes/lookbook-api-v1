package com.lookbook.user.domain.aggregates;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.lookbook.base.domain.exceptions.ValidationException;
import com.lookbook.user.domain.valueobjects.Email;

class UserTest {

    private static final String VALID_USERNAME = "john_doe";
    private static final String VALID_EMAIL = "john@example.com";
    private static final String VALID_PASSWORD = "Password123!";

    @Test
    void shouldCreateUserWithPendingStatus() {
        // When
        User user = createValidUser();

        // Then
        assertEquals(UserStatus.PENDING, user.getStatus());
        assertNotNull(user.getId());
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUpdatedAt());
    }

    @Test
    void shouldNotCreateUserWithNullUsername() {
        // Then
        assertThrows(ValidationException.class, () -> User.register(null, VALID_EMAIL, VALID_PASSWORD));
    }

    @Test
    void shouldNotCreateUserWithNullEmail() {
        // Then
        assertThrows(ValidationException.class, () -> User.register(VALID_USERNAME, null, VALID_PASSWORD));
    }

    @Test
    void shouldNotCreateUserWithNullPassword() {
        // Then
        assertThrows(ValidationException.class, () -> User.register(VALID_USERNAME, VALID_EMAIL, null));
    }

    @Test
    void shouldActivateUser() {
        // Given
        User user = createValidUser();

        // When
        user.activate();

        // Then
        assertEquals(UserStatus.ACTIVE, user.getStatus());
    }

    @Test
    void shouldDeactivateUser() {
        // Given
        User user = createValidUser();
        user.activate();

        // When
        user.deactivate();

        // Then
        assertEquals(UserStatus.INACTIVE, user.getStatus());
    }

    @Test
    void shouldReactivateUser() {
        // Given
        User user = createValidUser();
        user.activate();
        user.deactivate();

        // When
        user.activate();

        // Then
        assertEquals(UserStatus.ACTIVE, user.getStatus());
    }

    @Test
    void shouldNotActivateAlreadyActiveUser() {
        // Given
        User user = createValidUser();
        user.activate();

        // When
        user.activate();

        // Then
        assertEquals(UserStatus.ACTIVE, user.getStatus());
    }

    @Test
    void shouldNotDeactivateAlreadyInactiveUser() {
        // Given
        User user = createValidUser();
        user.deactivate();

        // When
        user.deactivate();

        // Then
        assertEquals(UserStatus.INACTIVE, user.getStatus());
    }

    @Test
    void shouldChangePasword() {
        // Given
        User user = createValidUser();
        String newPassword = "NewPassword456!";

        // When
        user.changePassword(VALID_PASSWORD, newPassword);

        // Then
        assertTrue(user.checkPassword(newPassword));
        assertFalse(user.checkPassword(VALID_PASSWORD));
    }

    @Test
    void shouldNotChangePaswordWithIncorrectCurrentPassword() {
        // Given
        User user = createValidUser();
        String wrongPassword = "WrongPassword123!";
        String newPassword = "NewPassword456!";

        // Then
        assertThrows(ValidationException.class, () -> user.changePassword(wrongPassword, newPassword));

        // Password should remain unchanged
        assertTrue(user.checkPassword(VALID_PASSWORD));
        assertFalse(user.checkPassword(newPassword));
    }

    @Test
    void shouldCheckCorrectPassword() {
        // Given
        User user = createValidUser();

        // Then
        assertTrue(user.checkPassword(VALID_PASSWORD));
    }

    @Test
    void shouldRejectIncorrectPassword() {
        // Given
        User user = createValidUser();

        // Then
        assertFalse(user.checkPassword("WrongPassword123!"));
    }

    @Test
    void shouldUpdateEmail() {
        // Given
        User user = createValidUser();
        String newEmail = "new.email@example.com";

        // When
        user.updateEmail(newEmail);

        // Then
        assertEquals(Email.of(newEmail), user.getEmail());
    }

    // Helper method to create a valid user
    private User createValidUser() {
        return User.register(VALID_USERNAME, VALID_EMAIL, VALID_PASSWORD);
    }
}