package com.lookbook.base.domain.validation;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.lookbook.base.domain.exceptions.ValidationException;

/**
 * Unit tests for the {@link ValidationResult} class.
 */
class ValidationResultTest {

    @Test
    void valid_ShouldReturnValidResult() {
        ValidationResult result = ValidationResult.valid();

        assertTrue(result.isValid());
        assertEquals(0, result.getErrors().size());
    }

    @Test
    void error_WithSingleError_ShouldReturnInvalidResult() {
        String errorMessage = "Test error message";
        ValidationResult result = ValidationResult.error(errorMessage);

        assertFalse(result.isValid());
        assertEquals(1, result.getErrors().size());
        assertEquals(errorMessage, result.getErrors().get(0));
    }

    @Test
    void constructor_WithErrorsList_ShouldCreateInvalidResult() {
        List<String> errors = Arrays.asList("Error 1", "Error 2", "Error 3");
        ValidationResult result = new ValidationResult(errors);

        assertFalse(result.isValid());
        assertEquals(3, result.getErrors().size());
        assertEquals(errors, result.getErrors());
    }

    @Test
    void addError_ToValidResult_ShouldMakeItInvalid() {
        ValidationResult result = ValidationResult.valid();
        result.addError("New error");

        assertFalse(result.isValid());
        assertEquals(1, result.getErrors().size());
        assertEquals("New error", result.getErrors().get(0));
    }

    @Test
    void addErrors_MultipleToValidResult_ShouldAddAllErrors() {
        ValidationResult result = ValidationResult.valid();
        List<String> errors = Arrays.asList("Error 1", "Error 2");

        result.addErrors(errors);

        assertFalse(result.isValid());
        assertEquals(2, result.getErrors().size());
        assertEquals(errors, result.getErrors());
    }

    @Test
    void combine_TwoValidResults_ShouldReturnValidResult() {
        ValidationResult result1 = ValidationResult.valid();
        ValidationResult result2 = ValidationResult.valid();

        ValidationResult combined = result1.combine(result2);

        assertTrue(combined.isValid());
        assertEquals(0, combined.getErrors().size());
    }

    @Test
    void combine_ValidAndInvalidResults_ShouldReturnInvalidResult() {
        ValidationResult valid = ValidationResult.valid();
        ValidationResult invalid = ValidationResult.error("Test error");

        ValidationResult validWithInvalid = valid.combine(invalid);
        ValidationResult invalidWithValid = invalid.combine(valid);

        assertFalse(validWithInvalid.isValid());
        assertEquals(1, validWithInvalid.getErrors().size());

        assertFalse(invalidWithValid.isValid());
        assertEquals(1, invalidWithValid.getErrors().size());
    }

    @Test
    void combine_TwoInvalidResults_ShouldCombineAllErrors() {
        ValidationResult invalid1 = ValidationResult.error("Error 1");
        List<String> errorList = Arrays.asList("Error 2", "Error 3");
        ValidationResult invalid2 = new ValidationResult(errorList);

        ValidationResult combined = invalid1.combine(invalid2);

        assertFalse(combined.isValid());
        assertEquals(3, combined.getErrors().size());
        assertTrue(combined.getErrors().contains("Error 1"));
        assertTrue(combined.getErrors().contains("Error 2"));
        assertTrue(combined.getErrors().contains("Error 3"));
    }

    @Test
    void throwIfInvalid_WithValidResult_ShouldNotThrowException() {
        ValidationResult valid = ValidationResult.valid();

        assertDoesNotThrow(() -> valid.throwIfInvalid());
    }

    @Test
    void throwIfInvalid_WithInvalidResult_ShouldThrowValidationException() {
        ValidationResult invalid = ValidationResult.error("Test error");

        ValidationException exception = assertThrows(ValidationException.class,
                () -> invalid.throwIfInvalid());

        assertTrue(exception.getMessage().contains("Test error"));
    }

    @Test
    void throwIfInvalid_WithMultipleErrors_ShouldIncludeAllErrorsInException() {
        List<String> errors = Arrays.asList("Error 1", "Error 2", "Error 3");
        ValidationResult invalid = new ValidationResult(errors);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> invalid.throwIfInvalid());

        String message = exception.getMessage();
        assertTrue(message.contains("Error 1"));
        assertTrue(message.contains("Error 2"));
        assertTrue(message.contains("Error 3"));
    }

    @Test
    void getErrors_ShouldReturnUnmodifiableList() {
        ValidationResult result = ValidationResult.error("Original error");
        List<String> errors = result.getErrors();

        assertThrows(UnsupportedOperationException.class,
                () -> errors.add("New error"));
    }
}