package com.lookbook.base.infrastructure.persistence.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lookbook.base.infrastructure.persistence.entities.JpaTestEntity;

@Repository
public interface TestEntityRepository extends JpaRepository<JpaTestEntity, UUID> {
    List<JpaTestEntity> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    List<JpaTestEntity> findByUpdatedAtBetween(LocalDateTime start, LocalDateTime end);

    List<JpaTestEntity> findByCreatedAtBefore(LocalDateTime timestamp);

    List<JpaTestEntity> findByUpdatedAtBefore(LocalDateTime timestamp);

    List<JpaTestEntity> findByCreatedAtAfter(LocalDateTime timestamp);

    List<JpaTestEntity> findByUpdatedAtAfter(LocalDateTime timestamp);
}