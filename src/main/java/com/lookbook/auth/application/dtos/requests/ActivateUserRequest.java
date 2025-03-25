package com.lookbook.auth.application.dtos.requests;

import lombok.Data;

/**
 * DTO for admin user activation request.
 */
@Data
public class ActivateUserRequest {
    private String username;
}