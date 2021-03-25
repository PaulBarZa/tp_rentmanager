package com.epf.rentmanager.dao.utils;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.epf.rentmanager.persistence.ConnectionManager;

import org.springframework.stereotype.Service;

@Service
public class DaoOutils {

    /**
     * Init a prepared statement with a query
     * 
     * @param query
     * @param shouldGenerateKeys
     * @return return the completed prepared statement
     */
    public PreparedStatement initPreparedStatement(String query, boolean shouldGenerateKeys) throws SQLException {
        Connection connection = new ConnectionManager().getConnection();
        if (shouldGenerateKeys) {
            return connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        } else {
            return connection.prepareStatement(query);
        }
    }

}
