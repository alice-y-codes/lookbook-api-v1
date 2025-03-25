package com.lookbook.auth.infrastructure.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lookbook.base.infrastructure.api.response.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationEntryPointTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private JwtAuthenticationEntryPoint entryPoint;
    private ByteArrayOutputStream outputStream;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws Exception {
        entryPoint = new JwtAuthenticationEntryPoint();
        outputStream = new ByteArrayOutputStream();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Create a PrintWriter that writes to our ByteArrayOutputStream
        PrintWriter writer = new PrintWriter(outputStream);

        // Mock response to return our PrintWriter
        when(response.getWriter()).thenReturn(writer);
    }

    @Test
    void commence_ShouldReturnUnauthorizedWithErrorResponse() throws Exception {
        // Set up test data
        String requestUri = "/api/v1/secured-endpoint";
        AuthenticationException authException = new BadCredentialsException("Invalid credentials");

        // Mock request URI
        when(request.getRequestURI()).thenReturn(requestUri);

        // Call the method
        entryPoint.commence(request, response, authException);

        // Flush the writer to ensure all content is written
        response.getWriter().flush();

        // Verify response status and content type
        verify(response).setStatus(HttpStatus.UNAUTHORIZED.value());
        verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);

        // Get response content
        String responseBody = outputStream.toString();

        // Parse response as ErrorResponse
        ErrorResponse errorResponse = objectMapper.readValue(responseBody, ErrorResponse.class);

        // Verify response content
        assertEquals("UNAUTHORIZED", errorResponse.getCode());
        assertEquals("Invalid credentials", errorResponse.getMessage());
        assertEquals(requestUri, errorResponse.getPath());
    }

    @Test
    void commence_ShouldHandleNullExceptionMessage() throws Exception {
        // Set up test data with null message
        String requestUri = "/api/v1/secured-endpoint";
        AuthenticationException authException = new AuthenticationException(null) {
        };

        // Mock request URI
        when(request.getRequestURI()).thenReturn(requestUri);

        // Call the method
        entryPoint.commence(request, response, authException);

        // Flush the writer to ensure all content is written
        response.getWriter().flush();

        // Verify response status and content type
        verify(response).setStatus(HttpStatus.UNAUTHORIZED.value());
        verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);

        // Get response content
        String responseBody = outputStream.toString();

        // Parse response as ErrorResponse
        ErrorResponse errorResponse = objectMapper.readValue(responseBody, ErrorResponse.class);

        // Verify response content
        assertEquals("UNAUTHORIZED", errorResponse.getCode());
        assertEquals("Unauthorized", errorResponse.getMessage());
        assertEquals(requestUri, errorResponse.getPath());
    }
}