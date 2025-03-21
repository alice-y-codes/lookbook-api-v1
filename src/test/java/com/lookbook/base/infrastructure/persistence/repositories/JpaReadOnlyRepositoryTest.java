package com.lookbook.base.infrastructure.persistence.repositories;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.lookbook.base.application.ports.repositories.ReadOnlyRepository;
import com.lookbook.base.application.ports.repositories.ReadOnlyRepositoryContractTest;
import com.lookbook.base.domain.entities.TestEntity;
import com.lookbook.base.infrastructure.persistence.config.TestJpaConfig;
import com.lookbook.base.infrastructure.persistence.entities.JpaTestEntity;

/**
 * Integration test for the JpaReadOnlyRepositoryAdapter.
 * Verifies that the adapter meets the ReadOnlyRepository contract.
 */
@DataJpaTest
@Import(TestJpaConfig.class)
@ActiveProfiles("test")
public class JpaReadOnlyRepositoryTest extends ReadOnlyRepositoryContractTest {

    @Autowired
    private TestEntityRepository testRepository;

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

        // Convert domain entities to JPA entities and save them
        List<JpaTestEntity> jpaEntities = entities.stream()
                .map(JpaTestEntity::new)
                .collect(Collectors.toList());
        testRepository.saveAll(jpaEntities);
    }
}