package com.lookbook.config;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
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
                .validateOnMigrate(true)
                .load();
    }

    /**
     * Creates a Flyway cleaner bean for the test profile.
     * WARNING: This will wipe the database! Only for test environment.
     * 
     * @param dataSource The database data source
     * @return A Flyway instance configured for clean and migrate
     */
    @Bean(name = "flywayTest", initMethod = "migrate")
    @Profile("test")
    public Flyway flywayTest(DataSource dataSource) {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .baselineOnMigrate(true)
                .locations("classpath:db/migration", "classpath:db/testdata")
                .load();

        // Clean the database first in test profile
        flyway.clean();

        return flyway;
    }
}