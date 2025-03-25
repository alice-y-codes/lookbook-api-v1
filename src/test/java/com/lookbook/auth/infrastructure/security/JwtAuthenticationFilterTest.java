package com.lookbook.auth.infrastructure.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.lookbook.auth.application.ports.services.JwtService;
import com.lookbook.user.domain.aggregates.User;

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
    private static final String REQUEST_URI = "/api/v1/protected-endpoint";

    @BeforeEach
    void setUp() {
        jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtService, userDetailsService);
        SecurityContextHolder.clearContext(); // Clear security context before each test

        // Mock request URI for all tests
        when(request.getRequestURI()).thenReturn(REQUEST_URI);
    }

    @Test
    void doFilterInternal_ShouldContinueFilterChain_WhenNoAuthHeader() throws ServletException, IOException {
        // Mock request to return null for Authorization header
        when(request.getHeader("Authorization")).thenReturn(null);

        // Call the filter
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Verify filter chain was continued
        verify(filterChain).doFilter(request, response);

        // Verify security context is empty
        assertNull(SecurityContextHolder.getContext().getAuthentication());

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

        // Verify security context is empty
        assertNull(SecurityContextHolder.getContext().getAuthentication());

        // No interaction with JWT service or UserDetailsService
        verifyNoInteractions(jwtService);
        verifyNoInteractions(userDetailsService);
    }

    @Test
    void doFilterInternal_ShouldSetAuthentication_WhenValidToken() throws ServletException, IOException {
        // Create domain user
        User domainUser = User.register(
                USERNAME,
                USERNAME + "@example.com",
                "Password123!");

        // Create user details from domain user
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                domainUser.getUsername().getValue(),
                domainUser.getHashedPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

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

        // Verify security context is set
        SecurityContext context = SecurityContextHolder.getContext();
        assertNotNull(context);
        Authentication authentication = context.getAuthentication();
        assertNotNull(authentication);
        assertEquals(userDetails, authentication.getPrincipal());
        assertEquals(1, authentication.getAuthorities().size());
        assertEquals("ROLE_USER", authentication.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    void doFilterInternal_ShouldNotSetAuthentication_WhenInvalidToken() throws ServletException, IOException {
        // Mock request to return Bearer token
        when(request.getHeader("Authorization")).thenReturn("Bearer " + VALID_TOKEN);

        // Mock JWT service
        when(jwtService.extractUsername(VALID_TOKEN)).thenReturn(USERNAME);
        when(jwtService.isTokenValid(VALID_TOKEN, USERNAME)).thenReturn(false);

        // Call the filter
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Verify filter chain was continued
        verify(filterChain).doFilter(request, response);

        // Verify interactions with services
        verify(jwtService).extractUsername(VALID_TOKEN);
        verify(jwtService).isTokenValid(VALID_TOKEN, USERNAME);
        verifyNoInteractions(userDetailsService);

        // Verify security context is empty
        assertNull(SecurityContextHolder.getContext().getAuthentication());
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

        // Verify security context is empty
        assertNull(SecurityContextHolder.getContext().getAuthentication());

        // Verify interactions with services
        verify(jwtService).extractUsername(VALID_TOKEN);
        verifyNoInteractions(userDetailsService);
    }

    @Test
    void doFilterInternal_ShouldContinueFilterChain_WhenUserNotFound() throws ServletException, IOException {
        // Mock request to return Bearer token
        when(request.getHeader("Authorization")).thenReturn("Bearer " + VALID_TOKEN);

        // Mock JWT service
        when(jwtService.extractUsername(VALID_TOKEN)).thenReturn(USERNAME);
        when(jwtService.isTokenValid(VALID_TOKEN, USERNAME)).thenReturn(true);

        // Mock UserDetailsService to throw exception
        when(userDetailsService.loadUserByUsername(USERNAME))
                .thenThrow(new UsernameNotFoundException("User not found"));

        // Call the filter
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Verify filter chain was continued despite the exception
        verify(filterChain).doFilter(request, response);

        // Verify security context is empty
        assertNull(SecurityContextHolder.getContext().getAuthentication());

        // Verify interactions with services
        verify(jwtService).extractUsername(VALID_TOKEN);
        verify(userDetailsService).loadUserByUsername(USERNAME);
        verify(jwtService).isTokenValid(VALID_TOKEN, USERNAME);
    }
}