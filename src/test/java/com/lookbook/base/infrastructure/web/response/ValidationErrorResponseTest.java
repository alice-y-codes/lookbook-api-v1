package com.lookbook.base.infrastructure.web.response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

class ValidationErrorResponseTest {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @Test
    void of_withMessageAndPath_shouldReturnValidationErrorResponse() {
        // Given
        String message = "Validation failed";
        String path = "/api/test";

        // When
        ValidationErrorResponse response = ValidationErrorResponse.of(message, path);

        // Then
        assertEquals("error", response.getStatus());
        assertEquals("VALIDATION_ERROR", response.getCode());
        assertEquals(message, response.getMessage());
        assertEquals(path, response.getPath());
        assertNotNull(response.getTimestamp());
        assertNotNull(response.getErrors());
        assertTrue(response.getErrors().isEmpty());
    }

    @Test
    void addFieldError_shouldAddErrorToMap() {
        // Given
        ValidationErrorResponse response = ValidationErrorResponse.of("Validation failed", "/api/test");
        String field = "username";
        String errorMessage = "Username is required";

        // When
        response.addFieldError(field, errorMessage);

        // Then
        Map<String, String> errors = response.getErrors();
        assertNotNull(errors);
        assertEquals(1, errors.size());
        assertEquals(errorMessage, errors.get(field));
    }

    @Test
    void addFieldErrors_shouldAddMultipleErrorsToMap() {
        // Given
        ValidationErrorResponse response = ValidationErrorResponse.of("Validation failed", "/api/test");

        Map<String, String> fieldErrors = new HashMap<>();
        fieldErrors.put("username", "Username is required");
        fieldErrors.put("email", "Email is invalid");

        // When
        response.addFieldErrors(fieldErrors);

        // Then
        Map<String, String> errors = response.getErrors();
        assertNotNull(errors);
        assertEquals(2, errors.size());
        assertEquals("Username is required", errors.get("username"));
        assertEquals("Email is invalid", errors.get("email"));
    }

    @Test
    void fromMethodArgumentNotValidException_shouldCreateResponseWithFieldErrors() {
        // Given
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        FieldError usernameError = new FieldError("user", "username", "Username is required");
        FieldError emailError = new FieldError("user", "email", "Email is invalid");

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(java.util.Arrays.asList(usernameError, emailError));

        String path = "/api/users";

        // When
        ValidationErrorResponse response = ValidationErrorResponse.fromMethodArgumentNotValidException(ex, path);

        // Then
        assertEquals("error", response.getStatus());
        assertEquals("VALIDATION_ERROR", response.getCode());
        assertEquals("Validation failed", response.getMessage());
        assertEquals(path, response.getPath());

        Map<String, String> errors = response.getErrors();
        assertNotNull(errors);
        assertEquals(2, errors.size());
        assertEquals("Username is required", errors.get("username"));
        assertEquals("Email is invalid", errors.get("email"));
    }

    @Test
    void serialization_shouldProduceCorrectJson() throws JsonProcessingException {
        // Given
        LocalDateTime fixedTime = LocalDateTime.of(2023, 4, 1, 10, 30, 0);
        ValidationErrorResponse response = ValidationErrorResponse.of("Validation failed", "/api/test");
        response.setTimestamp(fixedTime);
        response.addFieldError("username", "Username is required");
        response.addFieldError("email", "Email is invalid");

        // When
        String json = objectMapper.writeValueAsString(response);

        // Then
        assertNotNull(json);

        // Check for all expected elements in the json
        assertTrue(json.contains("\"status\":\"error\""));
        assertTrue(json.contains("\"code\":\"VALIDATION_ERROR\""));
        assertTrue(json.contains("\"message\":\"Validation failed\""));
        assertTrue(json.contains("\"path\":\"/api/test\""));
        assertTrue(json.contains("\"timestamp\":\"2023-04-01T10:30:00.000\""));
        assertTrue(json.contains("\"errors\":{"));
        assertTrue(json.contains("\"username\":\"Username is required\""));
        assertTrue(json.contains("\"email\":\"Email is invalid\""));
    }

    @Test
    void deserialization_shouldRecreateObject() throws JsonProcessingException {
        // Given
        String json = "{\"status\":\"error\",\"code\":\"VALIDATION_ERROR\",\"message\":\"Validation failed\",\"path\":\"/api/test\",\"timestamp\":\"2023-04-01T10:30:00.000\",\"errors\":{\"username\":\"Username is required\",\"email\":\"Email is invalid\"}}";

        // When
        ValidationErrorResponse response = objectMapper.readValue(json, ValidationErrorResponse.class);

        // Then
        assertEquals("error", response.getStatus());
        assertEquals("VALIDATION_ERROR", response.getCode());
        assertEquals("Validation failed", response.getMessage());
        assertEquals("/api/test", response.getPath());

        Map<String, String> errors = response.getErrors();
        assertNotNull(errors);
        assertEquals(2, errors.size());
        assertEquals("Username is required", errors.get("username"));
        assertEquals("Email is invalid", errors.get("email"));
    }
}