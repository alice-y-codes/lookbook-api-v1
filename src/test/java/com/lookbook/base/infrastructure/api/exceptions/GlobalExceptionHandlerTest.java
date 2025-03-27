package com.lookbook.base.infrastructure.api.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Map;

import javax.security.sasl.AuthenticationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import com.lookbook.base.domain.exceptions.DomainException;
import com.lookbook.base.domain.exceptions.EntityNotFoundException;
import com.lookbook.base.infrastructure.api.response.ErrorResponse;
import com.lookbook.base.infrastructure.api.response.ValidationErrorResponse;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
        webRequest = mock(WebRequest.class);
        when(webRequest.getDescription(false)).thenReturn("uri=/api/test");
    }

    @SuppressWarnings("null")
    @Test
    void handleValidationExceptions_shouldReturnValidationErrorResponse() {
        // Given
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        FieldError usernameError = new FieldError("user", "username", "Username is required");
        FieldError emailError = new FieldError("user", "email", "Email is invalid");

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(Arrays.asList(usernameError, emailError));

        // When
        ResponseEntity<ValidationErrorResponse> response = exceptionHandler.handleValidationExceptions(ex, webRequest);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("error", response.getBody().getStatus());
        assertEquals("VALIDATION_ERROR", response.getBody().getCode());
        assertEquals("Validation failed", response.getBody().getMessage());
        assertEquals("/api/test", response.getBody().getPath());

        Map<String, String> errors = response.getBody().getErrors();
        assertNotNull(errors);
        assertEquals(2, errors.size());
        assertEquals("Username is required", errors.get("username"));
        assertEquals("Email is invalid", errors.get("email"));
    }

    @SuppressWarnings("null")
    @Test
    void handleDomainExceptions_shouldReturnErrorResponse() {
        // Given
        DomainException ex = new DomainException("DOMAIN_CODE", "Domain exception message") {
        };

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleDomainExceptions(ex, webRequest);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("error", response.getBody().getStatus());
        assertEquals("DOMAIN_CODE", response.getBody().getCode());
        assertEquals("Domain exception message", response.getBody().getMessage());
        assertEquals("/api/test", response.getBody().getPath());
    }

    @SuppressWarnings("null")
    @Test
    void handleResourceNotFound_shouldReturnNotFoundResponse() {
        // Given
        EntityNotFoundException ex = new EntityNotFoundException(String.class, "123");

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleResourceNotFound(ex, webRequest);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("error", response.getBody().getStatus());
        assertEquals("ENT_NOT_FOUND", response.getBody().getCode());
        assertTrue(response.getBody().getMessage().contains("String with ID 123 not found"));
        assertEquals("/api/test", response.getBody().getPath());
    }

    @SuppressWarnings("null")
    @Test
    void handleAuthenticationExceptions_shouldReturnUnauthorizedResponse() {
        // Given
        AuthenticationException ex = mock(AuthenticationException.class);
        when(ex.getMessage()).thenReturn("Authentication failed");

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleAuthenticationExceptions(ex, webRequest);

        // Then
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("error", response.getBody().getStatus());
        assertEquals("AUTH_ERROR", response.getBody().getCode());
        assertEquals("Authentication failed", response.getBody().getMessage());
        assertEquals("/api/test", response.getBody().getPath());
    }

    @SuppressWarnings("null")
    @Test
    void handleAccessDeniedExceptions_shouldReturnForbiddenResponse() {
        // Given
        AccessDeniedException ex = mock(AccessDeniedException.class);
        when(ex.getMessage()).thenReturn("Access denied");

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleAccessDeniedExceptions(ex, webRequest);

        // Then
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("error", response.getBody().getStatus());
        assertEquals("ACCESS_DENIED", response.getBody().getCode());
        assertEquals("Access denied", response.getBody().getMessage());
        assertEquals("/api/test", response.getBody().getPath());
    }

    @SuppressWarnings("null")
    @Test
    void handleGeneralExceptions_shouldReturnServerErrorResponse() {
        // Given
        Exception ex = new RuntimeException("Internal server error");

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleGeneralExceptions(ex, webRequest);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("error", response.getBody().getStatus());
        assertEquals("INTERNAL_ERROR", response.getBody().getCode());
        assertEquals("Internal server error", response.getBody().getMessage());
        assertEquals("/api/test", response.getBody().getPath());
    }
}