package com.lookbook.user.infrastructure.api.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO for updating a user's email.
 */
public record UpdateEmailRequest(
        @NotBlank(message = "Email is required") @Email(message = "Must be a valid email address") String email) {
}