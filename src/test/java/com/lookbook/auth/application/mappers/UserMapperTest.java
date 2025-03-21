package com.lookbook.auth.application.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.lookbook.auth.application.dtos.responses.UserResponse;
import com.lookbook.user.domain.aggregates.User;

class UserMapperTest {

    @Test
    void toUserResponse_ShouldMapUserToUserResponseCorrectly() {
        // Arrange
        User user = User.register("testuser", "test@example.com", "Password1!");

        // Act
        UserResponse response = UserMapper.toUserResponse(user);

        // Assert
        assertNotNull(response);
        assertEquals(user.getId(), response.id());
        assertEquals(user.getUsername().getValue(), response.username());
        assertEquals(user.getEmail().getValue(), response.email());
        assertEquals(user.getStatus(), response.status());
        assertEquals(user.getCreatedAt(), response.createdAt());
        assertEquals(user.getUpdatedAt(), response.updatedAt());
    }
}