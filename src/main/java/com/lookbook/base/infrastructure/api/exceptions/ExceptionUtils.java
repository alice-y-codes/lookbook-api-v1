package com.lookbook.base.infrastructure.api.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import com.lookbook.base.domain.exceptions.DomainException;
import com.lookbook.base.domain.exceptions.EntityNotFoundException;
import com.lookbook.base.domain.exceptions.ValidationException;
import com.lookbook.base.infrastructure.api.response.ErrorResponse;
import com.lookbook.base.infrastructure.api.response.ValidationErrorResponse;

/**
 * Utility methods for handling exceptions in the web layer.
 */
public final class ExceptionUtils {

    private ExceptionUtils() {
        // Prevent instantiation
    }

    /**
     * Maps a domain exception to an HTTP status code.
     * 
     * @param exception The domain exception
     * @return The appropriate HTTP status
     */
    public static HttpStatus mapDomainExceptionToStatus(DomainException exception) {
        if (exception instanceof EntityNotFoundException) {
            return HttpStatus.NOT_FOUND;
        } else if (exception instanceof ValidationException) {
            return HttpStatus.BAD_REQUEST;
        } else {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    /**
     * Creates an error response from a domain exception.
     * 
     * @param exception The domain exception
     * @param path      The API path where the exception occurred
     * @return An error response with the appropriate code and message
     */
    public static ErrorResponse createErrorResponse(DomainException exception, String path) {
        return ErrorResponse.fromDomainError(
                exception.getErrorCode(),
                exception.getMessage(),
                path);
    }

    /**
     * Extracts field validation errors from a MethodArgumentNotValidException.
     * 
     * @param ex The validation exception
     * @return A map of field names to error messages
     */
    public static Map<String, String> extractValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        BindingResult result = ex.getBindingResult();

        for (FieldError fieldError : result.getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return errors;
    }

    /**
     * Creates a validation error response from a MethodArgumentNotValidException.
     * 
     * @param ex      The validation exception
     * @param request The web request
     * @return A validation error response with field errors
     */
    public static ValidationErrorResponse createValidationErrorResponse(
            MethodArgumentNotValidException ex, WebRequest request) {

        String path = request.getDescription(false).replace("uri=", "");
        return ValidationErrorResponse.fromMethodArgumentNotValidException(ex, path);
    }

    /**
     * Extracts the path from a web request.
     * 
     * @param request The web request
     * @return The request path
     */
    public static String extractPathFromRequest(WebRequest request) {
        return request.getDescription(false).replace("uri=", "");
    }
}