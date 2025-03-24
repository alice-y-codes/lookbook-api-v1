package com.lookbook.base.infrastructure.persistence.repositories;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.lookbook.base.application.ports.repositories.EntityRepository;
import com.lookbook.base.application.ports.repositories.EntityRepositoryContractTest;
import com.lookbook.base.domain.entities.TestEntity;
import com.lookbook.base.infrastructure.persistence.entities.JpaTestEntity;

/**
 * Integration test for the JpaEntityRepositoryAdapter.
 * Verifies that the adapter meets the EntityRepository contract.
 */
@DataJpaTest
@ActiveProfiles("test")
public class JpaEntityRepositoryTest extends EntityRepositoryContractTest {

    @Autowired
    private TestEntityRepository testRepository;

    @Override
    protected EntityRepository<TestEntity> createRepository() {
        return new TestEntityRepositoryAdapter(testRepository);
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
}