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

        @Override
        public String toString() {
                return "AuthenticationResponse{" +
                                "accessToken='" + accessToken + '\'' +
                                ", refreshToken='" + refreshToken + '\'' +
                                ", tokenType='" + tokenType + '\'' +
                                ", expiresAt=" + expiresAt +
                                ", user=" + user +
                                '}';
        }
}