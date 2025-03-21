package com.lookbook.auth.application.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.junit.jupiter.api.Test;

import com.lookbook.auth.application.dtos.responses.AuthenticationResponse;
import com.lookbook.auth.application.dtos.responses.TokenRefreshResponse;
import com.lookbook.user.domain.aggregates.User;

class AuthenticationMapperTest {

    private static final String ACCESS_TOKEN = "test-access-token";
    private static final String REFRESH_TOKEN = "test-refresh-token";
    private static final String TOKEN_TYPE = "Bearer";

    @Test
    void toAuthResponse_ShouldMapToAuthenticationResponseCorrectly() {
        // Arrange
        User user = User.register("testuser", "test@example.com", "Password1!");
        Date expiresAt = new Date();

        // Act
        AuthenticationResponse response = AuthenticationMapper.toAuthResponse(
                ACCESS_TOKEN, REFRESH_TOKEN, expiresAt, user);

        // Assert
        assertNotNull(response);
        assertEquals(ACCESS_TOKEN, response.accessToken());
        assertEquals(REFRESH_TOKEN, response.refreshToken());
        assertEquals(TOKEN_TYPE, response.tokenType());
        assertEquals(convertToLocalDateTime(expiresAt), response.expiresAt());
        assertNotNull(response.user());
        assertEquals(user.getUsername().getValue(), response.user().username());
        assertEquals(user.getEmail().getValue(), response.user().email());
    }

    @Test
    void toTokenRefreshResponse_ShouldMapToTokenRefreshResponseCorrectly() {
        // Arrange
        Date expiresAt = new Date();

        // Act
        TokenRefreshResponse response = AuthenticationMapper.toTokenRefreshResponse(
                ACCESS_TOKEN, REFRESH_TOKEN, expiresAt);

        // Assert
        assertNotNull(response);
        assertEquals(ACCESS_TOKEN, response.accessToken());
        assertEquals(REFRESH_TOKEN, response.refreshToken());
        assertEquals(TOKEN_TYPE, response.tokenType());
        assertEquals(convertToLocalDateTime(expiresAt), response.expiresAt());
    }

    private LocalDateTime convertToLocalDateTime(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}