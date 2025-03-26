package com.lookbook.auth.domain.services;

import java.util.Map;
import java.util.function.Function;

import io.jsonwebtoken.Claims;

/**
 * Service interface for JWT token operations including generation, validation,
 * and extraction of claims.
 */
public interface JwtService {

    /**
     * Extract the username from the JWT token.
     *
     * @param token the JWT token
     * @return the username
     */
    String extractUsername(String token);

    /**
     * Extract a specific claim from the JWT token.
     *
     * @param token          the JWT token
     * @param claimsResolver the function to extract a specific claim
     * @param <T>            the type of the claim value
     * @return the claim value
     */
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    /**
     * Generate a token for the specified username.
     *
     * @param username the username
     * @return the generated JWT token
     */
    String generateToken(String username);

    /**
     * Generate a token with extra claims.
     *
     * @param extraClaims additional claims to include in the token
     * @param username    the username
     * @return the generated JWT token
     */
    String generateToken(Map<String, Object> extraClaims, String username);

    /**
     * Generate a refresh token for the specified username.
     *
     * @param username the username
     * @return the generated refresh token
     */
    String generateRefreshToken(String username);

    /**
     * Validate if the token is valid for the specified username.
     *
     * @param token    the JWT token
     * @param username the username
     * @return true if valid, false otherwise
     */
    boolean isTokenValid(String token, String username);

    /**
     * Check if the token has expired.
     *
     * @param token the JWT token
     * @return true if expired, false otherwise
     */
    boolean isTokenExpired(String token);
}