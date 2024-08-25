package com.rc.mentorship.workplace_reservation.container;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.Scope;
import liquibase.command.CommandScope;
import liquibase.database.Database;
import liquibase.database.DatabaseConnection;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;

@Testcontainers
public class PostgresTestContainer {
    @Container
    private static final PostgreSQLContainer<?> postgreSqlContainer =
            new PostgreSQLContainer<>("postgres:13")
                    .withDatabaseName("test-db")
                    .withUsername("admin")
                    .withPassword("root");
//                    .setPortBindings(Collections.singletonList("5433:5432"));

//    @BeforeAll
//    static void beforeAll() {
//        postgreSqlContainer.start();
//
//        try (Connection connection = DriverManager.getConnection(
//                postgreSqlContainer.getJdbcUrl(),
//                postgreSqlContainer.getUsername(),
//                postgreSqlContainer.getPassword())) {
//            JdbcConnection jdbcConnection = new JdbcConnection(connection);
//
//            Liquibase liquibase = new Liquibase("db/changelog/db.changelog-master.yaml",
//                    new ClassLoaderResourceAccessor(), jdbcConnection);
//
//            // Update the database with the changesets
//            liquibase.update(null, (LabelExpression) null);
//        } catch (LiquibaseException | SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }

    static {
        postgreSqlContainer.start();
//        try {
//            Scope.child(Scope.Attr.resourceAccessor, new ClassLoaderResourceAccessor(), () -> {
//                CommandScope update = new CommandScope("update");
//
//                update.addArgumentValue("changelogFile", "db/changelog/db.changelog-master.yaml");
//                update.addArgumentValue("url", postgreSqlContainer.getJdbcUrl());
//                update.addArgumentValue("username", postgreSqlContainer.getUsername());
//                update.addArgumentValue("password", postgreSqlContainer.getPassword());
//
//                update.execute();
//            });
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }

    @DynamicPropertySource
    static void registerProperty(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSqlContainer::getUsername);
        registry.add("spring.datasource.password", postgreSqlContainer::getPassword);
    }
}
