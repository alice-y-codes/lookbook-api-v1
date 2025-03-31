package com.lookbook.base.infrastructure.persistence.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lookbook.base.domain.repositories.BaseRepository;
import com.lookbook.base.domain.entities.BaseEntity;

public class JpaBaseRepositoryAdapter<T extends BaseEntity> implements BaseRepository<T> {

    private final JpaRepository<T, UUID> jpaRepository;

    public JpaBaseRepositoryAdapter(JpaRepository<T, UUID> jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public T save(T entity) {
        return jpaRepository.save(entity);
    }

    @Override
    public Optional<T> findById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public List<T> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public List<T> findAllById(Iterable<UUID> ids) {
        return jpaRepository.findAllById(ids);
    }

    @Override
    public long count() {
        return jpaRepository.count();
    }

    @Override
    public void delete(T entity) {
        jpaRepository.delete(entity);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public void deleteAllById(Iterable<UUID> ids) {
        jpaRepository.deleteAllById(ids);
    }

    @Override
    public void deleteAll() {
        jpaRepository.deleteAll();
    }
}