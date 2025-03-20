package com.lookbook.base.domain.exceptions;

/**
 * Base exception for all domain exceptions.
 * Domain exceptions represent business rule violations and invariant failures.
 */
public abstract class DomainException extends RuntimeException {

    private final String errorCode;

    /**
     * Creates a new domain exception with the specified error code and message.
     *
     * @param errorCode A unique code for this type of error
     * @param message   A human-readable error message
     */
    protected DomainException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Creates a new domain exception with the specified error code, message, and
     * cause.
     *
     * @param errorCode A unique code for this type of error
     * @param message   A human-readable error message
     * @param cause     The underlying cause of the exception
     */
    protected DomainException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    /**
     * Gets the error code for this exception.
     *
     * @return The error code
     */
    public String getErrorCode() {
        return errorCode;
    }
}