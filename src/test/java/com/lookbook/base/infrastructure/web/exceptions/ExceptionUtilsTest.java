package com.lookbook.base.infrastructure.web.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import com.lookbook.base.domain.exceptions.DomainException;
import com.lookbook.base.domain.exceptions.EntityNotFoundException;
import com.lookbook.base.domain.exceptions.ValidationException;
import com.lookbook.base.infrastructure.web.response.ErrorResponse;
import com.lookbook.base.infrastructure.web.response.ValidationErrorResponse;

class ExceptionUtilsTest {

    @Test
    void mapDomainExceptionToStatus_shouldMapEntityNotFoundToNotFound() {
        // Given
        EntityNotFoundException exception = new EntityNotFoundException(String.class, "123");

        // When
        HttpStatus status = ExceptionUtils.mapDomainExceptionToStatus(exception);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, status);
    }

    @Test
    void mapDomainExceptionToStatus_shouldMapValidationExceptionToBadRequest() {
        // Given
        ValidationException exception = new ValidationException("Validation failed");

        // When
        HttpStatus status = ExceptionUtils.mapDomainExceptionToStatus(exception);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, status);
    }

    @Test
    void mapDomainExceptionToStatus_shouldMapGenericDomainExceptionToInternalServerError() {
        // Given
        DomainException exception = new DomainException("DOMAIN_ERROR", "Generic domain error") {
        };

        // When
        HttpStatus status = ExceptionUtils.mapDomainExceptionToStatus(exception);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, status);
    }

    @Test
    void createErrorResponse_shouldCreateResponseWithDomainExceptionDetails() {
        // Given
        DomainException exception = new DomainException("DOMAIN_ERROR", "Domain error") {
        };
        String path = "/api/test";

        // When
        ErrorResponse response = ExceptionUtils.createErrorResponse(exception, path);

        // Then
        assertEquals("error", response.getStatus());
        assertEquals("DOMAIN_ERROR", response.getCode());
        assertEquals("Domain error", response.getMessage());
        assertEquals(path, response.getPath());
        assertNotNull(response.getTimestamp());
    }

    @Test
    void extractValidationErrors_shouldExtractFieldErrorsFromException() {
        // Given
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        FieldError usernameError = new FieldError("user", "username", "Username is required");
        FieldError emailError = new FieldError("user", "email", "Email is invalid");

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(Arrays.asList(usernameError, emailError));

        // When
        Map<String, String> errors = ExceptionUtils.extractValidationErrors(ex);

        // Then
        assertNotNull(errors);
        assertEquals(2, errors.size());
        assertEquals("Username is required", errors.get("username"));
        assertEquals("Email is invalid", errors.get("email"));
    }

    @Test
    void createValidationErrorResponse_shouldCreateResponseWithFieldErrors() {
        // Given
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        WebRequest request = mock(WebRequest.class);

        FieldError usernameError = new FieldError("user", "username", "Username is required");
        FieldError emailError = new FieldError("user", "email", "Email is invalid");

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(Arrays.asList(usernameError, emailError));
        when(request.getDescription(false)).thenReturn("uri=/api/users");

        // When
        ValidationErrorResponse response = ExceptionUtils.createValidationErrorResponse(ex, request);

        // Then
        assertEquals("error", response.getStatus());
        assertEquals("VALIDATION_ERROR", response.getCode());
        assertEquals("Validation failed", response.getMessage());
        assertEquals("/api/users", response.getPath());

        Map<String, String> errors = response.getErrors();
        assertNotNull(errors);
        assertEquals(2, errors.size());
        assertEquals("Username is required", errors.get("username"));
        assertEquals("Email is invalid", errors.get("email"));
    }

    @Test
    void extractPathFromRequest_shouldExtractPathFromWebRequest() {
        // Given
        WebRequest request = mock(WebRequest.class);
        when(request.getDescription(false)).thenReturn("uri=/api/users");

        // When
        String path = ExceptionUtils.extractPathFromRequest(request);

        // Then
        assertEquals("/api/users", path);
    }
}