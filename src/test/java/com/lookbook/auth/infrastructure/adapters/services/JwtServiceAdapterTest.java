package com.lookbook.auth.infrastructure.adapters.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.lookbook.auth.infrastructure.config.JwtConfig;

class JwtServiceAdapterTest {

    private JwtServiceAdapter jwtService;
    private static final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    private static final long JWT_EXPIRATION = 3600000; // 1 hour
    private static final long REFRESH_EXPIRATION = 86400000; // 24 hours
    private static final String TEST_USERNAME = "testuser";

    @BeforeEach
    void setUp() {
        // Create JwtConfig with test values
        JwtConfig jwtConfig = new JwtConfig();
        jwtConfig.setSecretKey(SECRET_KEY);
        jwtConfig.setExpiration(JWT_EXPIRATION);
        JwtConfig.RefreshToken refreshToken = new JwtConfig.RefreshToken();
        refreshToken.setExpiration(REFRESH_EXPIRATION);
        jwtConfig.setRefreshToken(refreshToken);

        // Create JwtServiceAdapter with the config
        jwtService = new JwtServiceAdapter(jwtConfig);
    }

    @Test
    void generateToken_WithUsername_ShouldReturnToken() {
        // Act
        String token = jwtService.generateToken(TEST_USERNAME);

        // Assert
        assertNotNull(token);
    }

    @Test
    void generateToken_WithExtraClaims_ShouldIncludeClaimsInToken() {
        // Arrange
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", "USER");

        // Act
        String token = jwtService.generateToken(extraClaims, TEST_USERNAME);

        // Assert
        assertNotNull(token);
        assertEquals("USER", jwtService.extractClaim(token, claims -> claims.get("role", String.class)));
    }

    @Test
    void extractUsername_WithValidToken_ShouldReturnUsername() {
        // Arrange
        String token = jwtService.generateToken(TEST_USERNAME);

        // Act
        String username = jwtService.extractUsername(token);

        // Assert
        assertEquals(TEST_USERNAME, username);
    }

    @Test
    void isTokenValid_WithValidToken_ShouldReturnTrue() {
        // Arrange
        String token = jwtService.generateToken(TEST_USERNAME);

        // Act
        boolean isValid = jwtService.isTokenValid(token, TEST_USERNAME);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void isTokenValid_WithInvalidUsername_ShouldReturnFalse() {
        // Arrange
        String token = jwtService.generateToken(TEST_USERNAME);

        // Act
        boolean isValid = jwtService.isTokenValid(token, "wronguser");

        // Assert
        assertFalse(isValid);
    }

    @Test
    void generateRefreshToken_ShouldReturnToken() {
        // Act
        String refreshToken = jwtService.generateRefreshToken(TEST_USERNAME);

        // Assert
        assertNotNull(refreshToken);
        assertEquals(TEST_USERNAME, jwtService.extractUsername(refreshToken));
    }
}