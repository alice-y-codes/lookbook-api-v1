package com.lookbook.user.infrastructure.api.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lookbook.auth.application.dtos.responses.UserResponse;
import com.lookbook.auth.application.mappers.UserMapper;
import com.lookbook.base.infrastructure.api.exceptions.GlobalExceptionHandler;
import com.lookbook.user.application.ports.services.UserService;
import com.lookbook.user.domain.aggregates.User;
import com.lookbook.user.domain.aggregates.UserStatus;
import com.lookbook.user.infrastructure.api.requests.ChangePasswordRequest;
import com.lookbook.user.infrastructure.api.requests.UpdateEmailRequest;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    private UUID userId;
    private User testUser;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper.registerModule(new JavaTimeModule());

        // Create test user
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

        // Activate the user for testing
        try {
            java.lang.reflect.Method activateMethod = testUser.getClass().getDeclaredMethod("activate");
            activateMethod.setAccessible(true);
            activateMethod.invoke(testUser);
        } catch (Exception e) {
            throw new RuntimeException("Could not activate test user", e);
        }
    }

    @Test
    void getUserById_ShouldReturnUser_WhenUserExists() throws Exception {
        when(userService.findById(userId)).thenReturn(Optional.of(testUser));

        // Setup a static mock for UserMapper (using Mockito static mocking)
        try (var mockStatic = mockStatic(UserMapper.class)) {
            LocalDateTime now = LocalDateTime.now();
            UserResponse userResponse = new UserResponse(
                    userId,
                    "testuser",
                    "test@example.com",
                    UserStatus.ACTIVE,
                    now,
                    now);

            mockStatic.when(() -> UserMapper.toUserResponse(any(User.class))).thenReturn(userResponse);

            mockMvc.perform(get("/api/v1/users/{id}", userId)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.data.id").value(userId.toString()))
                    .andExpect(jsonPath("$.data.username").value("testuser"))
                    .andExpect(jsonPath("$.data.email").value("test@example.com"))
                    .andExpect(jsonPath("$.data.status").value("ACTIVE"));
        }
    }

    @Test
    void getUserById_ShouldReturnNotFound_WhenUserDoesNotExist() throws Exception {
        when(userService.findById(userId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("User with ID " + userId + " not found"));
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsers() throws Exception {
        User anotherUser = User.register("anotheruser", "another@example.com", "Password1!");
        List<User> users = Arrays.asList(testUser, anotherUser);

        when(userService.findAll()).thenReturn(users);

        // Setup a static mock for UserMapper
        try (var mockStatic = mockStatic(UserMapper.class)) {
            LocalDateTime now = LocalDateTime.now();

            UserResponse response1 = new UserResponse(
                    userId,
                    "testuser",
                    "test@example.com",
                    UserStatus.ACTIVE,
                    now,
                    now);

            UserResponse response2 = new UserResponse(
                    UUID.randomUUID(),
                    "anotheruser",
                    "another@example.com",
                    UserStatus.PENDING,
                    now,
                    now);

            mockStatic.when(() -> UserMapper.toUserResponse(eq(testUser))).thenReturn(response1);
            mockStatic.when(() -> UserMapper.toUserResponse(eq(anotherUser))).thenReturn(response2);

            mockMvc.perform(get("/api/v1/users")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data.length()").value(2))
                    .andExpect(jsonPath("$.data[0].username").value("testuser"))
                    .andExpect(jsonPath("$.data[1].username").value("anotheruser"));
        }
    }

    @Test
    void activateUser_ShouldReturnActivatedUser() throws Exception {
        when(userService.activate(userId)).thenReturn(testUser);

        // Setup a static mock for UserMapper
        try (var mockStatic = mockStatic(UserMapper.class)) {
            LocalDateTime now = LocalDateTime.now();
            UserResponse userResponse = new UserResponse(
                    userId,
                    "testuser",
                    "test@example.com",
                    UserStatus.ACTIVE,
                    now,
                    now);

            mockStatic.when(() -> UserMapper.toUserResponse(any(User.class))).thenReturn(userResponse);

            mockMvc.perform(post("/api/v1/users/{id}/activate", userId)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.message").value("User activated successfully"))
                    .andExpect(jsonPath("$.data.status").value("ACTIVE"));
        }
    }

    @Test
    void deactivateUser_ShouldReturnDeactivatedUser() throws Exception {
        // Set up deactivated test user
        User deactivatedUser = testUser;
        try {
            java.lang.reflect.Method deactivateMethod = deactivatedUser.getClass().getDeclaredMethod("deactivate");
            deactivateMethod.setAccessible(true);
            deactivateMethod.invoke(deactivatedUser);
        } catch (Exception e) {
            throw new RuntimeException("Could not deactivate test user", e);
        }

        when(userService.deactivate(userId)).thenReturn(deactivatedUser);

        // Setup a static mock for UserMapper
        try (var mockStatic = mockStatic(UserMapper.class)) {
            LocalDateTime now = LocalDateTime.now();
            UserResponse userResponse = new UserResponse(
                    userId,
                    "testuser",
                    "test@example.com",
                    UserStatus.INACTIVE,
                    now,
                    now);

            mockStatic.when(() -> UserMapper.toUserResponse(any(User.class))).thenReturn(userResponse);

            mockMvc.perform(post("/api/v1/users/{id}/deactivate", userId)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.message").value("User deactivated successfully"))
                    .andExpect(jsonPath("$.data.status").value("INACTIVE"));
        }
    }

    @Test
    void updateEmail_ShouldReturnUserWithUpdatedEmail() throws Exception {
        String newEmail = "newemail@example.com";
        UpdateEmailRequest request = new UpdateEmailRequest(newEmail);

        // Mock service to return user with updated email
        User updatedUser = testUser;
        try {
            java.lang.reflect.Method updateEmailMethod = updatedUser.getClass().getDeclaredMethod("updateEmail",
                    String.class);
            updateEmailMethod.setAccessible(true);
            updateEmailMethod.invoke(updatedUser, newEmail);
        } catch (Exception e) {
            throw new RuntimeException("Could not update email for test user", e);
        }

        when(userService.updateEmail(eq(userId), eq(newEmail))).thenReturn(updatedUser);

        // Setup a static mock for UserMapper
        try (var mockStatic = mockStatic(UserMapper.class)) {
            LocalDateTime now = LocalDateTime.now();
            UserResponse userResponse = new UserResponse(
                    userId,
                    "testuser",
                    newEmail,
                    UserStatus.ACTIVE,
                    now,
                    now);

            mockStatic.when(() -> UserMapper.toUserResponse(any(User.class))).thenReturn(userResponse);

            mockMvc.perform(put("/api/v1/users/{id}/email", userId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.message").value("Email updated successfully"))
                    .andExpect(jsonPath("$.data.email").value(newEmail));
        }
    }

    @Test
    void changePassword_ShouldReturnSuccessResponse() throws Exception {
        String currentPassword = "Password1!";
        String newPassword = "NewPassword1!";
        ChangePasswordRequest request = new ChangePasswordRequest(currentPassword, newPassword);

        when(userService.changePassword(eq(userId), eq(currentPassword), eq(newPassword))).thenReturn(testUser);

        // Setup a static mock for UserMapper
        try (var mockStatic = mockStatic(UserMapper.class)) {
            LocalDateTime now = LocalDateTime.now();
            UserResponse userResponse = new UserResponse(
                    userId,
                    "testuser",
                    "test@example.com",
                    UserStatus.ACTIVE,
                    now,
                    now);

            mockStatic.when(() -> UserMapper.toUserResponse(any(User.class))).thenReturn(userResponse);

            mockMvc.perform(put("/api/v1/users/{id}/password", userId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.message").value("Password changed successfully"));
        }
    }
}