package com.lookbook.auth.domain.valueobjects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.lookbook.base.domain.exceptions.ValidationException;

@DisplayName("Password Value Object Tests")
class PasswordTest {

    @Test
    @DisplayName("Should create password with valid value")
    void shouldCreatePasswordWithValidValue() {
        String rawPassword = "Test123!";
        Password password = Password.create(rawPassword);
        assertThat(password).isNotNull();
        assertThat(password.matches(rawPassword)).isTrue();
    }

    @Test
    @DisplayName("Should be equal when same password")
    void shouldBeEqualWhenSamePassword() {
        String rawPassword = "Test123!";
        Password password1 = Password.create(rawPassword);
        Password password2 = Password.create(rawPassword);

        // Two passwords with same value should match each other
        assertThat(password1.matches(rawPassword)).isTrue();
        assertThat(password2.matches(rawPassword)).isTrue();
        // But they should not be equal due to different hashes
        assertThat(password1).isNotEqualTo(password2);
    }

    @Test
    @DisplayName("Should not create password with empty value")
    void shouldNotCreatePasswordWithEmptyValue() {
        assertThatThrownBy(() -> Password.create(""))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Password is too short");
    }

    @Test
    @DisplayName("Should not create password with null value")
    void shouldNotCreatePasswordWithNullValue() {
        assertThatThrownBy(() -> Password.create(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Should not create password with too short value")
    void shouldNotCreatePasswordWithTooShortValue() {
        assertThatThrownBy(() -> Password.create("Short1!"))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Password is too short");
    }

    @Test
    @DisplayName("Should not create password with too long value")
    void shouldNotCreatePasswordWithTooLongValue() {
        String longPassword = "a".repeat(101) + "A1!";
        assertThatThrownBy(() -> Password.create(longPassword))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Password is too long");
    }

    @Test
    @DisplayName("Should not create password without lowercase")
    void shouldNotCreatePasswordWithoutLowercase() {
        assertThatThrownBy(() -> Password.create("TEST123!"))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("must contain at least one lowercase letter");
    }

    @Test
    @DisplayName("Should not create password without uppercase")
    void shouldNotCreatePasswordWithoutUppercase() {
        assertThatThrownBy(() -> Password.create("test123!"))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("must contain at least one uppercase letter");
    }

    @Test
    @DisplayName("Should not create password without digit")
    void shouldNotCreatePasswordWithoutDigit() {
        assertThatThrownBy(() -> Password.create("TestTest!"))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("must contain at least one digit");
    }

    @Test
    @DisplayName("Should not create password without special character")
    void shouldNotCreatePasswordWithoutSpecialCharacter() {
        assertThatThrownBy(() -> Password.create("Test1234"))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("must contain at least one special character");
    }

    @Test
    @DisplayName("Should create password from hash")
    void shouldCreatePasswordFromHash() {
        String rawPassword = "Test123!";
        Password original = Password.create(rawPassword);
        Password fromHash = Password.fromHash(original.getHashedValue());

        assertThat(fromHash).isEqualTo(original);
        assertThat(fromHash.matches(rawPassword)).isTrue();
    }

    @Test
    @DisplayName("Should not match invalid password")
    void shouldNotMatchInvalidPassword() {
        Password password = Password.create("Test123!");
        assertThat(password.matches("WrongPassword")).isFalse();
    }

    @Test
    @DisplayName("Should not match null password")
    void shouldNotMatchNullPassword() {
        Password password = Password.create("Test123!");
        assertThat(password.matches(null)).isFalse();
    }

    @Test
    @DisplayName("Should not match empty password")
    void shouldNotMatchEmptyPassword() {
        Password password = Password.create("Test123!");
        assertThat(password.matches("")).isFalse();
    }
}