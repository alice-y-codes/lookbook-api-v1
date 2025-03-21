package com.lookbook.auth.application.mappers;

import com.lookbook.auth.application.dtos.responses.UserResponse;
import com.lookbook.user.domain.aggregates.User;

/**
 * Mapper for User domain entity to UserResponse DTO.
 */
public class UserMapper {

    /**
     * Maps a User domain entity to a UserResponse DTO.
     *
     * @param user the User entity to map
     * @return the UserResponse DTO
     */
    public static UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername().getValue(),
                user.getEmail().getValue(),
                user.getStatus(),
                user.getCreatedAt(),
                user.getUpdatedAt());
    }
}