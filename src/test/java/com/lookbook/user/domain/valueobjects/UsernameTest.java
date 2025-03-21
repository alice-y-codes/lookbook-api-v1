package com.lookbook.user.domain.valueobjects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.lookbook.base.domain.exceptions.ValidationException;

class UsernameTest {

    @Test
    void shouldCreateValidUsername() {
        // Given
        String validUsername = "john_doe";

        // When
        Username username = Username.of(validUsername);

        // Then
        assertEquals(validUsername, username.getValue());
    }

    @Test
    void shouldTrimWhitespace() {
        // Given
        String usernameWithWhitespace = "  john_doe  ";

        // When
        Username username = Username.of(usernameWithWhitespace);

        // Then
        assertEquals("john_doe", username.getValue());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "ab", // too short
            "abcdefghijklmnopqrstuvwxyz", // too long
            "user name", // contains space
            "user@name", // contains special character
            "user.name", // contains dot
            "$pecial" // contains dollar sign
    })
    void shouldRejectInvalidUsernames(String invalidUsername) {
        // Then
        assertThrows(ValidationException.class, () -> Username.of(invalidUsername));
    }

    @Test
    void shouldRejectNullUsername() {
        // Then
        assertThrows(ValidationException.class, () -> Username.of(null));
    }

    @Test
    void shouldRejectTooShortUsername() {
        // Given
        String tooShortUsername = "ab";

        // Then
        ValidationException exception = assertThrows(ValidationException.class,
                () -> Username.of(tooShortUsername));

        // Verify error message for minimum length
        assertTrue(exception.getMessage().contains("too short"));
    }

    @Test
    void shouldRejectTooLongUsername() {
        // Given
        String tooLongUsername = "abcdefghijklmnopqrstuvwxyz";

        // Then
        ValidationException exception = assertThrows(ValidationException.class,
                () -> Username.of(tooLongUsername));

        // Verify error message for maximum length
        assertTrue(exception.getMessage().contains("too long"));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "admin",
            "administrator",
            "system",
            "moderator"
    })
    void shouldRejectReservedUsernames(String reservedUsername) {
        // Then
        ValidationException exception = assertThrows(ValidationException.class,
                () -> Username.of(reservedUsername));

        // Verify error message mentions reserved username
        assertTrue(exception.getMessage().contains("reserved"));
    }

    @Test
    void shouldAcceptValidCharacters() {
        // Given - usernames with valid characters
        Username username1 = Username.of("user123");
        Username username2 = Username.of("user_name");
        Username username3 = Username.of("user-name");

        // Then - no exceptions thrown
        assertEquals("user123", username1.getValue());
        assertEquals("user_name", username2.getValue());
        assertEquals("user-name", username3.getValue());
    }

    @Test
    void equalUsernamesShouldBeEqual() {
        // Given
        Username username1 = Username.of("john_doe");
        Username username2 = Username.of("john_doe");

        // Then
        assertEquals(username1, username2);
        assertEquals(username1.hashCode(), username2.hashCode());
    }

    @Test
    void differentUsernamesShouldNotBeEqual() {
        // Given
        Username username1 = Username.of("john_doe");
        Username username2 = Username.of("jane_doe");

        // Then
        assertNotEquals(username1, username2);
    }

    @Test
    void toStringShouldReturnUsernameValue() {
        // Given
        String usernameValue = "john_doe";
        Username username = Username.of(usernameValue);

        // Then
        assertEquals(usernameValue, username.toString());
    }
}