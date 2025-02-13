package org.sotil.kuarkus.demo.infrastructure.config;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DatabaseConfig {

    @ConfigProperty(name = "quarkus.datasource.jdbc.url")
    String jdbcUrl;

    @ConfigProperty(name = "quarkus.datasource.username")
    String username;

    @ConfigProperty(name = "quarkus.datasource.password")
    String password;

    public void printConfig() {
        System.out.println("JDBC URL: " + jdbcUrl);
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
    }

}
