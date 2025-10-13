package imggame.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import io.github.cdimascio.dotenv.Dotenv;

public class Database {
	public static Connection conn;

	public static void init() {
		Dotenv dotenv = Dotenv.load();

		String dbHost = dotenv.get("DB_HOST");
		String dbPort = dotenv.get("DB_PORT");
		String dbName = dotenv.get("MYSQL_DATABASE");
		String dbUser = dotenv.get("MYSQL_USER");
		String dbPassword = dotenv.get("MYSQL_PASSWORD");

		String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName
				+ "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

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
