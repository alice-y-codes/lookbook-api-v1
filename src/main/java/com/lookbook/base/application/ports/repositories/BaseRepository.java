package com.lookbook.base.application.ports.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.lookbook.base.domain.entities.BaseEntity;

/**
 * Base repository interface for all domain repositories.
 * Defines standard CRUD operations that all repositories should support.
 *
 * @param <T> The type of entity this repository manages (must extend
 *            BaseEntity)
 */
public interface BaseRepository<T extends BaseEntity> {

    /**
     * Saves an entity to the repository.
     * If the entity already exists (has an ID), it will be updated.
     *
     * @param entity The entity to save
     * @return The saved entity (may have updated fields like ID or timestamps)
     */
    T save(T entity);

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

    /**
     * Deletes an entity by its ID.
     *
     * @param id The entity's unique identifier
     */
    void deleteById(UUID id);

    /**
     * Deletes the given entity.
     *
     * @param entity The entity to delete
     */
    void delete(T entity);

    /**
     * Deletes all entities with the given IDs.
     *
     * @param ids The collection of entity IDs to delete
     */
    void deleteAllById(Iterable<UUID> ids);

    /**
     * Deletes all entities of this type.
     */
    void deleteAll();
}