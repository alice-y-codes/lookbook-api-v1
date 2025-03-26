package com.lookbook.auth.application.ports.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.lookbook.auth.domain.services.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.lookbook.auth.application.dtos.requests.LoginRequest;
import com.lookbook.auth.application.dtos.requests.RegisterUserRequest;
import com.lookbook.auth.application.dtos.requests.TokenRefreshRequest;
import com.lookbook.auth.application.dtos.responses.AuthenticationResponse;
import com.lookbook.auth.application.dtos.responses.TokenRefreshResponse;
import com.lookbook.auth.infrastructure.adapters.services.AuthenticationServiceAdapter;
import com.lookbook.base.domain.exceptions.ValidationException;
import com.lookbook.user.application.ports.repositories.UserRepository;
import com.lookbook.user.domain.aggregates.User;
import com.lookbook.user.domain.valueobjects.Email;
import com.lookbook.user.domain.valueobjects.Username;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        // The implementation class now exists, so we can initialize it
        authenticationService = new AuthenticationServiceAdapter(
                userRepository, jwtService, passwordEncoder, authenticationManager);
    }

    @Test
    void register_WithValidRequest_ShouldCreateUserAndReturnTokens() {
        // Arrange
        RegisterUserRequest request = new RegisterUserRequest(
                "testuser", "test@example.com", "Password1!");

        when(userRepository.existsByUsername(any(Username.class))).thenReturn(false);
        when(userRepository.existsByEmail(any(Email.class))).thenReturn(false);
        when(jwtService.generateToken(anyString())).thenReturn("access-token");
        when(jwtService.generateRefreshToken(anyString())).thenReturn("refresh-token");

        User mockUser = User.register("testuser", "test@example.com", "Password1!");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        // Act
        AuthenticationResponse response = authenticationService.register(request);

        // Assert
        assertNotNull(response);
        assertEquals("access-token", response.accessToken());
        assertEquals("refresh-token", response.refreshToken());
        assertEquals("Bearer", response.tokenType());
        assertNotNull(response.expiresAt());
        assertEquals("testuser", response.user().username());
    }

    @Test
    void register_WithExistingUsername_ShouldThrowException() {
        // Arrange
        RegisterUserRequest request = new RegisterUserRequest(
                "existinguser", "test@example.com", "Password1!");

        when(userRepository.existsByUsername(any(Username.class))).thenReturn(true);

        // Act & Assert
        assertThrows(ValidationException.class, () -> authenticationService.register(request));
    }

    @Test
    void authenticate_WithValidCredentials_ShouldReturnTokens() {
        // Arrange
        LoginRequest request = new LoginRequest("testuser", "Password1!");

        Username username = Username.of("testuser");
        User mockUser = User.register("testuser", "test@example.com", "Password1!");

        when(userRepository.findByUsername(any(Username.class))).thenReturn(Optional.of(mockUser));
        when(jwtService.generateToken(anyString())).thenReturn("access-token");
        when(jwtService.generateRefreshToken(anyString())).thenReturn("refresh-token");

        // Act
        AuthenticationResponse response = authenticationService.authenticate(request);

        // Assert
        assertNotNull(response);
        assertEquals("access-token", response.accessToken());
        assertEquals("refresh-token", response.refreshToken());
        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken("testuser", "Password1!"));
    }

    @Test
    void authenticate_WithInvalidCredentials_ShouldThrowException() {
        // Arrange
        LoginRequest request = new LoginRequest("testuser", "WrongPassword!");

        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> authenticationService.authenticate(request));
    }

    @Test
    void refreshToken_WithValidToken_ShouldReturnNewTokens() {
        // Arrange
        TokenRefreshRequest request = new TokenRefreshRequest("valid-refresh-token");
        String username = "testuser";

        when(jwtService.extractUsername("valid-refresh-token")).thenReturn(username);
        when(jwtService.isTokenValid("valid-refresh-token", username)).thenReturn(true);
        when(jwtService.generateToken(username)).thenReturn("new-access-token");

        // Act
        TokenRefreshResponse response = authenticationService.refreshToken(request);

        // Assert
        assertNotNull(response);
        assertEquals("new-access-token", response.accessToken());
        assertEquals("valid-refresh-token", response.refreshToken());
    }

    @Test
    void refreshToken_WithInvalidToken_ShouldThrowException() {
        // Arrange
        TokenRefreshRequest request = new TokenRefreshRequest("invalid-refresh-token");
        String username = "testuser";

        when(jwtService.extractUsername("invalid-refresh-token")).thenReturn(username);
        when(jwtService.isTokenValid("invalid-refresh-token", username)).thenReturn(false);

        // Act & Assert
        assertThrows(ValidationException.class, () -> authenticationService.refreshToken(request));
    }
}