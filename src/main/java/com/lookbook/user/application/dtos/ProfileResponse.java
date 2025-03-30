package com.lookbook.user.application.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for profile responses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse {
    private UUID id;
    private UUID userId;
    private String displayName;
    private String biography;
    private String profileImageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}