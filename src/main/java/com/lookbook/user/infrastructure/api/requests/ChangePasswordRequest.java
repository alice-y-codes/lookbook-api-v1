package com.lookbook.user.infrastructure.api.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Request DTO for changing a user's password.
 */
public record ChangePasswordRequest(
        @NotBlank(message = "Current password is required") String currentPassword,

        @NotBlank(message = "New password is required") @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$", message = "Password must have at least 8 characters, one uppercase, one lowercase, one number and one special character") String newPassword) {
}