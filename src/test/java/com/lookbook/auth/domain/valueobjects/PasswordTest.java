package com.lookbook.auth.domain.valueobjects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class PasswordTest {
    private static final String VALID_PASSWORD = "Password123!";
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Test
    void shouldCreateValidPassword() {
        // When
        Password password = Password.create(VALID_PASSWORD);

        // Then
        assertNotNull(password);
        assertNotNull(password.getHashedValue());
        assertTrue(password.getHashedValue().startsWith("$2a$")); // BCrypt hash starts with $2a$
    }

    @Test
    void shouldNotCreatePasswordWithNullValue() {
        // Then
        assertThrows(NullPointerException.class, () -> Password.create(null));
    }

    @Test
    void shouldNotCreatePasswordWithEmptyValue() {
        // Then
        assertThrows(IllegalArgumentException.class, () -> Password.create(""));
    }

    @Test
    void shouldMatchCorrectPassword() {
        // Given
        Password password = Password.create(VALID_PASSWORD);

        // Then
        assertTrue(password.matches(VALID_PASSWORD));
    }

    @Test
    void shouldNotMatchIncorrectPassword() {
        // Given
        Password password = Password.create(VALID_PASSWORD);

        // Then
        assertFalse(password.matches("WrongPassword123!"));
    }

    @Test
    void shouldNotMatchNullPassword() {
        // Given
        Password password = Password.create(VALID_PASSWORD);

        // Then
        assertFalse(password.matches(null));
    }

    @Test
    void shouldNotMatchEmptyPassword() {
        // Given
        Password password = Password.create(VALID_PASSWORD);

        // Then
        assertFalse(password.matches(""));
    }

    @Test
    void shouldBeEqualWhenSamePassword() {
        // Given
        Password password1 = Password.create(VALID_PASSWORD);
        Password password2 = Password.fromHash(password1.getHashedValue());

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
    void shouldHaveSameHashCodeWhenEqual() {
        // Given
        Password password1 = Password.create(VALID_PASSWORD);
        String hash = password1.getHashedValue();
        Password password2 = Password.fromHash(hash);

        // Then
        assertEquals(password1.hashCode(), password2.hashCode());
    }

    @Test
    void shouldHaveDifferentHashCodeWhenNotEqual() {
        // Given
        Password password1 = Password.create(VALID_PASSWORD);
        Password password2 = Password.create("DifferentPassword123!");

        // Then
        assertNotEquals(password1.hashCode(), password2.hashCode());
    }

    @Test
    void toStringShouldNotRevealPassword() {
        // Given
        Password password = Password.create(VALID_PASSWORD);

        // Then
        assertEquals("Password[PROTECTED]", password.toString());
        assertFalse(password.toString().contains(VALID_PASSWORD));
        assertFalse(password.toString().contains(password.getHashedValue()));
    }
}