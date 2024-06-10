package net.savantly.nexus.flow.dom.connections.datasource;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.log4j.Log4j2;
import net.savantly.nexus.flow.dom.connections.jdbcConnection.JdbcConnection;

@Log4j2
public class DatasourceFactory {

    public DataSource createFromJdbcConnection(JdbcConnection jdbcConnection) {
        log.info("creating datasource for url: {}", jdbcConnection.getJdbcUrl());
        
        String jdbcUrl = jdbcConnection.getJdbcUrl();

        HikariConfig config = new HikariConfig();
        config.setUsername(jdbcConnection.getUsername());
        config.setPassword(jdbcConnection.getRawPassword());
        config.setDriverClassName(jdbcConnection.getDriverClassName());
        config.setJdbcUrl(jdbcUrl);

        config.setMaximumPoolSize(2); // Adjust based on your needs
        config.setMinimumIdle(1);
        config.setConnectionTimeout(5 * 1000); // 5 seconds
        config.setMaxLifetime(30 * 1000); // 30 seconds
        config.setIdleTimeout(10 * 1000); // 10 seconds
        config.setPoolName("HikariPool-" + jdbcConnection.getName());
        log.info("Data source created: " + jdbcConnection.getName());

        HikariDataSource dataSource = new HikariDataSource(config);
        return dataSource;
    }
    
}
