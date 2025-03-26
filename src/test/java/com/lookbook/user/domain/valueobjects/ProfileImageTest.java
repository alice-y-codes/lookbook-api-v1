package com.lookbook.user.domain.valueobjects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("ProfileImage")
class ProfileImageTest {

    private static final URI VALID_URL = URI.create("https://example.com/profile.jpg");
    private static final int VALID_WIDTH = 800;
    private static final int VALID_HEIGHT = 600;
    private static final String VALID_FORMAT = "jpg";
    private static final long VALID_SIZE = 1024 * 1024; // 1MB

    @Test
    @DisplayName("should create with valid parameters")
    void shouldCreateWithValidParameters() {
        ProfileImage image = ProfileImage.of(VALID_URL, VALID_WIDTH, VALID_HEIGHT, VALID_FORMAT, VALID_SIZE);

        assertEquals(VALID_URL, image.getUrl());
        assertEquals(VALID_WIDTH, image.getWidth());
        assertEquals(VALID_HEIGHT, image.getHeight());
        assertEquals(VALID_FORMAT, image.getFormat());
        assertEquals(VALID_SIZE, image.getSizeBytes());
    }

    @Test
    @DisplayName("should reject null URL")
    void shouldRejectNullUrl() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> ProfileImage.of(null, VALID_WIDTH, VALID_HEIGHT, VALID_FORMAT, VALID_SIZE));
        assertTrue(exception.getMessage().contains("Profile image URL cannot be null"));
    }

    @ParameterizedTest
    @MethodSource("invalidDimensions")
    @DisplayName("should reject invalid dimensions")
    void shouldRejectInvalidDimensions(int width, int height, String expectedError) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> ProfileImage.of(VALID_URL, width, height, VALID_FORMAT, VALID_SIZE));
        assertTrue(exception.getMessage().contains(expectedError));
    }

    private static List<Arguments> invalidDimensions() {
        return Arrays.asList(
                Arguments.of(50, VALID_HEIGHT, "Image width must be between 100 and 2000 pixels"),
                Arguments.of(2500, VALID_HEIGHT, "Image width must be between 100 and 2000 pixels"),
                Arguments.of(VALID_WIDTH, 50, "Image height must be between 100 and 2000 pixels"),
                Arguments.of(VALID_WIDTH, 2500, "Image height must be between 100 and 2000 pixels"));
    }

    @ParameterizedTest
    @MethodSource("invalidFormats")
    @DisplayName("should reject invalid formats")
    void shouldRejectInvalidFormats(String format) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> ProfileImage.of(VALID_URL, VALID_WIDTH, VALID_HEIGHT, format, VALID_SIZE));
        assertTrue(exception.getMessage().contains("Image format must be one of"));
    }

    private static List<String> invalidFormats() {
        return Arrays.asList("", " ", "webp", "bmp", "tiff", null);
    }

    @ParameterizedTest
    @MethodSource("invalidSizes")
    @DisplayName("should reject invalid sizes")
    void shouldRejectInvalidSizes(long size, String expectedError) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> ProfileImage.of(VALID_URL, VALID_WIDTH, VALID_HEIGHT, VALID_FORMAT, size));
        assertTrue(exception.getMessage().contains(expectedError));
    }

    private static List<Arguments> invalidSizes() {
        return Arrays.asList(
                Arguments.of(0L, "Image size must be between 1 and 5242880 bytes"),
                Arguments.of(-1L, "Image size must be between 1 and 5242880 bytes"),
                Arguments.of(6 * 1024 * 1024L, "Image size must be between 1 and 5242880 bytes"));
    }
}