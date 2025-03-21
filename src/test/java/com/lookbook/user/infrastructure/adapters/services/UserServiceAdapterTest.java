package com.lookbook.user.infrastructure.adapters.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lookbook.base.domain.exceptions.EntityNotFoundException;
import com.lookbook.base.domain.exceptions.ValidationException;
import com.lookbook.user.application.ports.repositories.UserRepository;
import com.lookbook.user.domain.aggregates.User;
import com.lookbook.user.domain.aggregates.UserStatus;
import com.lookbook.user.domain.valueobjects.Email;
import com.lookbook.user.domain.valueobjects.Username;

@ExtendWith(MockitoExtension.class)
class UserServiceAdapterTest {

    @Mock
    private UserRepository userRepository;

    private UserServiceAdapter userService;

    private User testUser;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userService = new UserServiceAdapter(userRepository);

        // Create a test user
        userId = UUID.randomUUID();
        testUser = User.register("testuser", "test@example.com", "Password1!");

        // Use reflection to set the UUID since it's automatically generated
        try {
            java.lang.reflect.Field idField = testUser.getClass().getSuperclass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(testUser, userId);
        } catch (Exception e) {
            throw new RuntimeException("Could not set ID field for test user", e);
        }
    }

    @Test
    void findById_ShouldReturnUser_WhenUserExists() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));

        Optional<User> result = userService.findById(userId);

        assertTrue(result.isPresent());
        assertEquals(testUser, result.get());
        verify(userRepository).findById(userId);
    }

    @Test
    void findByUsername_ShouldReturnUser_WhenUserExists() {
        Username username = Username.of("testuser");
        when(userRepository.findByUsername(any(Username.class))).thenReturn(Optional.of(testUser));

        Optional<User> result = userService.findByUsername("testuser");

        assertTrue(result.isPresent());
        assertEquals(testUser, result.get());
        verify(userRepository).findByUsername(any(Username.class));
    }

    @Test
    void findByEmail_ShouldReturnUser_WhenUserExists() {
        Email email = Email.of("test@example.com");
        when(userRepository.findByEmail(any(Email.class))).thenReturn(Optional.of(testUser));

        Optional<User> result = userService.findByEmail("test@example.com");

        assertTrue(result.isPresent());
        assertEquals(testUser, result.get());
        verify(userRepository).findByEmail(any(Email.class));
    }

    @Test
    void findAll_ShouldReturnAllUsers() {
        User anotherUser = User.register("anotheruser", "another@example.com", "Password1!");
        List<User> users = Arrays.asList(testUser, anotherUser);

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.findAll();

        assertEquals(2, result.size());
        assertEquals(users, result);
        verify(userRepository).findAll();
    }

    @Test
    void activate_ShouldActivateUser_WhenUserExists() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.activate(userId);

        assertEquals(UserStatus.ACTIVE, result.getStatus());
        verify(userRepository).findById(userId);
        verify(userRepository).save(testUser);
    }

    @Test
    void activate_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.activate(userId));
        verify(userRepository).findById(userId);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void deactivate_ShouldDeactivateUser_WhenUserExists() {
        // Activate the user first
        testUser.activate();

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.deactivate(userId);

        assertEquals(UserStatus.INACTIVE, result.getStatus());
        verify(userRepository).findById(userId);
        verify(userRepository).save(testUser);
    }

    @Test
    void deactivate_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.deactivate(userId));
        verify(userRepository).findById(userId);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void updateEmail_ShouldUpdateEmail_WhenUserExists() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        String newEmail = "newemail@example.com";
        User result = userService.updateEmail(userId, newEmail);

        assertEquals(newEmail, result.getEmail().getValue());
        verify(userRepository).findById(userId);
        verify(userRepository).save(testUser);
    }

    @Test
    void updateEmail_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> userService.updateEmail(userId, "newemail@example.com"));
        verify(userRepository).findById(userId);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void changePassword_ShouldChangePassword_WhenUserExistsAndPasswordIsCorrect() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.changePassword(userId, "Password1!", "NewPassword1!");

        // We can't directly check the password, but we can verify the method calls
        verify(userRepository).findById(userId);
        verify(userRepository).save(testUser);
    }

    @Test
    void changePassword_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> userService.changePassword(userId, "Password1!", "NewPassword1!"));
        verify(userRepository).findById(userId);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void changePassword_ShouldThrowException_WhenCurrentPasswordIsIncorrect() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));

        assertThrows(ValidationException.class,
                () -> userService.changePassword(userId, "WrongPassword!", "NewPassword1!"));
        verify(userRepository).findById(userId);
        verifyNoMoreInteractions(userRepository);
    }
}