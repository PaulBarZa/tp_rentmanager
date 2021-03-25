package com.epf.rentmanager.configuration;

import java.sql.Connection;
import java.sql.SQLException;

import com.epf.rentmanager.persistence.ConnectionManager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({ "com.epf.rentmanager.service", "com.epf.rentmanager.dao", "com.epf.rentmanager.persistence" })
public class AppConfiguration {

    /**
     * Return a jbdbc Connection
     * 
     * @return
     * @throws SQLException
     */
    @Bean
    public Connection jdbcConnection() throws SQLException {
        return new ConnectionManager().getConnection();
    }
}
