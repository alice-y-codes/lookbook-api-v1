package com.lookbook.base.infrastructure.persistence.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.lookbook.base.application.ports.repositories.EntityRepository;
import com.lookbook.base.application.ports.repositories.ReadOnlyRepository;
import com.lookbook.base.domain.entities.TestEntity;
import com.lookbook.base.infrastructure.persistence.entities.JpaTestEntity;

public class JpaEntityRepositoryAdapter implements EntityRepository<TestEntity>, ReadOnlyRepository<TestEntity> {

    private final TestEntityRepository jpaRepository;

    public JpaEntityRepositoryAdapter(TestEntityRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public TestEntity save(TestEntity entity) {
        JpaTestEntity jpaEntity = new JpaTestEntity(entity);
        return jpaRepository.save(jpaEntity).toDomainEntity();
    }

    @Override
    public Optional<TestEntity> findById(UUID id) {
        return jpaRepository.findById(id).map(JpaTestEntity::toDomainEntity);
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public List<TestEntity> findAll() {
        return jpaRepository.findAll().stream()
                .map(JpaTestEntity::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<TestEntity> findAllById(Iterable<UUID> ids) {
        return jpaRepository.findAllById(ids).stream()
                .map(JpaTestEntity::toDomainEntity)
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
    public void delete(TestEntity entity) {
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
    public List<TestEntity> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end) {
        return jpaRepository.findByCreatedAtBetween(start, end).stream()
                .map(JpaTestEntity::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<TestEntity> findByUpdatedAtBetween(LocalDateTime start, LocalDateTime end) {
        return jpaRepository.findByUpdatedAtBetween(start, end).stream()
                .map(JpaTestEntity::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<TestEntity> findByCreatedAtBefore(LocalDateTime timestamp) {
        return jpaRepository.findByCreatedAtBefore(timestamp).stream()
                .map(JpaTestEntity::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<TestEntity> findByUpdatedAtBefore(LocalDateTime timestamp) {
        return jpaRepository.findByUpdatedAtBefore(timestamp).stream()
                .map(JpaTestEntity::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<TestEntity> findByCreatedAtAfter(LocalDateTime timestamp) {
        return jpaRepository.findByCreatedAtAfter(timestamp).stream()
                .map(JpaTestEntity::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<TestEntity> findByUpdatedAtAfter(LocalDateTime timestamp) {
        return jpaRepository.findByUpdatedAtAfter(timestamp).stream()
                .map(JpaTestEntity::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<TestEntity> findMostRecent(int limit) {
        return jpaRepository.findAll(PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdAt")))
                .getContent().stream()
                .map(JpaTestEntity::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<TestEntity> findMostRecentlyUpdated(int limit) {
        return jpaRepository.findAll(PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "updatedAt")))
                .getContent().stream()
                .map(JpaTestEntity::toDomainEntity)
                .collect(Collectors.toList());
    }
}