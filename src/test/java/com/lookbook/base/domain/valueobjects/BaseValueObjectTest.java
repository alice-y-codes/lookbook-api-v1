package com.lookbook.base.domain.valueobjects;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.lookbook.base.domain.exceptions.ValidationException;
import com.lookbook.base.domain.validation.ValidationResult;

/**
 * Tests for the {@link BaseValueObject} class.
 */
class BaseValueObjectTest {

    /**
     * A test value object representing a clothing size.
     */
    private static class Size extends BaseValueObject<Size> {
        private final String value;
        private final String system; // e.g., "US", "EU", "UK"

        public Size(String value, String system) {
            super();
            this.value = value;
            this.system = system;
            validateSelf(); // Validate after field initialization
        }

        @Override
        public ValidationResult validate() {
            ValidationResult result = new ValidationResult();
            if (value == null || value.trim().isEmpty()) {
                result.addError("Size value cannot be null or empty");
            }
            if (system == null || system.trim().isEmpty()) {
                result.addError("Size system cannot be null or empty");
            } else if (!system.matches("^(US|EU|UK)$")) {
                result.addError("Size system must be US, EU, or UK");
            }
            return result;
        }

        @Override
        protected boolean valueEquals(Object other) {
            Size that = (Size) other;
            return (this.value == null ? that.value == null : this.value.equals(that.value)) &&
                    (this.system == null ? that.system == null : this.system.equals(that.system));
        }

        @Override
        protected int valueHashCode() {
            return 31 * (value != null ? value.hashCode() : 0) + (system != null ? system.hashCode() : 0);
        }
    }

    /**
     * A test value object representing a color with RGB values.
     */
    private static class Color extends BaseValueObject<Color> {
        private final int red;
        private final int green;
        private final int blue;
        private final String name; // Optional friendly name

        public Color(int red, int green, int blue, String name) {
            super();
            this.red = red;
            this.green = green;
            this.blue = blue;
            this.name = name;
            validateSelf(); // Validate after field initialization
        }

        @Override
        public ValidationResult validate() {
            ValidationResult result = new ValidationResult();
            if (red < 0 || red > 255) {
                result.addError("Red value must be between 0 and 255");
            }
            if (green < 0 || green > 255) {
                result.addError("Green value must be between 0 and 255");
            }
            if (blue < 0 || blue > 255) {
                result.addError("Blue value must be between 0 and 255");
            }
            if (name != null && name.trim().isEmpty()) {
                result.addError("Color name cannot be empty if provided");
            }
            return result;
        }

        @Override
        protected boolean valueEquals(Object other) {
            Color that = (Color) other;
            return this.red == that.red &&
                    this.green == that.green &&
                    this.blue == that.blue &&
                    (this.name == null ? that.name == null : this.name.equals(that.name));
        }

        @Override
        protected int valueHashCode() {
            int result = 31 * red + green;
            result = 31 * result + blue;
            return 31 * result + (name != null ? name.hashCode() : 0);
        }
    }

    @Test
    void constructor_ValidSize_ShouldNotThrowException() {
        assertDoesNotThrow(() -> new Size("M", "US"));
    }

    @Test
    void constructor_InvalidSizeValue_ShouldThrowValidationException() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> new Size("", "US"));

        assertTrue(exception.getMessage().contains("Size value cannot be null or empty"));
    }

    @Test
    void constructor_InvalidSizeSystem_ShouldThrowValidationException() {
        assertThrows(ValidationException.class,
                () -> new Size("M", ""));

        assertThrows(ValidationException.class,
                () -> new Size("M", null));

        ValidationException exception = assertThrows(ValidationException.class,
                () -> new Size("M", "INVALID"));

        assertTrue(exception.getMessage().contains("Size system must be US, EU, or UK"));
    }

    @Test
    void equals_SameSizes_ShouldBeEqual() {
        Size size1 = new Size("M", "US");
        Size size2 = new Size("M", "US");

        assertEquals(size1, size2);
        assertEquals(size1.hashCode(), size2.hashCode());
    }

    @Test
    void equals_DifferentSizes_ShouldNotBeEqual() {
        Size size1 = new Size("M", "US");
        Size size2 = new Size("L", "US");
        Size size3 = new Size("M", "EU");

        assertNotEquals(size1, size2);
        assertNotEquals(size1, size3);
        assertNotEquals(size2, size3);
    }

    @Test
    void equals_WithNull_ShouldNotBeEqual() {
        Size size = new Size("M", "US");
        assertNotEquals(size, null);
    }

    @Test
    void equals_WithDifferentType_ShouldNotBeEqual() {
        Size size = new Size("M", "US");
        assertNotEquals(size, "Not a size object");
    }

    @Test
    void constructor_ValidColor_ShouldNotThrowException() {
        assertDoesNotThrow(() -> new Color(255, 128, 0, "Orange"));
    }

    @Test
    void constructor_InvalidRGBValues_ShouldThrowValidationException() {
        assertThrows(ValidationException.class,
                () -> new Color(-1, 128, 0, "Invalid Red"));

        assertThrows(ValidationException.class,
                () -> new Color(255, 256, 0, "Invalid Green"));

        ValidationException exception = assertThrows(ValidationException.class,
                () -> new Color(255, 128, -1, "Invalid Blue"));

        assertTrue(exception.getMessage().contains("Blue value must be between 0 and 255"));
    }

    @Test
    void constructor_EmptyName_ShouldThrowValidationException() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> new Color(255, 128, 0, ""));

        assertTrue(exception.getMessage().contains("Color name cannot be empty if provided"));
    }

    @Test
    void equals_CompositeValues_ShouldCompareCorrectly() {
        Color color1 = new Color(255, 128, 0, "Orange");
        Color color2 = new Color(255, 128, 0, "Orange");
        Color color3 = new Color(255, 128, 0, "Sunset");
        Color color4 = new Color(255, 0, 0, "Red");

        assertEquals(color1, color2);
        assertEquals(color1.hashCode(), color2.hashCode());
        assertNotEquals(color1, color3);
        assertNotEquals(color1, color4);
    }
}