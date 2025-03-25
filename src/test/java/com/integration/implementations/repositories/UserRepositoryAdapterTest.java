package com.integration.implementations.repositories;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.integration.contracts.repositories.UserRepositoryContractTest;
import com.lookbook.LookbookApplication;
import com.lookbook.user.application.ports.repositories.UserRepository;
import com.lookbook.user.domain.aggregates.User;
import com.lookbook.user.infrastructure.persistence.entities.JpaUser;
import com.lookbook.user.infrastructure.persistence.repositories.UserJpaRepository;
import com.lookbook.user.infrastructure.persistence.repositories.UserRepositoryAdapter;

/**
 * Integration test for the UserRepositoryAdapter.
 * Extends UserRepositoryContractTest to verify this implementation meets the
 * contract.
 */
@SpringBootTest(classes = LookbookApplication.class)
@ExtendWith(SpringExtension.class)
@Testcontainers
@ActiveProfiles("test")
public class UserRepositoryAdapterTest extends UserRepositoryContractTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("lookbook_test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.flyway.enabled", () -> "false");
    }

    @Autowired
    private UserJpaRepository jpaRepository;

    private UserRepositoryAdapter adapter;

    @Override
    protected UserRepository createRepository() {
        adapter = new UserRepositoryAdapter(jpaRepository);
        return adapter;
    }

    @Override
    protected User createEntity(String name) {
        // Generate a short unique identifier (first 8 chars of UUID)
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        // Create a valid username by replacing any hyphens with underscores
        String uniqueUsername = name + "_" + uniqueId.replace("-", "_");
        String uniqueEmail = "test_" + uniqueId + "@example.com";
        return User.register(uniqueUsername, uniqueEmail, "Password1!");
    }

    @Override
    protected void setupTestData(List<User> users) {
        // Clear any existing data first
        jpaRepository.deleteAll();
        jpaRepository.flush(); // Ensure deletion is flushed to the database

        // Convert domain users to JPA entities and save them
        List<JpaUser> jpaUsers = users.stream()
                .map(JpaUser::new)
                .collect(Collectors.toList());

        // Save to JPA repository
        jpaRepository.saveAll(jpaUsers);
        jpaRepository.flush(); // Ensure saves are flushed to the database
    }
}
