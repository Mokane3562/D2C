package com.d2c.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class SQLHandler {
	private String host;
	private String port;
	private Properties connectionProperties;
	private Connection connection;
	private static String example_sql = "SELECT ? FROM SOME_TABLE WHERE SOME_COLUMN = ?";
	private PreparedStatement example_statement;

	public SQLHandler() throws SQLException {
		// TODO replace placeholder string literals with real host and port
		this.host = "the host";
		this.port = "port number";
		this.connectionProperties = new Properties();
		// TODO replace placeholder string literals with real user and password
		this.connectionProperties.put("user", "user_name");
		this.connectionProperties.put("password", "the_password");
		this.connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/", connectionProperties);
		this.example_statement = connection.prepareStatement(example_sql);
	}

	public ResultSet getExample(String column, String value) throws SQLException {
		this.example_statement.setString(0, column);
		this.example_statement.setString(1, value);
		return this.example_statement.executeQuery();
	}

}
