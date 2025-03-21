package com.lookbook.base.infrastructure.persistence.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lookbook.base.application.ports.repositories.ReadOnlyRepository;
import com.lookbook.base.domain.entities.BaseEntity;
import com.lookbook.base.infrastructure.persistence.entities.JpaBaseEntity;

/**
 * Generic implementation of the ReadOnlyRepository interface using Spring Data
 * JPA.
 * This adapter provides read-only operations by delegating to a JpaRepository.
 *
 * @param <T> The domain entity type
 * @param <J> The JPA entity type
 */
public abstract class JpaReadOnlyRepositoryAdapter<T extends BaseEntity, J extends JpaBaseEntity>
        implements ReadOnlyRepository<T> {

    private final JpaRepository<J, UUID> jpaRepository;

    /**
     * Creates a new JpaReadOnlyRepositoryAdapter.
     *
     * @param jpaRepository The JPA repository to delegate to
     */
    protected JpaReadOnlyRepositoryAdapter(JpaRepository<J, UUID> jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    /**
     * Maps a JPA entity to its domain representation.
     *
     * @param jpaEntity The JPA entity to map
     * @return The domain entity
     */
    protected abstract T mapToEntity(J jpaEntity);

    @Override
    public Optional<T> findById(UUID id) {
        return jpaRepository.findById(id).map(this::mapToEntity);
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public List<T> findAll() {
        return jpaRepository.findAll().stream()
                .map(this::mapToEntity)
                .toList();
    }

    @Override
    public List<T> findAllById(Iterable<UUID> ids) {
        return jpaRepository.findAllById(ids).stream()
                .map(this::mapToEntity)
                .toList();
    }

    @Override
    public long count() {
        return jpaRepository.count();
    }
}