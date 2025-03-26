package com.lookbook.user.domain.valueobjects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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
            "This biography is way too long. This biography is way too long. This biography is way too long. This biography is way too long. This biography is way too long. This biography is way too long. This biography is way too long. This biography is way too long. This biography is way too long. This biography is way too long." // too
                                                                                                                                                                                                                                                                                                                                              // long
    })
    @DisplayName("should reject invalid biographies")
    void shouldRejectInvalidBiographies(String invalidBio) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> Biography.of(invalidBio));
        assertTrue(exception.getMessage().contains("Biography"));
    }

    @Test
    @DisplayName("should reject null biography")
    void shouldRejectNullBiography() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> Biography.of(null));
        assertTrue(exception.getMessage().contains("Biography"));
    }

    @Test
    @DisplayName("should handle special characters correctly")
    void shouldHandleSpecialCharacters() {
        Biography bio = Biography.of("This is a biography with special characters: !@#$%^&*()_+");
        assertEquals("This is a biography with special characters: !@#$%^&*()_+", bio.getValue());
    }
}