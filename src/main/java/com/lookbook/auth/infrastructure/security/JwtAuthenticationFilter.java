package com.lookbook.auth.infrastructure.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.lookbook.auth.application.ports.services.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filter that intercepts each request and validates JWT tokens.
 * Sets the authentication in the security context if the token is valid.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final List<String> publicEndpoints = Arrays.asList(
            "/api/v1/auth/register",
            "/api/v1/auth/login",
            "/api/v1/auth/refresh",
            "/v3/api-docs",
            "/swagger-ui",
            "/api/v1/health");

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        boolean shouldSkip = publicEndpoints.stream().anyMatch(endpoint -> path.startsWith(endpoint));
        logger.debug("JWT Filter: Request path '{}' should skip filter: {}", path, shouldSkip);
        return shouldSkip;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        logger.debug("JWT Filter: Processing request for path: {}", request.getRequestURI());

        // Skip JWT validation for public endpoints
        if (shouldNotFilter(request)) {
            logger.debug("JWT Filter: Skipping JWT validation for public endpoint");
            filterChain.doFilter(request, response);
            return;
        }

        // Get Authorization header
        final String authHeader = request.getHeader("Authorization");
        logger.debug("JWT Filter: Authorization header present: {}", authHeader != null);

        // Skip validation if Authorization header is missing or not a Bearer token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.debug("JWT Filter: No valid Authorization header, continuing chain");
            filterChain.doFilter(request, response);
            return;
        }

        // Extract token and username
        final String jwt = authHeader.substring(7);
        final String username = jwtService.extractUsername(jwt);
        logger.debug("JWT Filter: Extracted username from token: {}", username);

        // If username exists and no authentication is set
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Validate token first
            if (jwtService.isTokenValid(jwt, username)) {
                logger.debug("JWT Filter: Token is valid, loading user details");
                try {
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                    logger.debug("JWT Filter: Loaded user details for username: {}", username);

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } catch (UsernameNotFoundException e) {
                    logger.debug("JWT Filter: User not found: {}", username);
                    // Continue filter chain without setting authentication
                }
            } else {
                logger.debug("JWT Filter: Token is invalid");
            }
        }
        filterChain.doFilter(request, response);
    }
}