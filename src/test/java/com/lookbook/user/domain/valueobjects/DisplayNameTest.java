package com.lookbook.user.domain.valueobjects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@org.junit.jupiter.api.DisplayName("DisplayName")
class DisplayNameTest {

    @Test
    @org.junit.jupiter.api.DisplayName("should create with valid name")
    void shouldCreateWithValidName() {
        DisplayName name = DisplayName.of("John Doe");
        assertEquals("John Doe", name.getValue());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "", // empty
            " ", // whitespace
            "J", // too short
            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", // too long (51 chars)
            "John@Doe", // invalid characters
            "John/Doe", // invalid characters
            "John\\Doe" // invalid characters
    })
    @org.junit.jupiter.api.DisplayName("should reject invalid names")
    void shouldRejectInvalidNames(String invalidName) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> DisplayName.of(invalidName));
        assertTrue(exception.getMessage().contains("Display name"));
    }

    @Test
    @org.junit.jupiter.api.DisplayName("should reject null name")
    void shouldRejectNullName() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> DisplayName.of(null));
        assertTrue(exception.getMessage().contains("Display name"));
    }

    @Test
    @org.junit.jupiter.api.DisplayName("should handle special characters correctly")
    void shouldHandleSpecialCharacters() {
        DisplayName name = DisplayName.of("John's Doe-Jones");
        assertEquals("John's Doe-Jones", name.getValue());
    }
}