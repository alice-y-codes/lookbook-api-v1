package com.lookbook.auth.application.dtos.requests;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO for user login requests.
 */
public record LoginRequest(
        @NotBlank(message = "Username or email is required") String usernameOrEmail,

        @NotBlank(message = "Password is required") String password) {
}