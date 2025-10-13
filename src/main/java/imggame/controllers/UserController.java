package imggame.controllers;

import imggame.models.User;
import imggame.database.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class UserController {
	private static UserController instance;
	private Connection connection;

	private UserController(Connection connection) {
		this.connection = connection;
	}

	public static UserController getInstance() {
		if (instance == null) {
			instance = new UserController(Database.getConnection());
		}
		return instance;
	}

	// Add user-related methods here
	public void createUser(User user) {
		try {
			String query = "INSERT INTO users (username, email, password, score, elo) VALUES (?, ?, ?, ?, ?)";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, user.getUsername());
			statement.setString(2, user.getEmail());
			statement.setString(3, user.getPassword());
			statement.setInt(4, user.getScore());
			statement.setInt(5, user.getElo());
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error creating user", e);
		}
	}

	public boolean checkLogin(String username, String password) {
		try {
			String query = "SELECT * FROM users WHERE username = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, username);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				String storedPassword = resultSet.getString("password");
				if (storedPassword.equals(password)) {
					return true; // Successful login
				} else {
					return false; // Incorrect password
				}
			} else {
				return false; // User not found
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error during login", e);
		}

	}

	public User getUserById(String id) {
		try {
			String query = "SELECT * FROM users WHERE id = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, id);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				return new User(
						resultSet.getString("id"),
						resultSet.getString("username"),
						resultSet.getString("email"),
						resultSet.getString("password"),
						resultSet.getInt("score"),
						resultSet.getInt("elo"));
			} else {
				return null; // User not found
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error fetching user by ID", e);
		}
	}

	public List<User> getUserRankedList(int page_size, int offset, boolean isDESC) {
		List<User> userList = new ArrayList<>();
		try {
			String query = "SELECT * FROM users ORDER BY elo DESC LIMIT ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, page_size);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				userList.add(new User(
						resultSet.getString("id"),
						resultSet.getString("username"),
						resultSet.getString("email"),
						resultSet.getString("password"),
						resultSet.getInt("score"),
						resultSet.getInt("elo")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error fetching ranked user list", e);
		}
		return userList;
	}
}
