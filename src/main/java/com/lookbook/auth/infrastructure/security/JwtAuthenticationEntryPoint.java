package com.lookbook.auth.infrastructure.security;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {

        // Create a standardized error response
        ErrorResponse errorResponse = ErrorResponse.of(
                "UNAUTHORIZED",
                authException.getMessage() != null ? authException.getMessage() : "Unauthorized",
                request.getRequestURI());

        // Set response status and content type
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // Write error response to output
        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }
}