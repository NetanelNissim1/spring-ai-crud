package com.example.aicrud.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.net.URI;

@Configuration
public class DataSourceConfig {

    private static final Logger log = LoggerFactory.getLogger(DataSourceConfig.class);

    @Value("${spring.datasource.url:jdbc:postgresql://localhost:5432/productdb}")
    private String rawUrl;

    @Value("${spring.datasource.username:postgres}")
    private String defaultUsername;

    @Value("${spring.datasource.password:postgres}")
    private String defaultPassword;

    @Value("${spring.datasource.driver-class-name:org.postgresql.Driver}")
    private String driverClassName;

    @Bean
    @Primary
    public DataSource dataSource() {
        String dbUrl = System.getenv("DATABASE_URL");
        if (dbUrl == null || dbUrl.isBlank()) {
            dbUrl = System.getenv("SPRING_DATASOURCE_URL");
        }
        if (dbUrl == null || dbUrl.isBlank()) {
            dbUrl = rawUrl;
        }

        HikariConfig config = new HikariConfig();
        config.setDriverClassName(driverClassName);

        String username = defaultUsername;
        String password = defaultPassword;
        String jdbcUrl = dbUrl;

        try {
            // Automatically handle Render / Heroku postgresql://user:pass@host:port/db format
            if (dbUrl.startsWith("postgresql://") || dbUrl.startsWith("postgres://")) {
                URI uri = new URI(dbUrl);
                String userInfo = uri.getUserInfo();
                if (userInfo != null && userInfo.contains(":")) {
                    String[] parts = userInfo.split(":", 2);
                    username = parts[0];
                    password = parts[1];
                }
                String host = uri.getHost();
                int port = uri.getPort() == -1 ? 5432 : uri.getPort();
                String path = uri.getPath();
                jdbcUrl = "jdbc:postgresql://" + host + ":" + port + path;
            } else if (!dbUrl.startsWith("jdbc:")) {
                jdbcUrl = "jdbc:" + dbUrl;
            }
        } catch (Exception e) {
            log.warn("Could not parse DB URI as structured URI, using fallback: {}", e.getMessage());
            jdbcUrl = dbUrl.startsWith("jdbc:") ? dbUrl : "jdbc:" + dbUrl;
        }

        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);

        log.info("Initialized Production DataSource: {} (User: {})", jdbcUrl, username);
        return new HikariDataSource(config);
    }
}
