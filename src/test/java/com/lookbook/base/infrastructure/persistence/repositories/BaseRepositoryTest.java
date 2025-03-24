package com.lookbook.base.infrastructure.persistence.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
public abstract class BaseRepositoryTest {

    @BeforeEach
    void setUp() {
        // Add any common setup logic here
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        // Configure PostgreSQL database
        registry.add("spring.datasource.url",
                () -> "jdbc:postgresql://localhost:5432/lookbook_test");
        registry.add("spring.datasource.username", () -> "postgres");
        registry.add("spring.datasource.password", () -> "postgres");
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");

        // Configure JPA/Hibernate
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "none");
        registry.add("spring.jpa.show-sql", () -> "true");
        registry.add("spring.jpa.properties.hibernate.format_sql", () -> "true");
        registry.add("spring.jpa.properties.hibernate.dialect", () -> "org.hibernate.dialect.PostgreSQLDialect");

        // Configure Flyway
        registry.add("spring.flyway.enabled", () -> "true");
        registry.add("spring.flyway.baseline-on-migrate", () -> "true");
        registry.add("spring.flyway.validate-on-migrate", () -> "false");
        registry.add("spring.flyway.clean-disabled", () -> "false");
        registry.add("spring.flyway.schemas", () -> "public");
        registry.add("spring.flyway.locations", () -> "classpath:db/migration,classpath:db/testdata");
    }
}