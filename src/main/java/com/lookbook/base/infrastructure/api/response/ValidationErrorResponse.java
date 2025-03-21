package com.lookbook.base.infrastructure.api.response;

import java.util.HashMap;
import java.util.Map;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Specialized error response for validation failures with detailed field
 * errors.
 */
public class ValidationErrorResponse extends ErrorResponse {

    private static final String VALIDATION_ERROR_CODE = "VALIDATION_ERROR";

    @JsonInclude(Include.NON_EMPTY)
    private Map<String, String> errors;

    private ValidationErrorResponse() {
        super();
        this.errors = new HashMap<>();
    }

    /**
     * Creates a validation error response from field errors.
     * 
     * @param message General validation error message
     * @param path    API path that generated the error
     * @return The validation error response
     */
    public static ValidationErrorResponse of(String message, String path) {
        ValidationErrorResponse response = new ValidationErrorResponse();
        response.setCode(VALIDATION_ERROR_CODE);
        response.setMessage(message);
        response.setPath(path);
        return response;
    }

    /**
     * Creates a validation error response from a MethodArgumentNotValidException.
     * 
     * @param ex   The validation exception
     * @param path API path that generated the error
     * @return The validation error response with field errors
     */
    public static ValidationErrorResponse fromMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, String path) {

        ValidationErrorResponse response = of("Validation failed", path);

        BindingResult result = ex.getBindingResult();

        for (FieldError fieldError : result.getFieldErrors()) {
            response.addFieldError(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return response;
    }

    /**
     * Adds a field error to the response.
     * 
     * @param field   The field name that failed validation
     * @param message The validation error message
     * @return This response for method chaining
     */
    public ValidationErrorResponse addFieldError(String field, String message) {
        this.errors.put(field, message);
        return this;
    }

    /**
     * Adds multiple field errors to the response.
     * 
     * @param fieldErrors Map of field names to error messages
     * @return This response for method chaining
     */
    public ValidationErrorResponse addFieldErrors(Map<String, String> fieldErrors) {
        this.errors.putAll(fieldErrors);
        return this;
    }

    // Getters and setters

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
}