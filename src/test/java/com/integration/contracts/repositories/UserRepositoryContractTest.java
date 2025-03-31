package com.integration.contracts.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.lookbook.user.domain.aggregates.User;
import com.lookbook.user.domain.repositories.UserRepository;

/**
 * Contract test for UserRepository interface.
 * Any implementation of UserRepository should extend this test class
 * and implement the createRepository, createEntity, and setupTestData methods.
 */
@Transactional
@Rollback
public abstract class UserRepositoryContractTest {

    /**
     * Creates a repository instance for testing.
     * Must be implemented by concrete test classes.
     *
     * @return A repository implementation to test
     */
    protected abstract UserRepository createRepository();

    /**
     * Creates a test user with the given name.
     * Must be implemented by concrete test classes.
     *
     * @param name The name for the test user
     * @return A new test user
     */
    protected abstract User createEntity(String name);

    /**
     * Sets up test data in the repository.
     * Must be implemented by concrete test classes.
     *
     * @param users The users to set up in the repository
     */
    protected abstract void setupTestData(List<User> users);

    private List<User> testUsers;
    protected UserRepository repository;

    @BeforeEach
    protected void setUp() {
        // Initialize repository if not already initialized
        if (repository == null) {
            repository = createRepository();
        }
        // Set up test data
        testUsers = Arrays.asList(
                createEntity("test1"),
                createEntity("test2"),
                createEntity("test3"));
        setupTestData(testUsers);
    }

    @Test
    void findByUsername_ShouldReturnUser() {
        // Given
        User savedUser = testUsers.get(0);

        // When
        Optional<User> foundUser = repository.findByUsername(savedUser.getUsername());

        // Then
        assertTrue(foundUser.isPresent());
        assertEquals(savedUser.getId(), foundUser.get().getId());
        assertEquals(savedUser.getUsername(), foundUser.get().getUsername());
    }

    @Test
    void findByEmail_ShouldReturnUser() {
        // Given
        User savedUser = testUsers.get(0);

        // When
        Optional<User> foundUser = repository.findByEmail(savedUser.getEmail());

        // Then
        assertTrue(foundUser.isPresent());
        assertEquals(savedUser.getId(), foundUser.get().getId());
        assertEquals(savedUser.getEmail(), foundUser.get().getEmail());
    }

    @Test
    void existsByUsername_ShouldReturnTrue_WhenUsernameExists() {
        // Given
        User savedUser = testUsers.get(0);

        // When
        boolean exists = repository.existsByUsername(savedUser.getUsername());

        // Then
        assertTrue(exists);
    }

    @Test
    void existsByEmail_ShouldReturnTrue_WhenEmailExists() {
        // Given
        User savedUser = testUsers.get(0);

        // When
        boolean exists = repository.existsByEmail(savedUser.getEmail());

        // Then
        assertTrue(exists);
    }
}
