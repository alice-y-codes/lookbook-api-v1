package com.lookbook.base.infrastructure.persistence.repositories;

import com.lookbook.base.domain.entities.TestEntity;
import com.lookbook.base.infrastructure.persistence.entities.JpaTestEntity;

/**
 * Concrete implementation of JpaReadOnlyRepositoryAdapter for TestEntity.
 * This adapter is specifically for testing purposes.
 */
public class TestReadOnlyRepositoryAdapter extends JpaReadOnlyRepositoryAdapter<TestEntity, JpaTestEntity> {

    /**
     * Creates a new TestReadOnlyRepositoryAdapter.
     *
     * @param repository The test entity repository
     */
    public TestReadOnlyRepositoryAdapter(TestEntityRepository repository) {
        super(repository);
    }

    @Override
    protected TestEntity mapToEntity(JpaTestEntity jpaEntity) {
        return jpaEntity.toDomainEntity();
    }
}