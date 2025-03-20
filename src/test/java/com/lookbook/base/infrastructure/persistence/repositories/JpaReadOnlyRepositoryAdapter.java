package com.lookbook.base.infrastructure.persistence.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.lookbook.base.application.ports.repositories.ReadOnlyRepository;
import com.lookbook.base.domain.entities.TestEntity;

public class JpaReadOnlyRepositoryAdapter implements ReadOnlyRepository<TestEntity> {
    private final JpaEntityRepositoryAdapter delegate;

    public JpaReadOnlyRepositoryAdapter(TestEntityRepository repository) {
        this.delegate = new JpaEntityRepositoryAdapter(repository);
    }

    @Override
    public Optional<TestEntity> findById(UUID id) {
        return delegate.findById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return delegate.existsById(id);
    }

    @Override
    public List<TestEntity> findAll() {
        return delegate.findAll();
    }

    @Override
    public List<TestEntity> findAllById(Iterable<UUID> ids) {
        return delegate.findAllById(ids);
    }

    @Override
    public long count() {
        return delegate.count();
    }
}