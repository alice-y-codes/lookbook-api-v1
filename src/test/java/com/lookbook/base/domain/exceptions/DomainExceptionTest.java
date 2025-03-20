package com.lookbook.base.domain.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

/**
 * Tests for the domain exception hierarchy.
 */
class DomainExceptionTest {

    /**
     * Concrete implementation of DomainException for testing.
     */
    private static class TestDomainException extends DomainException {
        public TestDomainException(String message) {
            super("TEST_ERR", message);
        }

        public TestDomainException(String message, Throwable cause) {
            super("TEST_ERR", message, cause);
        }
    }

    @Test
    void domainException_WithMessageAndCode_ShouldSetBoth() {
        String message = "Test domain error";
        TestDomainException exception = new TestDomainException(message);

        assertEquals(message, exception.getMessage());
        assertEquals("TEST_ERR", exception.getErrorCode());
    }

    @Test
    void domainException_WithMessageAndCause_ShouldSetBoth() {
        String message = "Test domain error";
        RuntimeException cause = new RuntimeException("Original error");
        TestDomainException exception = new TestDomainException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals("TEST_ERR", exception.getErrorCode());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void validationException_WithSingleViolation_ShouldSetMessage() {
        String message = "Validation failed";
        ValidationException exception = new ValidationException(message);

        assertEquals(message, exception.getMessage());
        assertEquals("VAL_ERR", exception.getErrorCode());
        assertEquals(1, exception.getViolations().size());
        assertEquals(message, exception.getViolations().get(0));
        assertTrue(exception instanceof DomainException);
    }

    @Test
    void validationException_WithMultipleViolations_ShouldSetAllViolations() {
        List<String> violations = Arrays.asList(
                "Error 1",
                "Error 2",
                "Error 3");
        ValidationException exception = new ValidationException(violations);

        assertTrue(exception.getMessage().contains("3 violations"));
        assertEquals("VAL_ERR", exception.getErrorCode());
        assertEquals(violations, exception.getViolations());
        assertTrue(exception instanceof DomainException);
    }

    @Test
    void entityNotFoundException_WithTypeAndId_ShouldFormatMessage() {
        Class<?> entityType = String.class;
        UUID entityId = UUID.randomUUID();
        EntityNotFoundException exception = new EntityNotFoundException(entityType, entityId);

        assertEquals("ENT_NOT_FOUND", exception.getErrorCode());
        assertTrue(exception.getMessage().contains("String"));
        assertTrue(exception.getMessage().contains(entityId.toString()));
        assertEquals(entityType, exception.getEntityType());
        assertEquals(entityId, exception.getEntityId());
        assertTrue(exception instanceof DomainException);
    }

    @Test
    void entityNotFoundException_WithTypeAndCustomMessage_ShouldUseCustomMessage() {
        Class<?> entityType = Integer.class;
        String entityId = "123";
        String customMessage = "Could not find the specified entity";
        EntityNotFoundException exception = new EntityNotFoundException(entityType, entityId, customMessage);

        assertEquals("ENT_NOT_FOUND", exception.getErrorCode());
        assertEquals(customMessage, exception.getMessage());
        assertEquals(entityType, exception.getEntityType());
        assertEquals(entityId, exception.getEntityId());
        assertTrue(exception instanceof DomainException);
    }
}