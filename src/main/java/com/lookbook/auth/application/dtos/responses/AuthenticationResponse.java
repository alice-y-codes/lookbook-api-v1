package com.lookbook.auth.application.dtos.responses;

import java.time.LocalDateTime;

/**
 * DTO for authentication response.
 */
public record AuthenticationResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        LocalDateTime expiresAt,
        UserResponse user) {
}