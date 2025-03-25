package com.integration.contracts.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.lookbook.base.application.ports.repositories.ReadOnlyRepository;
import com.lookbook.base.domain.entities.TestEntity;

/**
 * Contract test for the ReadOnlyRepository interface.
 * Any implementation of ReadOnlyRepository should extend this test class
 * and implement the createRepository, createEntity, and setupTestData methods.
 */
public abstract class ReadOnlyRepositoryContractTest {

    /**
     * Creates a repository instance for testing.
     * Must be implemented by concrete test classes.
     *
     * @return A repository implementation to test
     */
    protected abstract ReadOnlyRepository<TestEntity> createRepository();

    /**
     * Creates a test entity with the given name.
     * Must be implemented by concrete test classes.
     *
     * @param name The name for the test entity
     * @return A new test entity
     */
    protected abstract TestEntity createEntity(String name);

    /**
     * Sets up test data in the repository.
     * Must be implemented by concrete test classes.
     *
     * @param entities The entities to set up in the repository
     */
    protected abstract void setupTestData(List<TestEntity> entities);

    private List<TestEntity> testEntities;
    protected ReadOnlyRepository<TestEntity> repository;

    @BeforeEach
    protected void setUp() {
        // Initialize repository if not already initialized
        if (repository == null) {
            repository = createRepository();
        }
        // Set up test data
        testEntities = Arrays.asList(
                createEntity("test1"),
                createEntity("test2"),
                createEntity("test3"));
        setupTestData(testEntities);
    }

    @Test
    public void findById_ExistingEntity_ShouldReturnEntity() {
        Optional<TestEntity> found = repository.findById(testEntities.get(0).getId());

        assertTrue(found.isPresent());
        assertEquals(testEntities.get(0).getId(), found.get().getId());
    }

    @Test
    public void findById_NonExistentEntity_ShouldReturnEmpty() {
        Optional<TestEntity> found = repository.findById(UUID.randomUUID());

        assertFalse(found.isPresent());
    }

    @Test
    public void existsById_ExistingEntity_ShouldReturnTrue() {
        assertTrue(repository.existsById(testEntities.get(0).getId()));
    }

    @Test
    public void existsById_NonExistentEntity_ShouldReturnFalse() {
        assertFalse(repository.existsById(UUID.randomUUID()));
    }

    @Test
    public void findAll_ShouldReturnAllEntities() {
        List<TestEntity> all = repository.findAll();

        assertEquals(testEntities.size(), all.size());
        assertTrue(all.containsAll(testEntities));
    }

    @Test
    public void findAllById_ExistingIds_ShouldReturnMatchingEntities() {
        List<TestEntity> found = repository.findAllById(Arrays.asList(
                testEntities.get(0).getId(),
                testEntities.get(2).getId()));

        assertEquals(2, found.size());
        assertTrue(found.contains(testEntities.get(0)));
        assertTrue(found.contains(testEntities.get(2)));
        assertFalse(found.contains(testEntities.get(1)));
    }

    @Test
    public void count_ShouldReturnCorrectCount() {
        assertEquals(testEntities.size(), repository.count());
    }
}