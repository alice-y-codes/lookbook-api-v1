package com.lookbook.auth.infrastructure.adapters.services;

import java.util.Date;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lookbook.auth.application.dtos.requests.LoginRequest;
import com.lookbook.auth.application.dtos.requests.RegisterUserRequest;
import com.lookbook.auth.application.dtos.requests.TokenRefreshRequest;
import com.lookbook.auth.application.dtos.responses.AuthenticationResponse;
import com.lookbook.auth.application.dtos.responses.TokenRefreshResponse;
import com.lookbook.auth.application.mappers.AuthenticationMapper;
import com.lookbook.auth.application.ports.services.AuthenticationService;
import com.lookbook.auth.domain.services.JwtService;
import com.lookbook.base.domain.exceptions.ValidationException;
import com.lookbook.user.application.ports.repositories.UserRepository;
import com.lookbook.user.domain.aggregates.User;
import com.lookbook.user.domain.valueobjects.Email;
import com.lookbook.user.domain.valueobjects.Username;

/**
 * Adapter implementation of the AuthenticationService interface.
 */
@Service
public class AuthenticationServiceAdapter implements AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationServiceAdapter(
            UserRepository userRepository,
            JwtService jwtService,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public AuthenticationResponse register(RegisterUserRequest request) {
        // Check if username or email already exists
        Username username = Username.of(request.username());
        Email email = Email.of(request.email());

        if (userRepository.existsByUsername(username)) {
            throw new ValidationException("Username already exists");
        }

        if (userRepository.existsByEmail(email)) {
            throw new ValidationException("Email already exists");
        }

        // Create new user
        User user = User.register(request.username(), request.email(), request.password());

        // Save the user
        user = userRepository.save(user);

        // Generate tokens
        String accessToken = jwtService.generateToken(user.getUsername().getValue());
        String refreshToken = jwtService.generateRefreshToken(user.getUsername().getValue());

        // Get token expiration (for demo, adding 24 hours to current time)
        Date expiresAt = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000);

        // Return authentication response
        return AuthenticationMapper.toAuthResponse(accessToken, refreshToken, expiresAt, user);
    }

    @Override
    public AuthenticationResponse authenticate(LoginRequest request) {
        // Authenticate the user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.usernameOrEmail(),
                        request.password()));

        // Find the user
        Username username = Username.of(request.usernameOrEmail());
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ValidationException("User not found"));

        // Generate tokens
        String accessToken = jwtService.generateToken(user.getUsername().getValue());
        String refreshToken = jwtService.generateRefreshToken(user.getUsername().getValue());

        // Get token expiration (for demo, adding 24 hours to current time)
        Date expiresAt = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000);

        // Return authentication response
        return AuthenticationMapper.toAuthResponse(accessToken, refreshToken, expiresAt, user);
    }

    @Override
    public TokenRefreshResponse refreshToken(TokenRefreshRequest request) {
        // Extract username from token
        String refreshToken = request.refreshToken();
        String username = jwtService.extractUsername(refreshToken);

        // Validate token
        if (!jwtService.isTokenValid(refreshToken, username)) {
            throw new ValidationException("Invalid refresh token");
        }

        // Generate new access token
        String accessToken = jwtService.generateToken(username);

        // Get token expiration (for demo, adding 24 hours to current time)
        Date expiresAt = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000);

        // Return refresh response
        return AuthenticationMapper.toTokenRefreshResponse(accessToken, refreshToken, expiresAt);
    }
}