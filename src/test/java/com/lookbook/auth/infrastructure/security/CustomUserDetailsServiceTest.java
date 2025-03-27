package com.lookbook.auth.infrastructure.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.lookbook.user.domain.aggregates.User;
import com.lookbook.user.domain.repositories.UserRepository;
import com.lookbook.user.domain.valueobjects.Username;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    private CustomUserDetailsService userDetailsService;
    private User activeUser;
    private User inactiveUser;
    private static final String ACTIVE_USERNAME = "activeuser";
    private static final String INACTIVE_USERNAME = "inactiveuser";
    private static final String PASSWORD = "Password1!";

    @BeforeEach
    void setUp() {
        userDetailsService = new CustomUserDetailsService(userRepository);

        // Create an active test user
        activeUser = User.register(ACTIVE_USERNAME, "active@example.com", PASSWORD);

        // Activate the user
        try {
            java.lang.reflect.Method activateMethod = activeUser.getClass().getDeclaredMethod("activate");
            activateMethod.setAccessible(true);
            activateMethod.invoke(activeUser);

            // Verify that the user is active
            java.lang.reflect.Method getStatusMethod = activeUser.getClass().getDeclaredMethod("getStatus");
            getStatusMethod.setAccessible(true);
            Object status = getStatusMethod.invoke(activeUser);
            if (!status.toString().equals("ACTIVE")) {
                throw new RuntimeException("Failed to activate user: " + status);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error activating test user", e);
        }

        // Create an inactive test user
        inactiveUser = User.register(INACTIVE_USERNAME, "inactive@example.com", PASSWORD);

        // Set the inactive user status - first activate, then deactivate
        try {
            java.lang.reflect.Method activateMethod = inactiveUser.getClass().getDeclaredMethod("activate");
            activateMethod.setAccessible(true);
            activateMethod.invoke(inactiveUser);

            java.lang.reflect.Method deactivateMethod = inactiveUser.getClass().getDeclaredMethod("deactivate");
            deactivateMethod.setAccessible(true);
            deactivateMethod.invoke(inactiveUser);

            // Verify the user is inactive
            java.lang.reflect.Method getStatusMethod = inactiveUser.getClass().getDeclaredMethod("getStatus");
            getStatusMethod.setAccessible(true);
            Object status = getStatusMethod.invoke(inactiveUser);
            if (!status.toString().equals("INACTIVE")) {
                throw new RuntimeException("Failed to deactivate user: " + status);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error setting up test users", e);
        }
    }

    @Test
    void loadUserByUsername_ShouldReturnUserDetails_WhenUserIsActive() {
        // Mock repository to return the active user
        when(userRepository.findByUsername(any(Username.class))).thenReturn(Optional.of(activeUser));

        // Call the service method
        UserDetails userDetails = userDetailsService.loadUserByUsername(ACTIVE_USERNAME);

        // Verify the response
        assertNotNull(userDetails);
        assertEquals(ACTIVE_USERNAME, userDetails.getUsername());
        assertEquals(activeUser.getHashedPassword(), userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void loadUserByUsername_ShouldThrowException_WhenUserNotFound() {
        // Mock repository to return an empty optional
        when(userRepository.findByUsername(any(Username.class))).thenReturn(Optional.empty());

        // Verify that the service throws the expected exception
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("nonexistentuser"));
    }

    @Test
    void loadUserByUsername_ShouldThrowException_WhenUserIsNotActive() {
        // Mock repository to return the inactive user
        when(userRepository.findByUsername(any(Username.class))).thenReturn(Optional.of(inactiveUser));

        // Verify that the service throws the expected exception
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(INACTIVE_USERNAME));

        assertTrue(exception.getMessage().contains("not active"));
    }
}