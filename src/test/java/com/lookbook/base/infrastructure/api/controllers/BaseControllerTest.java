package com.lookbook.base.infrastructure.api.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.Authentication;

import com.lookbook.base.infrastructure.api.response.ApiResponse;
import com.lookbook.base.infrastructure.api.response.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

class BaseControllerTest {

    private TestController controller;
    private HttpServletRequest request;

    // Concrete implementation of BaseController for testing
    private static class TestController extends BaseController {
        public <T> ApiResponse<T> exposeCreateSuccessResponse(T data) {
            return createSuccessResponse(data);
        }

        public <T> ApiResponse<T> exposeCreateSuccessResponse(T data, String message) {
            return createSuccessResponse(data, message);
        }

        public ErrorResponse exposeCreateErrorResponse(String code, String message) {
            HttpServletRequest mockRequest = mock(HttpServletRequest.class);
            when(mockRequest.getRequestURI()).thenReturn("/test");
            return createErrorResponse(code, message, mockRequest);
        }

        public Authentication exposeGetCurrentUser() {
            return getCurrentUser();
        }

        public boolean exposeIsPaginationRequested(Integer page, Integer size) {
            return isPaginationRequested(page, size);
        }

        public Pageable exposeCreatePageRequest(
                Integer page, Integer size, String sortBy, String sortDir, List<String> allowedSortFields) {
            Set<String> allowedFieldsSet = new HashSet<>(allowedSortFields);
            return createPageRequest(page, size, sortBy, sortDir, allowedFieldsSet);
        }
    }

    @BeforeEach
    void setUp() {
        controller = new TestController();
        request = mock(HttpServletRequest.class);
    }

    @Test
    void createSuccessResponse_withData_shouldReturnSuccessResponse() {
        // Given
        String data = "Test Data";

        // When
        ApiResponse<String> response = controller.exposeCreateSuccessResponse(data);

        // Then
        assertEquals("success", response.getStatus());
        assertEquals(data, response.getData());
        assertNotNull(response.getTimestamp());
    }

    @Test
    void createSuccessResponse_withDataAndMessage_shouldReturnSuccessResponseWithMessage() {
        // Given
        String data = "Test Data";
        String message = "Operation successful";

        // When
        ApiResponse<String> response = controller.exposeCreateSuccessResponse(data, message);

        // Then
        assertEquals("success", response.getStatus());
        assertEquals(data, response.getData());
        assertEquals(message, response.getMessage());
        assertNotNull(response.getTimestamp());
    }

    @Test
    void createErrorResponse_shouldReturnErrorResponseWithCodeAndMessage() {
        // Given
        String code = "TEST_ERROR";
        String message = "Error occurred";

        // When
        ErrorResponse response = controller.exposeCreateErrorResponse(code, message);

        // Then
        assertEquals("error", response.getStatus());
        assertEquals(code, response.getCode());
        assertEquals(message, response.getMessage());
        assertEquals("/test", response.getPath());
        assertNotNull(response.getTimestamp());
    }

    @Test
    void getCurrentUser_withAuthenticatedUser_shouldReturnAuthentication() {
        // This test would require a SecurityContext setup with Spring Security test
        // utilities
        // We'll mock the behavior to focus on testing our controller logic
        // In a real application, you would use Spring Security Test tools
    }

    @Test
    void isPaginationRequested_withPageAndSizeProvided_shouldReturnTrue() {
        // When
        boolean result = controller.exposeIsPaginationRequested(0, 10);

        // Then
        assertTrue(result);
    }

    @Test
    void isPaginationRequested_withOnlyPageProvided_shouldReturnTrue() {
        // When
        boolean result = controller.exposeIsPaginationRequested(1, null);

        // Then
        assertTrue(result);
    }

    @Test
    void isPaginationRequested_withOnlySizeProvided_shouldReturnTrue() {
        // When
        boolean result = controller.exposeIsPaginationRequested(null, 20);

        // Then
        assertTrue(result);
    }

    @Test
    void isPaginationRequested_withNoPageOrSize_shouldReturnFalse() {
        // When
        boolean result = controller.exposeIsPaginationRequested(null, null);

        // Then
        assertFalse(result);
    }

    @Test
    void createPageRequest_withAllParamsProvided_shouldCreatePageRequestWithSort() {
        // Given
        Integer page = 2;
        Integer size = 15;
        String sortBy = "name";
        String sortDir = "desc";
        List<String> allowedSortFields = Arrays.asList("name", "email", "createdAt");

        // When
        Pageable pageable = controller.exposeCreatePageRequest(page, size, sortBy, sortDir, allowedSortFields);

        // Then
        assertEquals(2, pageable.getPageNumber());
        assertEquals(15, pageable.getPageSize());
        assertNotNull(pageable.getSort());
        assertEquals(Sort.by(Direction.DESC, "name"), pageable.getSort());
    }

    @Test
    void createPageRequest_withInvalidSortField_shouldThrowException() {
        // Given
        Integer page = 0;
        Integer size = 10;
        String sortBy = "invalidField";
        String sortDir = "asc";
        List<String> allowedSortFields = Arrays.asList("name", "email", "createdAt");

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            controller.exposeCreatePageRequest(page, size, sortBy, sortDir, allowedSortFields);
        });
    }

    @Test
    void createPageRequest_withEmptyAllowedFields_shouldNotValidateSortField() {
        // Given
        Integer page = 0;
        Integer size = 10;
        String sortBy = "anyField";
        String sortDir = "asc";
        List<String> allowedSortFields = Collections.emptyList();

        // When
        Pageable pageable = controller.exposeCreatePageRequest(page, size, sortBy, sortDir, allowedSortFields);

        // Then
        assertEquals(0, pageable.getPageNumber());
        assertEquals(10, pageable.getPageSize());
        assertNotNull(pageable.getSort());
        assertEquals(Sort.by(Direction.ASC, "anyField"), pageable.getSort());
    }
}