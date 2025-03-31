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

import com.lookbook.base.domain.exceptions.ValidationException;

@DisplayName("ProfileImage")
class ProfileImageTest {

        private static final URI VALID_URL = URI.create("https://example.com/profile.jpg");
        private static final int VALID_WIDTH = 800;
        private static final int VALID_HEIGHT = 600;
        private static final String VALID_FORMAT = "jpg";
        private static final long VALID_SIZE = 1024 * 1024; // 1MB

        @Test
        @DisplayName("should create with valid image data")
        void shouldCreateWithValidImageData() {
                ProfileImage image = ProfileImage.of(
                                URI.create("https://example.com/image.jpg"),
                                800, 600, "jpg", 1024 * 1024);
                assertEquals(URI.create("https://example.com/image.jpg"), image.getUrl());
                assertEquals(800, image.getWidth());
                assertEquals(600, image.getHeight());
                assertEquals("jpg", image.getFormat());
                assertEquals(1024 * 1024, image.getSizeBytes());
        }

        @Test
        @DisplayName("should reject invalid dimensions")
        void shouldRejectInvalidDimensions() {
                ValidationException exception = assertThrows(
                                ValidationException.class,
                                () -> ProfileImage.of(
                                                URI.create("https://example.com/image.jpg"),
                                                50, 600, "jpg", 1024 * 1024));
                assertTrue(exception.getMessage().contains("Image width must be between 100 and 2000 pixels"));

                exception = assertThrows(
                                ValidationException.class,
                                () -> ProfileImage.of(
                                                URI.create("https://example.com/image.jpg"),
                                                800, 50, "jpg", 1024 * 1024));
                assertTrue(exception.getMessage().contains("Image height must be between 100 and 2000 pixels"));

                exception = assertThrows(
                                ValidationException.class,
                                () -> ProfileImage.of(
                                                URI.create("https://example.com/image.jpg"),
                                                2500, 600, "jpg", 1024 * 1024));
                assertTrue(exception.getMessage().contains("Image width must be between 100 and 2000 pixels"));

                exception = assertThrows(
                                ValidationException.class,
                                () -> ProfileImage.of(
                                                URI.create("https://example.com/image.jpg"),
                                                800, 2500, "jpg", 1024 * 1024));
                assertTrue(exception.getMessage().contains("Image height must be between 100 and 2000 pixels"));
        }

        @Test
        @DisplayName("should reject invalid formats")
        void shouldRejectInvalidFormats() {
                ValidationException exception = assertThrows(
                                ValidationException.class,
                                () -> ProfileImage.of(
                                                URI.create("https://example.com/image.webp"),
                                                800, 600, "webp", 1024 * 1024));
                assertTrue(exception.getMessage().contains("Image format must be one of: jpg, jpeg, png, gif"));
        }

        @ParameterizedTest
        @MethodSource("emptyFormats")
        @DisplayName("should reject empty formats")
        void shouldRejectEmptyFormats(String format) {
                ValidationException exception = assertThrows(
                                ValidationException.class,
                                () -> ProfileImage.of(VALID_URL, VALID_WIDTH, VALID_HEIGHT, format, VALID_SIZE));
                assertTrue(exception.getMessage().contains("Image format cannot be empty"),
                                "Expected error message to contain 'Image format cannot be empty' but got: "
                                                + exception.getMessage());
        }

        private static List<String> emptyFormats() {
                return Arrays.asList("", " ", null);
        }

        @ParameterizedTest
        @MethodSource("invalidFormats")
        @DisplayName("should reject unsupported formats")
        void shouldRejectUnsupportedFormats(String format) {
                ValidationException exception = assertThrows(
                                ValidationException.class,
                                () -> ProfileImage.of(VALID_URL, VALID_WIDTH, VALID_HEIGHT, format, VALID_SIZE));
                assertTrue(exception.getMessage().contains("Image format must be one of: jpg, jpeg, png, gif"),
                                "Expected error message to contain format validation message but got: "
                                                + exception.getMessage());
        }

        private static List<String> invalidFormats() {
                return Arrays.asList("webp", "bmp", "tiff");
        }

        @ParameterizedTest
        @MethodSource("invalidSizes")
        @DisplayName("should reject invalid sizes")
        void shouldRejectInvalidSizes(long size, String expectedError) {
                ValidationException exception = assertThrows(
                                ValidationException.class,
                                () -> ProfileImage.of(VALID_URL, VALID_WIDTH, VALID_HEIGHT, VALID_FORMAT, size));
                assertTrue(exception.getMessage().contains(expectedError));
        }

        private static List<Arguments> invalidSizes() {
                return Arrays.asList(
                                Arguments.of(0L, "Image size must be between 1 and 5242880 bytes"),
                                Arguments.of(-1L, "Image size must be between 1 and 5242880 bytes"),
                                Arguments.of(6 * 1024 * 1024L, "Image size must be between 1 and 5242880 bytes"));
        }

        @Test
        @DisplayName("should reject null URL")
        void shouldRejectNullUrl() {
                ValidationException exception = assertThrows(
                                ValidationException.class,
                                () -> ProfileImage.of(
                                                null,
                                                800, 600, "jpg", 1024 * 1024));
                assertTrue(exception.getMessage().contains("Profile image URL cannot be null"));
        }

        @ParameterizedTest
        @MethodSource("invalidDimensions")
        @DisplayName("should reject invalid dimensions")
        void shouldRejectInvalidDimensions(int width, int height, String expectedError) {
                ValidationException exception = assertThrows(
                                ValidationException.class,
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
}