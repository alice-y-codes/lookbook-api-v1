package com.lookbook.auth.infrastructure.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lookbook.base.infrastructure.api.response.ErrorResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Custom entry point that handles authentication failures.
 * This class is invoked when a user tries to access a secured resource
 * without proper authentication.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);
    private final ObjectMapper objectMapper;
    private final List<String> publicEndpoints = Arrays.asList(
            "/api/v1/auth/register",
            "/api/v1/auth/login",
            "/api/v1/auth/refresh",
            "/v3/api-docs",
            "/swagger-ui",
            "/api/v1/health");

    public JwtAuthenticationEntryPoint() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {

        String path = request.getRequestURI();
        logger.debug("Authentication entry point: Handling request for path: {}", path);

        // Skip processing for public endpoints
        if (publicEndpoints.stream().anyMatch(endpoint -> path.startsWith(endpoint))) {
            logger.debug("Authentication entry point: Public endpoint detected, continuing filter chain");
            response.setStatus(HttpStatus.OK.value());
            return;
        }

        // Skip processing for successful responses
        if (response.getStatus() == HttpStatus.OK.value() || response.getStatus() == HttpStatus.CREATED.value()) {
            logger.debug("Authentication entry point: Successful response detected, not interfering");
            return;
        }

        logger.debug("Authentication entry point: Protected endpoint detected, sending unauthorized response");
        logger.debug("Authentication entry point: Exception message: {}", authException.getMessage());

        // Create a standardized error response
        ErrorResponse errorResponse = ErrorResponse.of(
                "UNAUTHORIZED",
                authException.getMessage() != null ? authException.getMessage() : "Unauthorized",
                request.getRequestURI());

        // Set response status and content type
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // Write error response to output
        objectMapper.writeValue(response.getWriter(), errorResponse);
        logger.debug("Authentication entry point: Sent unauthorized response");
    }
}