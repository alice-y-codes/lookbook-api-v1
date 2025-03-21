package com.lookbook.user.infrastructure.persistence.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lookbook.user.application.ports.repositories.UserRepository;
import com.lookbook.user.infrastructure.persistence.repositories.UserJpaRepository;
import com.lookbook.user.infrastructure.persistence.repositories.UserRepositoryAdapter;

/**
 * Configuration for User persistence layer.
 * Creates beans for the persistence adapters.
 */
@Configuration
public class UserJpaConfig {

    /**
     * Creates a UserRepository bean that wraps the Spring Data JPA repository.
     *
     * @param jpaRepository The JPA repository to wrap
     * @return A UserRepository implementation
     */
    @Bean
    public UserRepository userRepository(UserJpaRepository jpaRepository) {
        return new UserRepositoryAdapter(jpaRepository);
    }
}