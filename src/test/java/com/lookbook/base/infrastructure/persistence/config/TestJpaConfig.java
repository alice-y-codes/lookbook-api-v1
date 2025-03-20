package com.lookbook.base.infrastructure.persistence.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;

@Configuration
@EnableJpaRepositories(basePackages = "com.lookbook.base.infrastructure.persistence.repositories")
@EntityScan(basePackages = "com.lookbook.base.infrastructure.persistence.entities")
@ActiveProfiles("test")
public class TestJpaConfig {
}