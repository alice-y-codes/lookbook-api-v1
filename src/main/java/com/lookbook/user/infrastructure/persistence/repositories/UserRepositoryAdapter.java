package com.lookbook.user.infrastructure.persistence.repositories;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.lookbook.base.infrastructure.persistence.repositories.JpaEntityRepositoryAdapter;
import com.lookbook.user.application.ports.repositories.UserRepository;
import com.lookbook.user.domain.aggregates.User;
import com.lookbook.user.domain.valueobjects.Email;
import com.lookbook.user.domain.valueobjects.Username;
import com.lookbook.user.infrastructure.persistence.entities.JpaUser;

/**
 * Implementation of the UserRepository interface using Spring Data JPA.
 * Extends the generic JpaEntityRepositoryAdapter for common CRUD and time-based
 * operations.
 */
@Component
public class UserRepositoryAdapter extends JpaEntityRepositoryAdapter<User, JpaUser> implements UserRepository {

    private final UserJpaRepository jpaRepository;

    /**
     * Creates a new UserRepositoryAdapter.
     *
     * @param jpaRepository The Spring Data JPA repository to delegate to
     */
    public UserRepositoryAdapter(UserJpaRepository jpaRepository) {
        super(jpaRepository);
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<User> findByUsername(Username username) {
        return jpaRepository.findByUsername(username.getValue())
                .map(this::mapToEntity);
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return jpaRepository.findByEmail(email.getValue())
                .map(this::mapToEntity);
    }

    @Override
    public boolean existsByUsername(Username username) {
        return jpaRepository.existsByUsername(username.getValue());
    }

    @Override
    public boolean existsByEmail(Email email) {
        return jpaRepository.existsByEmail(email.getValue());
    }

    @Override
    protected JpaUser mapToJpaEntity(User entity) {
        return new JpaUser(entity);
    }

    @Override
    protected User mapToEntity(JpaUser jpaEntity) {
        return jpaEntity.toDomainEntity();
    }
}