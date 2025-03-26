package com.integration.contracts.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.lookbook.base.domain.repositories.BaseRepository;
import com.lookbook.base.domain.entities.TestEntity;

/**
 * Contract test for the BaseRepository interface.
 * Any implementation of BaseRepository should extend this test class
 * and implement the createRepository and createEntity methods.
 */
public abstract class BaseRepositoryContractTest {

    /**
     * Creates a repository instance for testing.
     * Must be implemented by concrete test classes.
     */
    protected abstract BaseRepository<TestEntity> createRepository();

    /**
     * Creates a test entity with the given name.
     * Must be implemented by concrete test classes.
     */
    protected abstract TestEntity createEntity(String name);

    /**
     * Sets up test data in the repository.
     * Must be implemented by concrete test classes.
     *
     * @param entities The entities to set up in the repository
     */
    protected abstract void setupTestData(List<TestEntity> entities);

    @Test
    void save_NewEntity_ShouldAssignId() {
        BaseRepository<TestEntity> repository = createRepository();
        TestEntity entity = createEntity("test");
        setupTestData(Arrays.asList(entity));

        assertNotNull(entity.getId());
        assertEquals("test", entity.getName());
    }

    @Test
    void findById_ExistingEntity_ShouldReturnEntity() {
        BaseRepository<TestEntity> repository = createRepository();
        TestEntity entity = createEntity("test");
        setupTestData(Arrays.asList(entity));

        Optional<TestEntity> found = repository.findById(entity.getId());

        assertTrue(found.isPresent());
        assertEquals(entity.getId(), found.get().getId());
        assertEquals("test", found.get().getName());
    }

    @Test
    void findById_NonExistentEntity_ShouldReturnEmpty() {
        BaseRepository<TestEntity> repository = createRepository();
        Optional<TestEntity> found = repository.findById(UUID.randomUUID());

        assertFalse(found.isPresent());
    }

    @Test
    void existsById_ExistingEntity_ShouldReturnTrue() {
        BaseRepository<TestEntity> repository = createRepository();
        TestEntity entity = createEntity("test");
        setupTestData(Arrays.asList(entity));

        assertTrue(repository.existsById(entity.getId()));
    }

    @Test
    void existsById_NonExistentEntity_ShouldReturnFalse() {
        BaseRepository<TestEntity> repository = createRepository();
        assertFalse(repository.existsById(UUID.randomUUID()));
    }

    @Test
    void findAll_MultipleEntities_ShouldReturnAll() {
        BaseRepository<TestEntity> repository = createRepository();
        TestEntity entity1 = createEntity("test1");
        TestEntity entity2 = createEntity("test2");
        setupTestData(Arrays.asList(entity1, entity2));

        List<TestEntity> all = repository.findAll();

        assertTrue(all.contains(entity1));
        assertTrue(all.contains(entity2));
        assertEquals(2, all.size());
    }

    @Test
    void findAllById_ExistingIds_ShouldReturnMatchingEntities() {
        BaseRepository<TestEntity> repository = createRepository();
        TestEntity entity1 = createEntity("test1");
        TestEntity entity2 = createEntity("test2");
        TestEntity entity3 = createEntity("test3");
        setupTestData(Arrays.asList(entity1, entity2, entity3));

        List<TestEntity> found = repository.findAllById(Arrays.asList(entity1.getId(), entity3.getId()));

        assertEquals(2, found.size());
        assertTrue(found.contains(entity1));
        assertTrue(found.contains(entity3));
        assertFalse(found.contains(entity2));
    }

    @Test
    void count_MultipleEntities_ShouldReturnCorrectCount() {
        BaseRepository<TestEntity> repository = createRepository();
        TestEntity entity1 = createEntity("test1");
        TestEntity entity2 = createEntity("test2");
        TestEntity entity3 = createEntity("test3");
        setupTestData(Arrays.asList(entity1, entity2, entity3));

        assertEquals(3, repository.count());
    }

    @Test
    void deleteById_ExistingEntity_ShouldRemoveEntity() {
        BaseRepository<TestEntity> repository = createRepository();
        TestEntity entity = createEntity("test");
        setupTestData(Arrays.asList(entity));

        repository.deleteById(entity.getId());

        assertFalse(repository.existsById(entity.getId()));
    }

    @Test
    void delete_ExistingEntity_ShouldRemoveEntity() {
        BaseRepository<TestEntity> repository = createRepository();
        TestEntity entity = createEntity("test");
        setupTestData(Arrays.asList(entity));

        repository.delete(entity);

        assertFalse(repository.existsById(entity.getId()));
    }

    @Test
    void deleteAllById_MultipleEntities_ShouldRemoveAll() {
        BaseRepository<TestEntity> repository = createRepository();
        TestEntity entity1 = repository.save(createEntity("test1"));
        TestEntity entity2 = repository.save(createEntity("test2"));
        TestEntity entity3 = repository.save(createEntity("test3"));

        repository.deleteAllById(Arrays.asList(entity1.getId(), entity2.getId()));

        assertFalse(repository.existsById(entity1.getId()));
        assertFalse(repository.existsById(entity2.getId()));
        assertTrue(repository.existsById(entity3.getId()));
    }

    @Test
    void deleteAll_MultipleEntities_ShouldRemoveAll() {
        BaseRepository<TestEntity> repository = createRepository();
        repository.save(createEntity("test1"));
        repository.save(createEntity("test2"));
        repository.save(createEntity("test3"));

        repository.deleteAll();

        assertEquals(0, repository.count());
    }
}