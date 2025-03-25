package com.integration.base.infrastructure.persistence.repositories;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.lookbook.LookbookApplication;

/**
 * Base test class for TestContainers configuration.
 * Provides common database setup for tests.
 */
@SpringBootTest(classes = LookbookApplication.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Testcontainers
@Transactional
public abstract class BaseTestContainersTest {

        @Container
        static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
                        DockerImageName.parse("postgres:16-alpine"))
                        .withDatabaseName("lookbook_test")
                        .withUsername("test")
                        .withPassword("test")
                        .withReuse(true); // Enable container reuse

        @BeforeAll
        static void beforeAll() {
                postgres.start();
                // Wait for container to be ready
                postgres.waitingFor(org.testcontainers.containers.wait.strategy.Wait.forListeningPort());
        }

        @DynamicPropertySource
        static void postgresProperties(DynamicPropertyRegistry registry) {
                registry.add("spring.datasource.url", postgres::getJdbcUrl);
                registry.add("spring.datasource.username", postgres::getUsername);
                registry.add("spring.datasource.password", postgres::getPassword);
                registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
                registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
                registry.add("spring.flyway.enabled", () -> "false");

                // Transaction management properties
                registry.add("spring.jpa.properties.hibernate.connection.provider_disables_autocommit", () -> "false");
                registry.add("spring.jpa.properties.hibernate.connection.autocommit", () -> "true");
                registry.add("spring.jpa.properties.hibernate.current_session_context_class",
                                () -> "org.springframework.orm.hibernate5.SpringSessionContext");
                registry.add("spring.jpa.properties.hibernate.transaction.jta.platform",
                                () -> "org.hibernate.engine.transaction.jta.platform.internal.NoJtaPlatform");
                registry.add("spring.jpa.properties.hibernate.transaction.factory_class",
                                () -> "org.hibernate.transaction.JDBCTransactionFactory");
                registry.add("spring.jpa.properties.hibernate.connection.handling_mode",
                                () -> "DELAYED_ACQUISITION_AND_HOLD");
                registry.add("spring.jpa.properties.hibernate.connection.release_mode", () -> "AFTER_TRANSACTION");

                // Additional connection properties
                registry.add("spring.datasource.hikari.maximum-pool-size", () -> "5");
                registry.add("spring.datasource.hikari.minimum-idle", () -> "1");
                registry.add("spring.datasource.hikari.idle-timeout", () -> "300000");
                registry.add("spring.datasource.hikari.connection-timeout", () -> "20000");
                registry.add("spring.datasource.hikari.max-lifetime", () -> "1200000");

                // EntityManager properties
                registry.add("spring.jpa.properties.hibernate.connection.provider_supports_autocommit", () -> "true");
                registry.add("spring.jpa.properties.hibernate.connection.provider_supports_auto_commit", () -> "true");
                registry.add("spring.jpa.properties.hibernate.connection.provider_supports_auto_commit_override",
                                () -> "true");
        }
}