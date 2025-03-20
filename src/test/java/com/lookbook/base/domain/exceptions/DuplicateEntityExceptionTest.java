package com.lookbook.base.domain.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link DuplicateEntityException} class.
 */
class DuplicateEntityExceptionTest {

    /**
     * A test entity class for exception testing.
     */
    private static class TestEntity {
        private String name;

        public TestEntity(String name) {
            this.name = name;
        }
    }

    @Test
    void constructor_WithTypeFieldAndValue_ShouldFormatMessage() {
        DuplicateEntityException exception = new DuplicateEntityException(
                TestEntity.class,
                "name",
                "test-name");

        assertEquals("DUP_ENT", exception.getErrorCode());
        assertTrue(exception.getMessage().contains("TestEntity"));
        assertTrue(exception.getMessage().contains("name"));
        assertTrue(exception.getMessage().contains("test-name"));
        assertEquals(TestEntity.class, exception.getEntityType());
        assertEquals("name", exception.getField());
        assertEquals("test-name", exception.getValue());
        assertTrue(exception instanceof DomainException);
    }

    @Test
    void constructor_WithCustomMessage_ShouldUseCustomMessage() {
        String customMessage = "A test entity with this name already exists";
        DuplicateEntityException exception = new DuplicateEntityException(
                TestEntity.class,
                "name",
                "test-name",
                customMessage);

        assertEquals("DUP_ENT", exception.getErrorCode());
        assertEquals(customMessage, exception.getMessage());
        assertEquals(TestEntity.class, exception.getEntityType());
        assertEquals("name", exception.getField());
        assertEquals("test-name", exception.getValue());
        assertTrue(exception instanceof DomainException);
    }
}