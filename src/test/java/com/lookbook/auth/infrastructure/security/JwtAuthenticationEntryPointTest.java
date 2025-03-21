package com.lookbook.auth.infrastructure.security;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationEntryPointTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private JwtAuthenticationEntryPoint entryPoint;
    private StringWriter stringWriter;
    private PrintWriter printWriter;

    @BeforeEach
    void setUp() throws Exception {
        entryPoint = new JwtAuthenticationEntryPoint();
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);
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

        // Verify response status and content type
        verify(response).setStatus(HttpStatus.UNAUTHORIZED.value());
        verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);

        // Force the PrintWriter to flush
        printWriter.flush();

        // Verify response content contains error elements
        String responseBody = stringWriter.toString();
        // We're not testing the exact JSON structure here, just that it contains the
        // essential elements
        assertResponseContains(responseBody, "UNAUTHORIZED");
        assertResponseContains(responseBody, "Invalid credentials");
        assertResponseContains(responseBody, requestUri);
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

        // Verify response status and content type
        verify(response).setStatus(HttpStatus.UNAUTHORIZED.value());
        verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);

        // Force the PrintWriter to flush
        printWriter.flush();

        // Verify response contains fallback message
        String responseBody = stringWriter.toString();
        assertResponseContains(responseBody, "UNAUTHORIZED");
        assertResponseContains(responseBody, "Unauthorized");
        assertResponseContains(responseBody, requestUri);
    }

    // Helper method to check if response contains specific strings
    private void assertResponseContains(String response, String expected) {
        if (!response.contains(expected)) {
            throw new AssertionError("Expected response to contain '" + expected + "' but was: " + response);
        }
    }
}