package com.lookbook.auth.application.ports.services;

import com.lookbook.auth.application.dtos.requests.LoginRequest;
import com.lookbook.auth.application.dtos.requests.RegisterUserRequest;
import com.lookbook.auth.application.dtos.requests.TokenRefreshRequest;
import com.lookbook.auth.application.dtos.responses.AuthenticationResponse;
import com.lookbook.auth.application.dtos.responses.TokenRefreshResponse;

/**
 * Service interface for authentication operations.
 */
public interface AuthenticationService {

    /**
     * Register a new user.
     *
     * @param request the registration request
     * @return the authentication response with tokens
     */
    AuthenticationResponse register(RegisterUserRequest request);

    /**
     * Authenticate a user.
     *
     * @param request the login request
     * @return the authentication response with tokens
     */
    AuthenticationResponse authenticate(LoginRequest request);

    /**
     * Refresh an access token using a refresh token.
     *
     * @param request the token refresh request
     * @return the token refresh response with new tokens
     */
    TokenRefreshResponse refreshToken(TokenRefreshRequest request);
}