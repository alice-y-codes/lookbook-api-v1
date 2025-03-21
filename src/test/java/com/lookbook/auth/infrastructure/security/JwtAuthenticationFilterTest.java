package com.lookbook.auth.infrastructure.security;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.lookbook.auth.application.ports.services.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private JwtAuthenticationFilter jwtAuthenticationFilter;
    private static final String USERNAME = "testuser";
    private static final String VALID_TOKEN = "valid.jwt.token";

    @BeforeEach
    void setUp() {
        jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtService, userDetailsService);
        SecurityContextHolder.clearContext(); // Clear security context before each test
    }

    @Test
    void doFilterInternal_ShouldContinueFilterChain_WhenNoAuthHeader() throws ServletException, IOException {
        // Mock request to return null for Authorization header
        when(request.getHeader("Authorization")).thenReturn(null);

        // Call the filter
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Verify filter chain was continued
        verify(filterChain).doFilter(request, response);

        // No interaction with JWT service or UserDetailsService
        verifyNoInteractions(jwtService);
        verifyNoInteractions(userDetailsService);
    }

    @Test
    void doFilterInternal_ShouldContinueFilterChain_WhenAuthHeaderNotBearer() throws ServletException, IOException {
        // Mock request to return non-Bearer authorization header
        when(request.getHeader("Authorization")).thenReturn("Basic dXNlcjpwYXNzd29yZA==");

        // Call the filter
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Verify filter chain was continued
        verify(filterChain).doFilter(request, response);

        // No interaction with JWT service or UserDetailsService
        verifyNoInteractions(jwtService);
        verifyNoInteractions(userDetailsService);
    }

    @Test
    void doFilterInternal_ShouldSetAuthentication_WhenValidToken() throws ServletException, IOException {
        // Create user details
        UserDetails userDetails = new User(USERNAME, "password", Collections.emptyList());

        // Mock request to return Bearer token
        when(request.getHeader("Authorization")).thenReturn("Bearer " + VALID_TOKEN);

        // Mock JWT service
        when(jwtService.extractUsername(VALID_TOKEN)).thenReturn(USERNAME);
        when(jwtService.isTokenValid(VALID_TOKEN, USERNAME)).thenReturn(true);

        // Mock UserDetailsService
        when(userDetailsService.loadUserByUsername(USERNAME)).thenReturn(userDetails);

        // Call the filter
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Verify filter chain was continued
        verify(filterChain).doFilter(request, response);

        // Verify interactions with services
        verify(jwtService).extractUsername(VALID_TOKEN);
        verify(userDetailsService).loadUserByUsername(USERNAME);
        verify(jwtService).isTokenValid(VALID_TOKEN, USERNAME);
    }

    @Test
    void doFilterInternal_ShouldNotSetAuthentication_WhenInvalidToken() throws ServletException, IOException {
        // Create user details
        UserDetails userDetails = new User(USERNAME, "password", Collections.emptyList());

        // Mock request to return Bearer token
        when(request.getHeader("Authorization")).thenReturn("Bearer " + VALID_TOKEN);

        // Mock JWT service
        when(jwtService.extractUsername(VALID_TOKEN)).thenReturn(USERNAME);
        when(jwtService.isTokenValid(VALID_TOKEN, USERNAME)).thenReturn(false);

        // Mock UserDetailsService
        when(userDetailsService.loadUserByUsername(USERNAME)).thenReturn(userDetails);

        // Call the filter
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Verify filter chain was continued
        verify(filterChain).doFilter(request, response);

        // Verify interactions with services
        verify(jwtService).extractUsername(VALID_TOKEN);
        verify(userDetailsService).loadUserByUsername(USERNAME);
        verify(jwtService).isTokenValid(VALID_TOKEN, USERNAME);
    }

    @Test
    void doFilterInternal_ShouldContinueFilterChain_WhenNoUsernameInToken() throws ServletException, IOException {
        // Mock request to return Bearer token
        when(request.getHeader("Authorization")).thenReturn("Bearer " + VALID_TOKEN);

        // Mock JWT service to return null username
        when(jwtService.extractUsername(VALID_TOKEN)).thenReturn(null);

        // Call the filter
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Verify filter chain was continued
        verify(filterChain).doFilter(request, response);

        // Verify interactions with services
        verify(jwtService).extractUsername(VALID_TOKEN);
        verifyNoInteractions(userDetailsService);
    }
}