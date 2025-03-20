package com.lookbook.base.infrastructure.web.response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

class ApiResponseTest {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @Test
    void success_withNoParameters_shouldReturnSuccessResponse() {
        // When
        ApiResponse<Object> response = ApiResponse.success();

        // Then
        assertEquals("success", response.getStatus());
        assertNull(response.getData());
        assertNull(response.getMessage());
        assertNotNull(response.getTimestamp());
        assertNotNull(response.getMeta());
    }

    @Test
    void success_withData_shouldReturnSuccessResponseWithData() {
        // Given
        String testData = "Test Data";

        // When
        ApiResponse<String> response = ApiResponse.success(testData);

        // Then
        assertEquals("success", response.getStatus());
        assertEquals(testData, response.getData());
        assertNull(response.getMessage());
        assertNotNull(response.getTimestamp());
    }

    @Test
    void success_withDataAndMessage_shouldReturnSuccessResponseWithDataAndMessage() {
        // Given
        String testData = "Test Data";
        String testMessage = "Test Message";

        // When
        ApiResponse<String> response = ApiResponse.success(testData, testMessage);

        // Then
        assertEquals("success", response.getStatus());
        assertEquals(testData, response.getData());
        assertEquals(testMessage, response.getMessage());
        assertNotNull(response.getTimestamp());
    }

    @Test
    void error_withMessage_shouldReturnErrorResponse() {
        // Given
        String errorMessage = "Error Message";

        // When
        ApiResponse<Object> response = ApiResponse.error(errorMessage);

        // Then
        assertEquals("error", response.getStatus());
        assertEquals(errorMessage, response.getMessage());
        assertNull(response.getData());
        assertNotNull(response.getTimestamp());
    }

    @Test
    void addMetadata_shouldAddMetadataToResponse() {
        // Given
        ApiResponse<String> response = ApiResponse.success("data");
        String metaKey = "test-key";
        String metaValue = "test-value";

        // When
        response.addMetadata(metaKey, metaValue);

        // Then
        Map<String, Object> meta = response.getMeta();
        assertNotNull(meta);
        assertEquals(metaValue, meta.get(metaKey));
    }

    @Test
    void serialization_shouldProduceCorrectJson() throws JsonProcessingException {
        // Given
        LocalDateTime fixedTime = LocalDateTime.of(2023, 4, 1, 10, 30, 0);
        ApiResponse<String> response = ApiResponse.success("test-data", "test-message");
        response.setTimestamp(fixedTime);
        response.addMetadata("count", 42);

        // When
        String json = objectMapper.writeValueAsString(response);

        // Then
        assertNotNull(json);
        assertEquals(
                "{\"status\":\"success\",\"data\":\"test-data\",\"message\":\"test-message\",\"timestamp\":\"2023-04-01T10:30:00.000\",\"meta\":{\"count\":42}}",
                json.replace(" ", "").replace("\n", "").replace("\r", ""));
    }

    @Test
    void deserialization_shouldRecreateObject() throws JsonProcessingException {
        // Given
        String json = "{\"status\":\"success\",\"data\":\"test-data\",\"message\":\"test-message\",\"timestamp\":\"2023-04-01T10:30:00.000\",\"meta\":{\"count\":42}}";

        // When
        ApiResponse<?> response = objectMapper.readValue(json, ApiResponse.class);

        // Then
        assertEquals("success", response.getStatus());
        assertEquals("test-data", response.getData());
        assertEquals("test-message", response.getMessage());
        assertNotNull(response.getTimestamp());
        assertEquals(42, response.getMeta().get("count"));
    }
}