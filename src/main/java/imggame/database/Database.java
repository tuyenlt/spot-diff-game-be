package imggame.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import imggame.config.AppConfig;

public class Database {
	public static Connection conn;

	public static void init() {
		String url = AppConfig.getDatabaseUrl();
		String dbUser = AppConfig.MYSQL_USER;
		String dbPassword = AppConfig.MYSQL_PASSWORD;

		try {
			conn = DriverManager.getConnection(url, dbUser, dbPassword);
			System.out.println("Database connected successfully.");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error connecting to the database", e);
		}
	}

	public static Connection getConnection() {
		return conn;
	}
}
