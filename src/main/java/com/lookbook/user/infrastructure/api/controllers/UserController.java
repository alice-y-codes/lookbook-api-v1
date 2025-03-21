package com.lookbook.user.infrastructure.api.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lookbook.auth.application.dtos.responses.UserResponse;
import com.lookbook.auth.application.mappers.UserMapper;
import com.lookbook.base.domain.exceptions.EntityNotFoundException;
import com.lookbook.base.domain.exceptions.ValidationException;
import com.lookbook.base.infrastructure.api.controllers.BaseController;
import com.lookbook.base.infrastructure.api.response.ApiResponse;
import com.lookbook.user.application.ports.services.UserService;
import com.lookbook.user.domain.aggregates.User;
import com.lookbook.user.infrastructure.api.requests.ChangePasswordRequest;
import com.lookbook.user.infrastructure.api.requests.UpdateEmailRequest;

import jakarta.validation.Valid;

/**
 * Controller for user-related endpoints.
 */
@RestController
@RequestMapping("/api/v1/users")
public class UserController extends BaseController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Get the current authenticated user's profile.
     *
     * @return the user profile
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMyProfile() {
        Authentication authentication = super.getCurrentUser();

        if (authentication == null) {
            throw new ValidationException("Not authenticated");
        }

        String username = authentication.getName();

        User user = userService.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(User.class, username));

        return ResponseEntity.ok(createSuccessResponse(UserMapper.toUserResponse(user)));
    }

    /**
     * Get a user by ID.
     *
     * @param id the user ID
     * @return the user profile
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable UUID id) {
        User user = userService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));

        return ResponseEntity.ok(createSuccessResponse(UserMapper.toUserResponse(user)));
    }

    /**
     * Get all users.
     *
     * @return a list of all users
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<User> users = userService.findAll();

        List<UserResponse> userResponses = users.stream()
                .map(UserMapper::toUserResponse)
                .toList();

        return ResponseEntity.ok(createSuccessResponse(userResponses));
    }

    /**
     * Activate a user.
     *
     * @param id the user ID
     * @return the updated user profile
     */
    @PostMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<UserResponse>> activateUser(@PathVariable UUID id) {
        User user = userService.activate(id);

        return ResponseEntity.ok(
                createSuccessResponse(
                        UserMapper.toUserResponse(user),
                        "User activated successfully"));
    }

    /**
     * Deactivate a user.
     *
     * @param id the user ID
     * @return the updated user profile
     */
    @PostMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<UserResponse>> deactivateUser(@PathVariable UUID id) {
        User user = userService.deactivate(id);

        return ResponseEntity.ok(
                createSuccessResponse(
                        UserMapper.toUserResponse(user),
                        "User deactivated successfully"));
    }

    /**
     * Update a user's email.
     *
     * @param id      the user ID
     * @param request the update email request
     * @return the updated user profile
     */
    @PutMapping("/{id}/email")
    public ResponseEntity<ApiResponse<UserResponse>> updateEmail(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateEmailRequest request) {

        User user = userService.updateEmail(id, request.email());

        return ResponseEntity.ok(
                createSuccessResponse(
                        UserMapper.toUserResponse(user),
                        "Email updated successfully"));
    }

    /**
     * Change a user's password.
     *
     * @param id      the user ID
     * @param request the change password request
     * @return the updated user profile
     */
    @PutMapping("/{id}/password")
    public ResponseEntity<ApiResponse<UserResponse>> changePassword(
            @PathVariable UUID id,
            @Valid @RequestBody ChangePasswordRequest request) {

        User user = userService.changePassword(id, request.currentPassword(), request.newPassword());

        return ResponseEntity.ok(
                createSuccessResponse(
                        UserMapper.toUserResponse(user),
                        "Password changed successfully"));
    }
}