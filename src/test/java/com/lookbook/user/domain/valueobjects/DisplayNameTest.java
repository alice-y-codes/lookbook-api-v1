package com.lookbook.user.domain.valueobjects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.lookbook.base.domain.exceptions.ValidationException;

@DisplayName("DisplayName")
class DisplayNameTest {

    @Test
    @DisplayName("should create with valid name")
    void shouldCreateWithValidName() {
        com.lookbook.user.domain.valueobjects.DisplayName name = com.lookbook.user.domain.valueobjects.DisplayName
                .of("John Doe");
        assertEquals("John Doe", name.getValue());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "", // empty
            " ", // whitespace
            "J", // too short
            "This name is way too long and exceeds the maximum length requirement", // too long
            "John@Doe", // invalid characters
            "John#Doe", // invalid characters
            "John$Doe" // invalid characters
    })
    @DisplayName("should reject invalid names")
    void shouldRejectInvalidNames(String invalidName) {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> com.lookbook.user.domain.valueobjects.DisplayName.of(invalidName));
        assertTrue(exception.getMessage().contains("Display name"));
    }

    @Test
    @DisplayName("should reject null name")
    void shouldRejectNullName() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> com.lookbook.user.domain.valueobjects.DisplayName.of(null));
        assertTrue(exception.getMessage().contains("Display name"));
    }

    @Test
    @DisplayName("should handle special characters correctly")
    void shouldHandleSpecialCharacters() {
        com.lookbook.user.domain.valueobjects.DisplayName name = com.lookbook.user.domain.valueobjects.DisplayName
                .of("John Doe, Jr.");
        assertEquals("John Doe, Jr.", name.getValue());
    }
}