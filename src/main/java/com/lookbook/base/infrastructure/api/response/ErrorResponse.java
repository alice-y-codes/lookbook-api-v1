package com.lookbook.base.infrastructure.api.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Standard error response format for API errors.
 */
public class ErrorResponse {

    private final String status = "error";
    private String code;
    private String message;
    private String path;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime timestamp;

    protected ErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Creates a new error response with the given parameters.
     * 
     * @param code    Error code
     * @param message Error message
     * @param path    API path that generated the error
     * @return The error response
     */
    public static ErrorResponse of(String code, String message, String path) {
        ErrorResponse response = new ErrorResponse();
        response.code = code;
        response.message = message;
        response.path = path;
        return response;
    }

    /**
     * Creates a new error response with only a message.
     * 
     * @param message Error message
     * @param path    API path that generated the error
     * @return The error response
     */
    public static ErrorResponse of(String message, String path) {
        return of("GENERIC_ERROR", message, path);
    }

    /**
     * Creates a new error response for a domain exception.
     * 
     * @param errorCode Domain error code
     * @param message   Error message
     * @param path      API path that generated the error
     * @return The error response
     */
    public static ErrorResponse fromDomainError(String errorCode, String message, String path) {
        return of(errorCode, message, path);
    }

    // Getters and setters

    public String getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}