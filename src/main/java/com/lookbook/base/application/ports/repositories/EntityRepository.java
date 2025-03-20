package com.lookbook.base.application.ports.repositories;

import java.time.LocalDateTime;
import java.util.List;

import com.lookbook.base.domain.entities.BaseEntity;

/**
 * Extended repository interface for domain entities.
 * Adds domain-specific operations beyond basic CRUD.
 *
 * @param <T> The type of entity this repository manages (must extend
 *            BaseEntity)
 */
public interface EntityRepository<T extends BaseEntity> extends BaseRepository<T> {

    /**
     * Finds entities created within a time range.
     *
     * @param start The start of the time range (inclusive)
     * @param end   The end of the time range (inclusive)
     * @return A list of entities created within the range
     */
    List<T> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    /**
     * Finds entities last updated within a time range.
     *
     * @param start The start of the time range (inclusive)
     * @param end   The end of the time range (inclusive)
     * @return A list of entities updated within the range
     */
    List<T> findByUpdatedAtBetween(LocalDateTime start, LocalDateTime end);

    /**
     * Finds entities created before a specific time.
     *
     * @param timestamp The cutoff timestamp
     * @return A list of entities created before the timestamp
     */
    List<T> findByCreatedAtBefore(LocalDateTime timestamp);

    /**
     * Finds entities last updated before a specific time.
     *
     * @param timestamp The cutoff timestamp
     * @return A list of entities updated before the timestamp
     */
    List<T> findByUpdatedAtBefore(LocalDateTime timestamp);

    /**
     * Finds entities created after a specific time.
     *
     * @param timestamp The cutoff timestamp
     * @return A list of entities created after the timestamp
     */
    List<T> findByCreatedAtAfter(LocalDateTime timestamp);

    /**
     * Finds entities last updated after a specific time.
     *
     * @param timestamp The cutoff timestamp
     * @return A list of entities updated after the timestamp
     */
    List<T> findByUpdatedAtAfter(LocalDateTime timestamp);

    /**
     * Finds the most recently created entities.
     *
     * @param limit Maximum number of entities to return
     * @return A list of entities, ordered by creation time descending
     */
    List<T> findMostRecent(int limit);

    /**
     * Finds the most recently updated entities.
     *
     * @param limit Maximum number of entities to return
     * @return A list of entities, ordered by last update time descending
     */
    List<T> findMostRecentlyUpdated(int limit);
}