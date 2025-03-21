package com.lookbook.user.infrastructure.persistence.repositories;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.lookbook.user.application.ports.repositories.UserRepository;
import com.lookbook.user.application.ports.repositories.UserRepositoryContractTest;
import com.lookbook.user.domain.aggregates.User;
import com.lookbook.user.infrastructure.persistence.config.UserJpaConfig;
import com.lookbook.user.infrastructure.persistence.entities.JpaUser;

/**
 * Integration test for the UserRepositoryAdapter.
 * Extends UserRepositoryContractTest to verify this implementation meets the
 * contract.
 */
@DataJpaTest
@Import(UserJpaConfig.class)
@ActiveProfiles("test")
@Transactional
public class UserRepositoryAdapterTest extends UserRepositoryContractTest {

    @Autowired
    private UserJpaRepository jpaRepository;

    @Override
    protected UserRepository createRepository() {
        return new UserRepositoryAdapter(jpaRepository);
    }

    @Override
    protected User createUser(String username, String email) {
        return User.register(username, email, "Password123!");
    }

    @Override
    protected void setupTestData(List<User> users) {
        // Clear any existing data first
        jpaRepository.deleteAll();

        // Convert domain users to JPA entities and save them
        List<JpaUser> jpaUsers = users.stream()
                .map(JpaUser::new)
                .collect(Collectors.toList());

        // Save to JPA repository
        jpaRepository.saveAll(jpaUsers);
    }
}
