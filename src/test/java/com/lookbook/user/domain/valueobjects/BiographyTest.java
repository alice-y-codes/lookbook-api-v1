package com.lookbook.user.domain.valueobjects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.lookbook.base.domain.exceptions.ValidationException;

@DisplayName("Biography")
class BiographyTest {

    @Test
    @DisplayName("should create with valid biography")
    void shouldCreateWithValidBiography() {
        Biography bio = Biography.of("This is a valid biography that meets the minimum length requirement.");
        assertEquals("This is a valid biography that meets the minimum length requirement.", bio.getValue());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "", // empty
            " ", // whitespace
            "Too short", // too short
            "This biography is way too long. This biography is way too long. This biography is way too long. " +
                    "This biography is way too long. This biography is way too long. This biography is way too long. " +
                    "This biography is way too long. This biography is way too long. This biography is way too long. " +
                    "This biography is way too long. This biography is way too long. This biography is way too long." // too
                                                                                                                      // long
    })
    @DisplayName("should reject invalid biographies")
    void shouldRejectInvalidBiographies(String invalidBio) {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> Biography.of(invalidBio));

        String errorMessage = exception.getMessage();
        if (invalidBio == null || invalidBio.trim().isEmpty()) {
            assertTrue(errorMessage.contains("Biography cannot be empty"),
                    "Expected error message to contain 'Biography cannot be empty' but got: " + errorMessage);
        } else if (invalidBio.length() < 10) {
            assertTrue(errorMessage.contains("Biography must be between 10 and 500 characters"),
                    "Expected error message to contain length validation message but got: " + errorMessage);
        } else {
            assertTrue(errorMessage.contains("Biography must be between 10 and 500 characters"),
                    "Expected error message to contain length validation message but got: " + errorMessage);
        }
    }

    @Test
    @DisplayName("should reject null biography")
    void shouldRejectNullBiography() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> Biography.of(null));
        assertTrue(exception.getMessage().contains("Biography cannot be empty"));
    }

    @Test
    @DisplayName("should handle special characters correctly")
    void shouldHandleSpecialCharacters() {
        Biography bio = Biography.of("This is a biography with special characters: !@#$%^&*()_+");
        assertEquals("This is a biography with special characters: !@#$%^&*()_+", bio.getValue());
    }
}