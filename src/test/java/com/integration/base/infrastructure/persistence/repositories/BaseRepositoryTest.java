package com.integration.base.infrastructure.persistence.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.lookbook.base.domain.repositories.BaseRepository;
import com.lookbook.base.domain.entities.BaseEntity;

/**
 * Base test class for repository tests using TestContainers.
 * Provides common test setup and basic CRUD operation tests.
 * 
 * @param <T> The type of entity being tested
 * @param <R> The type of repository being tested
 */
public abstract class BaseRepositoryTest<T extends BaseEntity, R extends BaseRepository<T>>
        extends BaseTestContainersTest {

    protected R repository;

    @BeforeEach
    protected void setUp() {
        // Initialize repository if not already initialized
        if (repository == null) {
            repository = createRepository();
        }
        // Clear any existing data
        clearRepository();
    }

    /**
     * Clears the repository of all data.
     * By default, calls deleteAll() but can be overridden by subclasses.
     */
    protected void clearRepository() {
        repository.deleteAll();
    }

    /**
     * Creates a repository instance for testing.
     * Must be implemented by concrete test classes.
     *
     * @return A repository implementation to test
     */
    protected abstract R createRepository();

    /**
     * Sets up test data in the repository.
     * Must be implemented by concrete test classes.
     *
     * @param entities The entities to set up in the repository
     */
    protected abstract void setupTestData(List<T> entities);

    /**
     * Creates a test entity with a unique name.
     * Must be implemented by concrete test classes.
     *
     * @return A new test entity
     */
    protected abstract T createTestEntity();

    @Test
    public void save_ShouldPersistEntity() {
        T entity = createTestEntity();
        T saved = repository.save(entity);

        assertNotNull(saved.getId());
        assertTrue(repository.existsById(saved.getId()));
    }

    @Test
    public void findById_ShouldReturnEntity_WhenExists() {
        T entity = createTestEntity();
        T saved = repository.save(entity);

        Optional<T> found = repository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
    }

    @Test
    public void findAll_ShouldReturnAllEntities() {
        T entity1 = createTestEntity();
        T entity2 = createTestEntity();
        setupTestData(Arrays.asList(entity1, entity2));

        List<T> found = repository.findAll();

        assertEquals(2, found.size());
        assertTrue(found.stream().anyMatch(e -> e.getId().equals(entity1.getId())));
        assertTrue(found.stream().anyMatch(e -> e.getId().equals(entity2.getId())));
    }

    @Test
    public void deleteById_ShouldRemoveEntity() {
        T entity = createTestEntity();
        T saved = repository.save(entity);

        repository.deleteById(saved.getId());

        assertFalse(repository.existsById(saved.getId()));
    }

    @Test
    public void delete_ShouldRemoveEntity() {
        T entity = createTestEntity();
        T saved = repository.save(entity);

        repository.delete(saved);

        assertFalse(repository.existsById(saved.getId()));
    }

    @Test
    public void existsById_ShouldReturnTrue_WhenEntityExists() {
        T entity = createTestEntity();
        T saved = repository.save(entity);

        assertTrue(repository.existsById(saved.getId()));
    }

    @Test
    public void existsById_ShouldReturnFalse_WhenEntityDoesNotExist() {
        assertFalse(repository.existsById(UUID.randomUUID()));
    }

    @Test
    public void count_ShouldReturnNumberOfEntities() {
        T entity1 = createTestEntity();
        T entity2 = createTestEntity();
        setupTestData(Arrays.asList(entity1, entity2));

        assertEquals(2, repository.count());
    }

    @Test
    public void deleteAll_ShouldRemoveAllEntities() {
        T entity1 = createTestEntity();
        T entity2 = createTestEntity();
        setupTestData(Arrays.asList(entity1, entity2));

        repository.deleteAll();

        assertEquals(0, repository.count());
    }

    @Test
    public void deleteAllById_ShouldRemoveSpecifiedEntities() {
        T entity1 = createTestEntity();
        T entity2 = createTestEntity();
        T entity3 = createTestEntity();
        setupTestData(Arrays.asList(entity1, entity2, entity3));

        repository.deleteAllById(Arrays.asList(entity1.getId(), entity2.getId()));

        assertEquals(1, repository.count());
        assertFalse(repository.existsById(entity1.getId()));
        assertFalse(repository.existsById(entity2.getId()));
        assertTrue(repository.existsById(entity3.getId()));
    }
}