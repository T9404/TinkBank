package com.academy.fintech.origination.integration.config;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;

public class KafkaPostgresTestContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:15-alpine")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    KafkaContainer kafkaContainer = new KafkaContainer();

    @Override
    public void initialize(ConfigurableApplicationContext ctx) {
        postgreSQLContainer.start();
        kafkaContainer.start();

        var jdbcUrl = postgreSQLContainer.getJdbcUrl();
        var updatedUrl = jdbcUrl.replace("jdbc:", "jdbc:tc:");

        TestPropertyValues.of(
                "spring.datasource.url=" + updatedUrl,
                "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                "spring.datasource.password=" + postgreSQLContainer.getPassword(),
                "spring.kafka.bootstrap-servers=" + kafkaContainer.getBootstrapServers()
        ).applyTo(ctx.getEnvironment());
    }
}
