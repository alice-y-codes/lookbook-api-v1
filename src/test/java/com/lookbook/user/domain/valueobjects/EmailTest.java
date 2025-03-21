package com.lookbook.user.domain.valueobjects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.lookbook.base.domain.exceptions.ValidationException;

public class EmailTest {

    @Test
    void shouldCreateValidEmail() {
        // Given
        String validEmail = "test@example.com";

        // When
        Email email = Email.of(validEmail);

        // Then
        assertEquals(validEmail, email.getValue());
    }

    @Test
    void shouldNormalizeEmail() {
        // Given
        String mixedCaseEmail = "Test@ExaMple.Com";

        // When
        Email email = Email.of(mixedCaseEmail);

        // Then
        assertEquals("test@example.com", email.getValue());
    }

    @Test
    void shouldTrimWhitespace() {
        // Given
        String emailWithWhitespace = "   test@example.com   ";

        // When
        Email email = Email.of(emailWithWhitespace);

        // Then
        assertEquals("test@example.com", email.getValue());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "plainaddress",
            "@missingusername.com",
            "username@.com",
            "username@domain.toolongtld",
            "username@domain@domain.com",
            "username@domain..com"
    })
    void shouldRejectInvalidEmails(String invalidEmail) {
        // Then
        assertThrows(ValidationException.class, () -> Email.of(invalidEmail));
    }

    @Test
    void shouldRejectNullEmail() {
        // Then
        assertThrows(ValidationException.class, () -> Email.of(null));
    }

    @Test
    void shouldRejectTooLongEmail() {
        // Given
        String tooLongEmail = "a".repeat(256) + "@example.com";

        // Then
        assertThrows(ValidationException.class, () -> Email.of(tooLongEmail));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "user@tempmail.com",
            "test@disposable.com"
    })
    void shouldRejectBlockedDomains(String blockedDomainEmail) {
        // Then
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> Email.of(blockedDomainEmail));

        // Verify error message contains domain rejection message
        assertTrue(
                exception.getMessage().contains("domain is not allowed"),
                "Exception should mention domain not being allowed");
    }

    @Test
    void equalEmailsShouldBeEqual() {
        // Given
        Email email1 = Email.of("test@example.com");
        Email email2 = Email.of("test@example.com");

        // Then
        assertEquals(email1, email2);
        assertEquals(email1.hashCode(), email2.hashCode());
    }

    @Test
    void differentEmailsShouldNotBeEqual() {
        // Given
        Email email1 = Email.of("test@example.com");
        Email email2 = Email.of("test2@example.com");

        // Then
        assertNotEquals(email1, email2);
    }

    @Test
    void toStringShouldReturnEmailValue() {
        // Given
        String emailValue = "test@example.com";
        Email email = Email.of(emailValue);

        // Then
        assertEquals(emailValue, email.toString());
    }
}