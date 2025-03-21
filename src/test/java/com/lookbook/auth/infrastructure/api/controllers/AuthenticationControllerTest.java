package com.lookbook.auth.infrastructure.api.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
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
import com.lookbook.auth.application.dtos.requests.LoginRequest;
import com.lookbook.auth.application.dtos.requests.RegisterUserRequest;
import com.lookbook.auth.application.dtos.requests.TokenRefreshRequest;
import com.lookbook.auth.application.dtos.responses.AuthenticationResponse;
import com.lookbook.auth.application.dtos.responses.TokenRefreshResponse;
import com.lookbook.auth.application.dtos.responses.UserResponse;
import com.lookbook.auth.application.ports.services.AuthenticationService;
import com.lookbook.base.infrastructure.api.exceptions.GlobalExceptionHandler;
import com.lookbook.user.domain.aggregates.UserStatus;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

        @Mock
        private AuthenticationService authenticationService;

        @InjectMocks
        private AuthenticationController authenticationController;

        private MockMvc mockMvc;
        private ObjectMapper objectMapper = new ObjectMapper();

        @BeforeEach
        void setUp() {
                mockMvc = MockMvcBuilders.standaloneSetup(authenticationController)
                                .setControllerAdvice(new GlobalExceptionHandler())
                                .build();

                // Configure ObjectMapper to handle Java 8 dates
                objectMapper.registerModule(new JavaTimeModule());
        }

        @Test
        void register_ShouldReturnCreatedWithTokens_WhenValidRequest() throws Exception {
                // Set up test data
                RegisterUserRequest request = new RegisterUserRequest(
                                "testuser",
                                "test@example.com",
                                "Password1!");

                LocalDateTime now = LocalDateTime.now();
                LocalDateTime expiresAt = now.plusHours(1);

                UserResponse userResponse = new UserResponse(
                                UUID.randomUUID(),
                                "testuser",
                                "test@example.com",
                                UserStatus.ACTIVE,
                                now,
                                now);

                AuthenticationResponse authResponse = new AuthenticationResponse(
                                "access-token",
                                "refresh-token",
                                "Bearer",
                                expiresAt,
                                userResponse);

                when(authenticationService.register(any(RegisterUserRequest.class))).thenReturn(authResponse);

                // Perform the request
                mockMvc.perform(post("/api/v1/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.status").value("success"))
                                .andExpect(jsonPath("$.message").value("User registered successfully"))
                                .andExpect(jsonPath("$.data.accessToken").value("access-token"))
                                .andExpect(jsonPath("$.data.refreshToken").value("refresh-token"))
                                .andExpect(jsonPath("$.data.user.username").value("testuser"))
                                .andExpect(jsonPath("$.data.user.email").value("test@example.com"));
        }

        @Test
        void login_ShouldReturnOkWithTokens_WhenValidCredentials() throws Exception {
                // Set up test data
                LoginRequest request = new LoginRequest("testuser", "Password1!");

                LocalDateTime now = LocalDateTime.now();
                LocalDateTime expiresAt = now.plusHours(1);

                UserResponse userResponse = new UserResponse(
                                UUID.randomUUID(),
                                "testuser",
                                "test@example.com",
                                UserStatus.ACTIVE,
                                now,
                                now);

                AuthenticationResponse authResponse = new AuthenticationResponse(
                                "access-token",
                                "refresh-token",
                                "Bearer",
                                expiresAt,
                                userResponse);

                when(authenticationService.authenticate(any(LoginRequest.class))).thenReturn(authResponse);

                // Perform the request
                mockMvc.perform(post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.status").value("success"))
                                .andExpect(jsonPath("$.message").value("User authenticated successfully"))
                                .andExpect(jsonPath("$.data.accessToken").value("access-token"))
                                .andExpect(jsonPath("$.data.refreshToken").value("refresh-token"))
                                .andExpect(jsonPath("$.data.user.username").value("testuser"))
                                .andExpect(jsonPath("$.data.user.email").value("test@example.com"));
        }

        @Test
        void refreshToken_ShouldReturnOkWithNewTokens_WhenValidRefreshToken() throws Exception {
                // Set up test data
                TokenRefreshRequest request = new TokenRefreshRequest("valid-refresh-token");

                LocalDateTime expiresAt = LocalDateTime.now().plusHours(1);

                TokenRefreshResponse refreshResponse = new TokenRefreshResponse(
                                "new-access-token",
                                "valid-refresh-token",
                                "Bearer",
                                expiresAt);

                when(authenticationService.refreshToken(any(TokenRefreshRequest.class))).thenReturn(refreshResponse);

                // Perform the request
                mockMvc.perform(post("/api/v1/auth/refresh")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.status").value("success"))
                                .andExpect(jsonPath("$.message").value("Token refreshed successfully"))
                                .andExpect(jsonPath("$.data.accessToken").value("new-access-token"))
                                .andExpect(jsonPath("$.data.refreshToken").value("valid-refresh-token"));
        }
}