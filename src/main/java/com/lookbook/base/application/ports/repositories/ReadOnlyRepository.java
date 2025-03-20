package com.lookbook.base.application.ports.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.lookbook.base.domain.entities.BaseEntity;

/**
 * Repository interface for read-only operations.
 * Used when entities should not be modified through this repository.
 * Extends BaseRepository but prohibits mutation operations by throwing
 * UnsupportedOperationException.
 *
 * @param <T> The type of entity this repository manages (must extend
 *            BaseEntity)
 */
public interface ReadOnlyRepository<T extends BaseEntity> extends BaseRepository<T> {

    @Override
    default T save(T entity) {
        throw new UnsupportedOperationException("This is a read-only repository");
    }

    @Override
    default void deleteById(UUID id) {
        throw new UnsupportedOperationException("This is a read-only repository");
    }

    @Override
    default void delete(T entity) {
        throw new UnsupportedOperationException("This is a read-only repository");
    }

    @Override
    default void deleteAllById(Iterable<UUID> ids) {
        throw new UnsupportedOperationException("This is a read-only repository");
    }

    @Override
    default void deleteAll() {
        throw new UnsupportedOperationException("This is a read-only repository");
    }

    /**
     * Finds an entity by its ID.
     *
     * @param id The entity's unique identifier
     * @return An Optional containing the entity if found, empty otherwise
     */
    Optional<T> findById(UUID id);

    /**
     * Checks if an entity with the given ID exists.
     *
     * @param id The entity's unique identifier
     * @return true if an entity with the ID exists, false otherwise
     */
    boolean existsById(UUID id);

    /**
     * Finds all entities of this type.
     *
     * @return A list of all entities
     */
    List<T> findAll();

    /**
     * Finds all entities with the given IDs.
     *
     * @param ids The collection of entity IDs to find
     * @return A list of found entities (may be smaller than the input list if some
     *         IDs don't exist)
     */
    List<T> findAllById(Iterable<UUID> ids);

    /**
     * Counts the total number of entities.
     *
     * @return The total count
     */
    long count();
}