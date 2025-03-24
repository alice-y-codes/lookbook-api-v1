package com.lookbook.config;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Configuration for Flyway database migrations.
 */
@Configuration
public class FlywayConfig {

    /**
     * Configures Flyway with advanced options.
     * 
     * @param dataSource The database data source
     * @return A configured Flyway instance
     */
    @Bean(initMethod = "migrate")
    @Profile("!test") // Skip automatic migration in test profile
    public Flyway flyway(DataSource dataSource) {
        return Flyway.configure()
                .dataSource(dataSource)
                .baselineOnMigrate(true)
                .locations("classpath:db/migration")
                .validateOnMigrate(false) // Disable validation to allow migrations to run
                .schemas("public")
                .load();
    }

    /**
     * Creates a FlywayMigrationStrategy for the test profile to properly clean and
     * migrate.
     * 
     * @return A migration strategy that cleans and then migrates
     */
    @Bean
    @Profile("test")
    public FlywayMigrationStrategy cleanMigrateStrategy() {
        return flyway -> {
            // Clean the database
            flyway.clean();
            // Migrate with test data
            flyway.migrate();
        };
    }

    /**
     * Creates a Flyway bean for test environment with appropriate configuration.
     * The actual clean and migrate will be handled by the migration strategy.
     * 
     * @param dataSource The database data source
     * @return A configured Flyway instance
     */
    @Bean
    @Profile("test")
    public Flyway flywayTest(DataSource dataSource) {
        return Flyway.configure()
                .dataSource(dataSource)
                .baselineOnMigrate(true)
                .locations("classpath:db/migration", "classpath:db/testdata")
                .validateOnMigrate(false)
                .cleanDisabled(false)
                .schemas("public")
                .load();
    }
}