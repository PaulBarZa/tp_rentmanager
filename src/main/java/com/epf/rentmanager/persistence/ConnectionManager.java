package com.epf.rentmanager.persistence;

import java.sql.Connection;
import java.sql.SQLException;

import org.h2.jdbcx.JdbcDataSource;
import org.springframework.stereotype.Service;

@Service
public class ConnectionManager {

	private static final String DB_CONNECTION = "jdbc:h2:~/RentManagerDatabase";
	private static final String DB_USER = "";
	private static final String DB_PASSWORD = "";
	private static JdbcDataSource datasource = null;

	/**
	 * Generate a connection with database
	 * 
	 * @return connection
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException {
		if (datasource == null) {
			datasource = new JdbcDataSource();
			datasource.setURL(DB_CONNECTION);
			datasource.setUser(DB_USER);
			datasource.setPassword(DB_PASSWORD);
		}
		return datasource.getConnection();
	}

}