package com.lookbook.auth.application.dtos.responses;

import java.time.LocalDateTime;
import java.util.UUID;

import com.lookbook.user.domain.aggregates.UserStatus;

/**
 * DTO for user response.
 */
public record UserResponse(
        UUID id,
        String username,
        String email,
        UserStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}