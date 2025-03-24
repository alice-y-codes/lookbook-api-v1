package com.lookbook.auth.infrastructure.security;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
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

    @BeforeEach
    void setUp() throws Exception {
        entryPoint = new JwtAuthenticationEntryPoint();
        outputStream = new ByteArrayOutputStream();
        ServletOutputStream servletOutputStream = new ServletOutputStream() {
            @Override
            public void write(int b) throws IOException {
                outputStream.write(b);
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setWriteListener(WriteListener writeListener) {
                // Not needed for testing
            }
        };
        when(response.getOutputStream()).thenReturn(servletOutputStream);
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

        // Get response content
        String responseBody = outputStream.toString();

        // Verify response content contains error elements
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

        // Get response content
        String responseBody = outputStream.toString();

        // Verify response contains fallback message
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