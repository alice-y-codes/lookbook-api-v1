package com.lookbook.base.application.ports.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.lookbook.base.domain.entities.TestEntity;

/**
 * Contract test for the EntityRepository interface.
 * Any implementation of EntityRepository should extend this test class
 * and implement the createRepository and createEntity methods from
 * BaseRepositoryContractTest.
 */
public abstract class EntityRepositoryContractTest extends BaseRepositoryContractTest {

    protected abstract EntityRepository<TestEntity> createRepository();

    protected EntityRepository<TestEntity> repository;
    protected LocalDateTime now;

    @BeforeEach
    void setUp() {
        repository = createRepository();
        now = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);
    }

    @Test
    void findByCreatedAtBetween_ShouldReturnEntitiesInRange() {
        TestEntity entity1 = createEntity("test1");
        TestEntity entity2 = createEntity("test2");
        TestEntity entity3 = createEntity("test3");
        setupTestData(Arrays.asList(entity1, entity2, entity3));

        LocalDateTime start = now.minusHours(1);
        LocalDateTime end = now.plusHours(1);

        List<TestEntity> found = repository.findByCreatedAtBetween(start, end);

        assertEquals(3, found.size());
        assertTrue(found.stream().allMatch(e -> e.getCreatedAt().isAfter(start) &&
                e.getCreatedAt().isBefore(end)));
    }

    @Test
    void findByUpdatedAtBetween_ShouldReturnEntitiesInRange() {
        TestEntity entity1 = createEntity("test1");
        TestEntity entity2 = createEntity("test2");
        TestEntity entity3 = createEntity("test3");
        setupTestData(Arrays.asList(entity1, entity2, entity3));

        LocalDateTime start = now.minusHours(1);
        LocalDateTime end = now.plusHours(1);

        List<TestEntity> found = repository.findByUpdatedAtBetween(start, end);

        assertEquals(3, found.size());
        assertTrue(found.stream().allMatch(e -> e.getUpdatedAt().isAfter(start) &&
                e.getUpdatedAt().isBefore(end)));
    }

    @Test
    void findByCreatedAtBefore_ShouldReturnOlderEntities() {
        TestEntity entity = createEntity("test");
        setupTestData(Arrays.asList(entity));

        List<TestEntity> found = repository.findByCreatedAtBefore(now.plusHours(1));

        assertEquals(1, found.size());
        assertTrue(found.get(0).getCreatedAt().isBefore(now.plusHours(1)));
    }

    @Test
    void findByUpdatedAtBefore_ShouldReturnOlderEntities() {
        TestEntity entity = createEntity("test");
        setupTestData(Arrays.asList(entity));

        List<TestEntity> found = repository.findByUpdatedAtBefore(now.plusHours(1));

        assertEquals(1, found.size());
        assertTrue(found.get(0).getUpdatedAt().isBefore(now.plusHours(1)));
    }

    @Test
    void findByCreatedAtAfter_ShouldReturnNewerEntities() {
        TestEntity entity = createEntity("test");
        setupTestData(Arrays.asList(entity));

        List<TestEntity> found = repository.findByCreatedAtAfter(now.minusHours(1));

        assertEquals(1, found.size());
        assertTrue(found.get(0).getCreatedAt().isAfter(now.minusHours(1)));
    }

    @Test
    void findByUpdatedAtAfter_ShouldReturnNewerEntities() {
        TestEntity entity = createEntity("test");
        setupTestData(Arrays.asList(entity));

        List<TestEntity> found = repository.findByUpdatedAtAfter(now.minusHours(1));

        assertEquals(1, found.size());
        assertTrue(found.get(0).getUpdatedAt().isAfter(now.minusHours(1)));
    }

    @Test
    void findMostRecent_ShouldReturnLimitedEntities() {
        TestEntity entity1 = createEntity("test1");
        TestEntity entity2 = createEntity("test2");
        TestEntity entity3 = createEntity("test3");
        TestEntity entity4 = createEntity("test4");
        TestEntity entity5 = createEntity("test5");
        setupTestData(Arrays.asList(entity1, entity2, entity3, entity4, entity5));

        List<TestEntity> found = repository.findMostRecent(3);

        assertEquals(3, found.size());
        // Verify entities are ordered by creation time (most recent first)
        for (int i = 1; i < found.size(); i++) {
            assertTrue(found.get(i - 1).getCreatedAt().isAfter(found.get(i).getCreatedAt()) ||
                    found.get(i - 1).getCreatedAt().equals(found.get(i).getCreatedAt()));
        }
    }
}