package com.lookbook.base.domain.validation;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.lookbook.base.domain.exceptions.ValidationException;

/**
 * Tests for the {@link SelfValidating} interface.
 */
class SelfValidatingTest {

    /**
     * A test class that always validates successfully.
     */
    private static class ValidObject implements SelfValidating<ValidObject> {
        @Override
        public ValidationResult validate() {
            return ValidationResult.valid();
        }
    }

    /**
     * A test class that always fails validation.
     */
    private static class InvalidObject implements SelfValidating<InvalidObject> {
        @Override
        public ValidationResult validate() {
            return ValidationResult.error("Test validation error");
        }
    }

    /**
     * A test class that validates based on a condition.
     */
    private static class ConditionalObject implements SelfValidating<ConditionalObject> {
        private final String value;

        public ConditionalObject(String value) {
            this.value = value;
        }

        @Override
        public ValidationResult validate() {
            ValidationResult result = new ValidationResult();
            if (value == null || value.trim().isEmpty()) {
                result.addError("Value cannot be null or empty");
            }
            if (value != null && value.length() > 10) {
                result.addError("Value cannot be longer than 10 characters");
            }
            return result;
        }
    }

    @Test
    void validate_ValidObject_ShouldReturnValidResult() {
        ValidObject object = new ValidObject();
        ValidationResult result = object.validate();

        assertTrue(result.isValid());
        assertTrue(result.getErrors().isEmpty());
    }

    @Test
    void validate_InvalidObject_ShouldReturnInvalidResult() {
        InvalidObject object = new InvalidObject();
        ValidationResult result = object.validate();

        assertFalse(result.isValid());
        assertEquals(1, result.getErrors().size());
        assertEquals("Test validation error", result.getErrors().get(0));
    }

    @Test
    void validateAndThrow_ValidObject_ShouldNotThrow() {
        ValidObject object = new ValidObject();
        assertDoesNotThrow(() -> object.validateAndThrow());
    }

    @Test
    void validateAndThrow_InvalidObject_ShouldThrowValidationException() {
        InvalidObject object = new InvalidObject();
        ValidationException exception = assertThrows(ValidationException.class,
                () -> object.validateAndThrow());

        assertEquals("Test validation error", exception.getMessage());
    }

    @Test
    void validate_ConditionalObject_ShouldValidateBasedOnCondition() {
        ConditionalObject validObject = new ConditionalObject("Valid");
        ConditionalObject nullObject = new ConditionalObject(null);
        ConditionalObject emptyObject = new ConditionalObject("");
        ConditionalObject longObject = new ConditionalObject("ThisIsTooLong");

        assertTrue(validObject.validate().isValid());
        assertFalse(nullObject.validate().isValid());
        assertFalse(emptyObject.validate().isValid());
        assertFalse(longObject.validate().isValid());
    }

    @Test
    void validateAndThrow_ConditionalObject_ShouldThrowForInvalidCases() {
        ConditionalObject nullObject = new ConditionalObject(null);
        ValidationException exception = assertThrows(ValidationException.class,
                () -> nullObject.validateAndThrow());

        assertTrue(exception.getMessage().contains("Value cannot be null or empty"));
    }

    @Test
    void validate_ConditionalObject_ShouldCollectMultipleErrors() {
        ConditionalObject object = new ConditionalObject("");
        ValidationResult result = object.validate();

        assertFalse(result.isValid());
        assertEquals(1, result.getErrors().size());
        assertTrue(result.getErrors().get(0).contains("Value cannot be null or empty"));
    }
}