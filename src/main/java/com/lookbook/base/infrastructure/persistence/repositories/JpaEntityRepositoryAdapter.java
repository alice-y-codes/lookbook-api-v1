package com.lookbook.base.infrastructure.persistence.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lookbook.base.application.ports.repositories.EntityRepository;
import com.lookbook.base.domain.entities.BaseEntity;
import com.lookbook.base.infrastructure.persistence.entities.JpaBaseEntity;

/**
 * Generic implementation of the EntityRepository interface using Spring Data
 * JPA.
 * This adapter bridges between the domain repository interfaces and JPA
 * repositories.
 *
 * @param <T> The domain entity type
 * @param <J> The JPA entity type
 */
public abstract class JpaEntityRepositoryAdapter<T extends BaseEntity, J extends JpaBaseEntity>
        implements EntityRepository<T> {

    private final JpaRepository<J, UUID> jpaRepository;

    /**
     * Creates a new JpaEntityRepositoryAdapter.
     *
     * @param jpaRepository The JPA repository to delegate to
     */
    protected JpaEntityRepositoryAdapter(JpaRepository<J, UUID> jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    /**
     * Maps a domain entity to its JPA representation.
     *
     * @param entity The domain entity to map
     * @return The JPA entity
     */
    protected abstract J mapToJpaEntity(T entity);

    /**
     * Maps a JPA entity to its domain representation.
     *
     * @param jpaEntity The JPA entity to map
     * @return The domain entity
     */
    protected abstract T mapToEntity(J jpaEntity);

    @Override
    public T save(T entity) {
        J jpaEntity = mapToJpaEntity(entity);
        return mapToEntity(jpaRepository.save(jpaEntity));
    }

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
                .collect(Collectors.toList());
    }

    @Override
    public List<T> findAllById(Iterable<UUID> ids) {
        return jpaRepository.findAllById(ids).stream()
                .map(this::mapToEntity)
                .collect(Collectors.toList());
    }

    @Override
    public long count() {
        return jpaRepository.count();
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public void delete(T entity) {
        jpaRepository.deleteById(entity.getId());
    }

    @Override
    public void deleteAllById(Iterable<UUID> ids) {
        jpaRepository.deleteAllById(ids);
    }

    @Override
    public void deleteAll() {
        jpaRepository.deleteAll();
    }

    @Override
    public List<T> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end) {
        return jpaRepository.findAll().stream()
                .filter(jpaEntity -> {
                    LocalDateTime createdAt = jpaEntity.getCreatedAt();
                    return !createdAt.isBefore(start) && !createdAt.isAfter(end);
                })
                .map(this::mapToEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<T> findByUpdatedAtBetween(LocalDateTime start, LocalDateTime end) {
        return jpaRepository.findAll().stream()
                .filter(jpaEntity -> {
                    LocalDateTime updatedAt = jpaEntity.getUpdatedAt();
                    return !updatedAt.isBefore(start) && !updatedAt.isAfter(end);
                })
                .map(this::mapToEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<T> findByCreatedAtBefore(LocalDateTime timestamp) {
        return jpaRepository.findAll().stream()
                .filter(jpaEntity -> jpaEntity.getCreatedAt().isBefore(timestamp))
                .map(this::mapToEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<T> findByUpdatedAtBefore(LocalDateTime timestamp) {
        return jpaRepository.findAll().stream()
                .filter(jpaEntity -> jpaEntity.getUpdatedAt().isBefore(timestamp))
                .map(this::mapToEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<T> findByCreatedAtAfter(LocalDateTime timestamp) {
        return jpaRepository.findAll().stream()
                .filter(jpaEntity -> jpaEntity.getCreatedAt().isAfter(timestamp))
                .map(this::mapToEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<T> findByUpdatedAtAfter(LocalDateTime timestamp) {
        return jpaRepository.findAll().stream()
                .filter(jpaEntity -> jpaEntity.getUpdatedAt().isAfter(timestamp))
                .map(this::mapToEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<T> findMostRecent(int limit) {
        return jpaRepository.findAll(PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdAt")))
                .getContent().stream()
                .map(this::mapToEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<T> findMostRecentlyUpdated(int limit) {
        return jpaRepository.findAll(PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "updatedAt")))
                .getContent().stream()
                .map(this::mapToEntity)
                .collect(Collectors.toList());
    }
}