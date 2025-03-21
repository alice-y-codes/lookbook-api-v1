package com.lookbook.base.infrastructure.persistence.repositories;

import com.lookbook.base.domain.entities.TestEntity;
import com.lookbook.base.infrastructure.persistence.entities.JpaTestEntity;

/**
 * Concrete implementation of JpaEntityRepositoryAdapter for TestEntity.
 * This adapter is specifically for testing purposes.
 */
public class TestEntityRepositoryAdapter extends JpaEntityRepositoryAdapter<TestEntity, JpaTestEntity> {

    /**
     * Creates a new TestEntityRepositoryAdapter.
     *
     * @param repository The test entity repository
     */
    public TestEntityRepositoryAdapter(TestEntityRepository repository) {
        super(repository);
    }

    @Override
    protected JpaTestEntity mapToJpaEntity(TestEntity entity) {
        return new JpaTestEntity(entity);
    }

    @Override
    protected TestEntity mapToEntity(JpaTestEntity jpaEntity) {
        return jpaEntity.toDomainEntity();
    }
}