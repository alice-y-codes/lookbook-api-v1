package com.integration.implementations.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.integration.contracts.repositories.ReadOnlyRepositoryContractTest;
import com.lookbook.LookbookApplication;
import com.lookbook.base.domain.repositories.ReadOnlyRepository;
import com.lookbook.base.domain.entities.TestEntity;
import com.lookbook.base.infrastructure.persistence.entities.JpaTestEntity;
import com.lookbook.base.infrastructure.persistence.repositories.TestEntityRepository;
import com.lookbook.base.infrastructure.persistence.repositories.TestReadOnlyRepositoryAdapter;

/**
 * Integration test for the JpaReadOnlyRepositoryAdapter.
 * Verifies that the adapter meets the ReadOnlyRepository contract.
 */
@SpringBootTest(classes = LookbookApplication.class)
@ExtendWith(SpringExtension.class)
public class JpaReadOnlyRepositoryTest extends ReadOnlyRepositoryContractTest {

    @Autowired
    private TestEntityRepository testRepository;

    public JpaReadOnlyRepositoryTest() {
        super();
    }

    @Override
    protected ReadOnlyRepository<TestEntity> createRepository() {
        return new TestReadOnlyRepositoryAdapter(testRepository);
    }

    @Override
    protected TestEntity createEntity(String name) {
        return new TestEntity(name);
    }

    @Override
    protected void setupTestData(List<TestEntity> entities) {
        // Clear any existing data
        testRepository.deleteAll();
        testRepository.flush(); // Ensure deletion is flushed to the database

        // Convert domain entities to JPA entities and save them
        List<JpaTestEntity> jpaEntities = entities.stream()
                .map(JpaTestEntity::new)
                .collect(Collectors.toList());
        testRepository.saveAll(jpaEntities);
        testRepository.flush(); // Ensure saves are flushed to the database
    }

    @AfterEach
    void cleanup() {
        testRepository.deleteAll();
        testRepository.flush();
    }

    @Test
    @Override
    public void findById_ExistingEntity_ShouldReturnEntity() {
        TestEntity entity = createEntity("test");
        JpaTestEntity jpaEntity = new JpaTestEntity(entity);
        testRepository.save(jpaEntity);
        testRepository.flush();

        Optional<TestEntity> found = repository.findById(entity.getId());

        assertTrue(found.isPresent());
        assertEquals(entity.getId(), found.get().getId());
    }

    @Test
    @Override
    public void existsById_ExistingEntity_ShouldReturnTrue() {
        TestEntity entity = createEntity("test");
        JpaTestEntity jpaEntity = new JpaTestEntity(entity);
        testRepository.save(jpaEntity);
        testRepository.flush();

        assertTrue(repository.existsById(entity.getId()));
    }
}