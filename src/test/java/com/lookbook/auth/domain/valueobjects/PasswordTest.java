package com.lookbook.auth.domain.valueobjects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.lookbook.base.domain.exceptions.ValidationException;

class PasswordTest {

    private static final String VALID_PASSWORD = "Password123!";

    @Test
    void shouldCreateValidPassword() {
        // Given a valid password

        // When
        Password password = Password.create(VALID_PASSWORD);

        // Then
        assertNotNull(password);
        assertNotNull(password.getHashedValue());
        assertTrue(password.getHashedValue().startsWith("$2a$")); // BCrypt hash starts with $2a$
    }

    @Test
    void shouldRejectNullPassword() {
        // Then
        assertThrows(ValidationException.class, () -> Password.create(null));
    }

    @Test
    void shouldRejectEmptyPassword() {
        // Then
        assertThrows(ValidationException.class, () -> Password.create(""));
    }

    @Test
    void shouldRejectTooShortPassword() {
        // Given
        String tooShortPassword = "Abc1!";

        // Then
        ValidationException exception = assertThrows(ValidationException.class,
                () -> Password.create(tooShortPassword));

        // Verify error message
        assertTrue(exception.getMessage().contains("too short"));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "password123!", // no uppercase
            "PASSWORD123!", // no lowercase
            "Password!!!!", // no digit
            "Password123" // no special char
    })
    void shouldRejectPasswordsWithMissingRequiredCharacters(String invalidPassword) {
        // Then
        assertThrows(ValidationException.class, () -> Password.create(invalidPassword));
    }

    @Test
    void passwordShouldMatchCorrectPassword() {
        // Given
        Password password = Password.create(VALID_PASSWORD);

        // Then
        assertTrue(password.matches(VALID_PASSWORD));
    }

    @Test
    void passwordShouldNotMatchIncorrectPassword() {
        // Given
        Password password = Password.create(VALID_PASSWORD);

        // Then
        assertFalse(password.matches("WrongPassword123!"));
    }

    @Test
    void passwordShouldNotMatchNull() {
        // Given
        Password password = Password.create(VALID_PASSWORD);

        // Then
        assertFalse(password.matches(null));
    }

    @Test
    void passwordShouldNotMatchEmptyString() {
        // Given
        Password password = Password.create(VALID_PASSWORD);

        // Then
        assertFalse(password.matches(""));
    }

    @Test
    void samePlaintextShouldProduceDifferentHashesDueToSalt() {
        // Given
        Password password1 = Password.create(VALID_PASSWORD);
        Password password2 = Password.create(VALID_PASSWORD);

        // Then
        assertNotEquals(password1.getHashedValue(), password2.getHashedValue());
    }

    @Test
    void shouldRecreateFromHash() {
        // Given
        Password original = Password.create(VALID_PASSWORD);
        String hash = original.getHashedValue();

        // When
        Password recreated = Password.fromHash(hash);

        // Then
        assertEquals(hash, recreated.getHashedValue());
    }

    @Test
    void recreatedPasswordShouldMatchOriginalPlaintext() {
        // Given
        Password original = Password.create(VALID_PASSWORD);
        String hash = original.getHashedValue();

        // When
        Password recreated = Password.fromHash(hash);

        // Then
        assertTrue(recreated.matches(VALID_PASSWORD));
    }

    @Test
    void equalPasswordsShouldHaveSameHashCode() {
        // Given
        Password password1 = Password.create(VALID_PASSWORD);
        String hash = password1.getHashedValue();
        Password password2 = Password.fromHash(hash);

        // Then
        assertEquals(password1, password2);
        assertEquals(password1.hashCode(), password2.hashCode());
    }

    @Test
    void differentPasswordsShouldNotBeEqual() {
        // Given
        Password password1 = Password.create(VALID_PASSWORD);
        Password password2 = Password.create("DifferentPassword123!");

        // Then
        assertNotEquals(password1, password2);
    }

    @Test
    void toStringShouldNotRevealPasswordDetails() {
        // Given
        Password password = Password.create(VALID_PASSWORD);

        // When
        String stringRepresentation = password.toString();

        // Then
        assertFalse(stringRepresentation.contains(VALID_PASSWORD));
        assertFalse(stringRepresentation.contains(password.getHashedValue()));
        assertTrue(stringRepresentation.contains("PROTECTED"));
    }
}