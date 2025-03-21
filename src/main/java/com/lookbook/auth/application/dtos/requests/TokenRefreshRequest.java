package com.lookbook.auth.application.dtos.requests;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO for token refresh requests.
 */
public record TokenRefreshRequest(
        @NotBlank(message = "Refresh token is required") String refreshToken) {
}