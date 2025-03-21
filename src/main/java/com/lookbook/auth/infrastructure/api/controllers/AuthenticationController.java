package com.lookbook.auth.infrastructure.api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lookbook.auth.application.dtos.requests.LoginRequest;
import com.lookbook.auth.application.dtos.requests.RegisterUserRequest;
import com.lookbook.auth.application.dtos.requests.TokenRefreshRequest;
import com.lookbook.auth.application.dtos.responses.AuthenticationResponse;
import com.lookbook.auth.application.dtos.responses.TokenRefreshResponse;
import com.lookbook.auth.application.ports.services.AuthenticationService;
import com.lookbook.base.infrastructure.api.controllers.BaseController;
import com.lookbook.base.infrastructure.api.response.ApiResponse;

import jakarta.validation.Valid;

/**
 * Controller for authentication endpoints.
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController extends BaseController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Register a new user.
     *
     * @param registerRequest the registration request
     * @return the authentication response with tokens
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> register(
            @Valid @RequestBody RegisterUserRequest registerRequest) {

        logger.info("Registering user with username: {}", registerRequest.username());

        AuthenticationResponse response = authenticationService.register(registerRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createSuccessResponse(response, "User registered successfully"));
    }

    /**
     * Authenticate a user.
     *
     * @param loginRequest the login request
     * @return the authentication response with tokens
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> login(
            @Valid @RequestBody LoginRequest loginRequest) {

        logger.info("Authenticating user: {}", loginRequest.usernameOrEmail());

        AuthenticationResponse response = authenticationService.authenticate(loginRequest);

        return ResponseEntity.ok(createSuccessResponse(response, "User authenticated successfully"));
    }

    /**
     * Refresh an access token using a refresh token.
     *
     * @param refreshRequest the token refresh request
     * @return the token refresh response with new tokens
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenRefreshResponse>> refreshToken(
            @Valid @RequestBody TokenRefreshRequest refreshRequest) {

        TokenRefreshResponse response = authenticationService.refreshToken(refreshRequest);

        return ResponseEntity.ok(createSuccessResponse(response, "Token refreshed successfully"));
    }
}