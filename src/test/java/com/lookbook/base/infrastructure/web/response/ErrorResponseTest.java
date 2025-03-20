package com.lookbook.base.infrastructure.web.response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

class ErrorResponseTest {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @Test
    void of_withCodeMessageAndPath_shouldReturnErrorResponse() {
        // Given
        String code = "TEST_CODE";
        String message = "Test error message";
        String path = "/api/test";

        // When
        ErrorResponse response = ErrorResponse.of(code, message, path);

        // Then
        assertEquals("error", response.getStatus());
        assertEquals(code, response.getCode());
        assertEquals(message, response.getMessage());
        assertEquals(path, response.getPath());
        assertNotNull(response.getTimestamp());
    }

    @Test
    void of_withMessageAndPath_shouldReturnErrorResponseWithGenericCode() {
        // Given
        String message = "Test error message";
        String path = "/api/test";

        // When
        ErrorResponse response = ErrorResponse.of(message, path);

        // Then
        assertEquals("error", response.getStatus());
        assertEquals("GENERIC_ERROR", response.getCode());
        assertEquals(message, response.getMessage());
        assertEquals(path, response.getPath());
        assertNotNull(response.getTimestamp());
    }

    @Test
    void fromDomainError_shouldReturnErrorResponseWithDomainErrorDetails() {
        // Given
        String errorCode = "DOMAIN_ERROR";
        String message = "Domain error occurred";
        String path = "/api/domain";

        // When
        ErrorResponse response = ErrorResponse.fromDomainError(errorCode, message, path);

        // Then
        assertEquals("error", response.getStatus());
        assertEquals(errorCode, response.getCode());
        assertEquals(message, response.getMessage());
        assertEquals(path, response.getPath());
        assertNotNull(response.getTimestamp());
    }

    @Test
    void serialization_shouldProduceCorrectJson() throws JsonProcessingException {
        // Given
        LocalDateTime fixedTime = LocalDateTime.of(2023, 4, 1, 10, 30, 0);
        ErrorResponse response = ErrorResponse.of("TEST_CODE", "Test error message", "/api/test");
        response.setTimestamp(fixedTime);

        // When
        String json = objectMapper.writeValueAsString(response);

        // Then
        assertNotNull(json);
        // Parse the expected and actual JSON to avoid whitespace issues
        Object expectedJson = objectMapper.readValue(
                "{\"status\":\"error\",\"code\":\"TEST_CODE\",\"message\":\"Test error message\",\"path\":\"/api/test\",\"timestamp\":\"2023-04-01T10:30:00.000\"}",
                Object.class);
        Object actualJson = objectMapper.readValue(json, Object.class);
        assertEquals(expectedJson, actualJson);
    }

    @Test
    void deserialization_shouldRecreateObject() throws JsonProcessingException {
        // Given
        String json = "{\"status\":\"error\",\"code\":\"TEST_CODE\",\"message\":\"Test error message\",\"path\":\"/api/test\",\"timestamp\":\"2023-04-01T10:30:00.000\"}";

        // When
        ErrorResponse response = objectMapper.readValue(json, ErrorResponse.class);

        // Then
        assertEquals("error", response.getStatus());
        assertEquals("TEST_CODE", response.getCode());
        assertEquals("Test error message", response.getMessage());
        assertEquals("/api/test", response.getPath());
        assertNotNull(response.getTimestamp());
    }
}