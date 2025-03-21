package com.lookbook.user.application.ports.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.lookbook.user.domain.aggregates.User;
import com.lookbook.user.domain.valueobjects.Email;
import com.lookbook.user.domain.valueobjects.Username;

/**
 * Contract test for the UserRepository interface.
 * Extends EntityRepositoryContractTest to inherit tests for standard repository
 * operations.
 * Any implementation of UserRepository should extend this test.
 */
public abstract class UserRepositoryContractTest {

    /**
     * Creates a repository for testing.
     * Must be implemented by concrete test classes.
     */
    protected abstract UserRepository createRepository();

    /**
     * Creates a user with the given username and email for testing.
     */
    protected abstract User createUser(String username, String email);

    /**
     * Sets up test data in the repository.
     * Must be implemented by concrete test classes.
     *
     * @param users The users to set up in the repository
     */
    protected abstract void setupTestData(List<User> users);

    @Test
    void findByUsername_ExistingUsername_ShouldReturnUser() {
        // Arrange
        User user = createUser("testuser", "test@example.com");
        UserRepository repository = createRepository();
        setupTestData(List.of(user));

        // Act
        Optional<User> result = repository.findByUsername(user.getUsername());

        // Assert
        assertTrue(result.isPresent());
        assertEquals(user.getId(), result.get().getId());
    }

    @Test
    void findByUsername_NonExistentUsername_ShouldReturnEmpty() {
        // Arrange
        UserRepository repository = createRepository();
        Username nonExistentUsername = Username.of("nonexistent");

        // Act
        Optional<User> result = repository.findByUsername(nonExistentUsername);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void findByEmail_ExistingEmail_ShouldReturnUser() {
        // Arrange
        User user = createUser("testuser", "test@example.com");
        UserRepository repository = createRepository();
        setupTestData(List.of(user));

        // Act
        Optional<User> result = repository.findByEmail(user.getEmail());

        // Assert
        assertTrue(result.isPresent());
        assertEquals(user.getId(), result.get().getId());
    }

    @Test
    void findByEmail_NonExistentEmail_ShouldReturnEmpty() {
        // Arrange
        UserRepository repository = createRepository();
        Email nonExistentEmail = Email.of("nonexistent@example.com");

        // Act
        Optional<User> result = repository.findByEmail(nonExistentEmail);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void existsByUsername_ExistingUsername_ShouldReturnTrue() {
        // Arrange
        User user = createUser("testuser", "test@example.com");
        UserRepository repository = createRepository();
        setupTestData(List.of(user));

        // Act
        boolean exists = repository.existsByUsername(user.getUsername());

        // Assert
        assertTrue(exists);
    }

    @Test
    void existsByUsername_NonExistentUsername_ShouldReturnFalse() {
        // Arrange
        UserRepository repository = createRepository();
        Username nonExistentUsername = Username.of("nonexistent");

        // Act
        boolean exists = repository.existsByUsername(nonExistentUsername);

        // Assert
        assertFalse(exists);
    }

    @Test
    void existsByEmail_ExistingEmail_ShouldReturnTrue() {
        // Arrange
        User user = createUser("testuser", "test@example.com");
        UserRepository repository = createRepository();
        setupTestData(List.of(user));

        // Act
        boolean exists = repository.existsByEmail(user.getEmail());

        // Assert
        assertTrue(exists);
    }

    @Test
    void existsByEmail_NonExistentEmail_ShouldReturnFalse() {
        // Arrange
        UserRepository repository = createRepository();
        Email nonExistentEmail = Email.of("nonexistent@example.com");

        // Act
        boolean exists = repository.existsByEmail(nonExistentEmail);

        // Assert
        assertFalse(exists);
    }
}
