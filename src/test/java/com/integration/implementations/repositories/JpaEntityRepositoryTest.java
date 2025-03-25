package com.integration.implementations.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.integration.contracts.repositories.EntityRepositoryContractTest;
import com.lookbook.base.application.ports.repositories.EntityRepository;
import com.lookbook.base.domain.entities.TestEntity;
import com.lookbook.base.infrastructure.persistence.repositories.TestEntityRepository;
import com.lookbook.base.infrastructure.persistence.repositories.TestEntityRepositoryAdapter;

/**
 * Integration test for the JpaEntityRepositoryAdapter.
 * Verifies that the adapter meets the EntityRepository contract.
 */
public class JpaEntityRepositoryTest extends EntityRepositoryContractTest<TestEntity> {

    @Autowired
    private TestEntityRepository testRepository;

    private EntityRepository<TestEntity> repository;

    public JpaEntityRepositoryTest() {
        super();
    }

    @Override
    protected EntityRepository<TestEntity> createRepository() {
        repository = new TestEntityRepositoryAdapter(testRepository);
        return repository;
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

        // Save each entity using the domain repository
        EntityRepository<TestEntity> repository = createRepository();
        for (TestEntity entity : entities) {
            repository.save(entity);
        }
        testRepository.flush(); // Ensure saves are flushed to the database
    }
}