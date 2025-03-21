package com.lookbook.base.infrastructure.api.exceptions;

import javax.security.sasl.AuthenticationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.lookbook.base.domain.exceptions.DomainException;
import com.lookbook.base.domain.exceptions.EntityNotFoundException;
import com.lookbook.base.infrastructure.api.response.ErrorResponse;
import com.lookbook.base.infrastructure.api.response.ValidationErrorResponse;

/**
 * Global exception handler for REST controllers.
 * Provides centralized exception handling across all controllers.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

        private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

        /**
         * Handles validation exceptions from request body validation.
         */
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ValidationErrorResponse> handleValidationExceptions(
                        MethodArgumentNotValidException ex, WebRequest request) {

                log.debug("Handling validation exception: {}", ex.getMessage());

                ValidationErrorResponse response = ExceptionUtils.createValidationErrorResponse(ex, request);

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(response);
        }

        /**
         * Handles domain exceptions.
         */
        @ExceptionHandler(DomainException.class)
        public ResponseEntity<ErrorResponse> handleDomainExceptions(
                        DomainException ex, WebRequest request) {

                log.debug("Handling domain exception: {}", ex.getMessage());

                HttpStatus status = ExceptionUtils.mapDomainExceptionToStatus(ex);
                String path = ExceptionUtils.extractPathFromRequest(request);
                ErrorResponse response = ExceptionUtils.createErrorResponse(ex, path);

                return ResponseEntity
                                .status(status)
                                .body(response);
        }

        /**
         * Handles entity not found exceptions.
         */
        @ExceptionHandler(EntityNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleResourceNotFound(
                        EntityNotFoundException ex, WebRequest request) {

                log.debug("Handling entity not found exception: {}", ex.getMessage());

                String path = ExceptionUtils.extractPathFromRequest(request);
                ErrorResponse response = ExceptionUtils.createErrorResponse(ex, path);

                return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(response);
        }

        /**
         * Handles authentication exceptions.
         */
        @ExceptionHandler(AuthenticationException.class)
        public ResponseEntity<ErrorResponse> handleAuthenticationExceptions(
                        AuthenticationException ex, WebRequest request) {

                log.debug("Handling authentication exception: {}", ex.getMessage());

                String path = ExceptionUtils.extractPathFromRequest(request);
                ErrorResponse response = ErrorResponse.of("AUTH_ERROR", "Authentication failed", path);

                return ResponseEntity
                                .status(HttpStatus.UNAUTHORIZED)
                                .body(response);
        }

        /**
         * Handles authorization exceptions.
         */
        @ExceptionHandler(AccessDeniedException.class)
        public ResponseEntity<ErrorResponse> handleAccessDeniedExceptions(
                        AccessDeniedException ex, WebRequest request) {

                log.debug("Handling access denied exception: {}", ex.getMessage());

                String path = ExceptionUtils.extractPathFromRequest(request);
                ErrorResponse response = ErrorResponse.of("ACCESS_DENIED", "Access denied", path);

                return ResponseEntity
                                .status(HttpStatus.FORBIDDEN)
                                .body(response);
        }

        /**
         * Handles all other exceptions.
         */
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleGeneralExceptions(
                        Exception ex, WebRequest request) {

                log.error("Unhandled exception", ex);

                String path = ExceptionUtils.extractPathFromRequest(request);
                ErrorResponse response = ErrorResponse.of(
                                "INTERNAL_ERROR",
                                "Internal server error",
                                path);

                return ResponseEntity
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(response);
        }
}