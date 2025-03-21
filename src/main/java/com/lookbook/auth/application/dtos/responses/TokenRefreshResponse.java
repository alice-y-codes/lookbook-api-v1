package com.lookbook.auth.application.dtos.responses;

import java.time.LocalDateTime;

/**
 * DTO for token refresh response.
 */
public record TokenRefreshResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        LocalDateTime expiresAt) {
}