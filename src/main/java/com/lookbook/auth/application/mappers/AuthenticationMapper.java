package com.lookbook.auth.application.mappers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import com.lookbook.auth.application.dtos.responses.AuthenticationResponse;
import com.lookbook.auth.application.dtos.responses.TokenRefreshResponse;
import com.lookbook.user.domain.aggregates.User;

/**
 * Mapper for authentication responses.
 */
public class AuthenticationMapper {

    private static final String TOKEN_TYPE = "Bearer";

    /**
     * Maps authentication data to an AuthenticationResponse DTO.
     *
     * @param accessToken  the access token
     * @param refreshToken the refresh token
     * @param expiresAt    the expiration date
     * @param user         the user
     * @return the AuthenticationResponse DTO
     */
    public static AuthenticationResponse toAuthResponse(
            String accessToken,
            String refreshToken,
            Date expiresAt,
            User user) {
        return new AuthenticationResponse(
                accessToken,
                refreshToken,
                TOKEN_TYPE,
                convertToLocalDateTime(expiresAt),
                UserMapper.toUserResponse(user));
    }

    /**
     * Maps refresh token data to a TokenRefreshResponse DTO.
     *
     * @param accessToken  the new access token
     * @param refreshToken the refresh token
     * @param expiresAt    the expiration date
     * @return the TokenRefreshResponse DTO
     */
    public static TokenRefreshResponse toTokenRefreshResponse(
            String accessToken,
            String refreshToken,
            Date expiresAt) {
        return new TokenRefreshResponse(
                accessToken,
                refreshToken,
                TOKEN_TYPE,
                convertToLocalDateTime(expiresAt));
    }

    /**
     * Converts a Date to LocalDateTime.
     *
     * @param date the date to convert
     * @return the LocalDateTime
     */
    private static LocalDateTime convertToLocalDateTime(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}