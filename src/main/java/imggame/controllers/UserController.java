package imggame.controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import imggame.database.Database;
import imggame.models.User;

public class UserController {
	private Connection connection;

	public UserController() {
		this.connection = Database.getConnection();
	}

	public boolean isUsernameOrEmailTaken(String username, String email) throws SQLException {
		String query = "SELECT COUNT(*) FROM users WHERE username = ? OR email = ?";
		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setString(1, username);
			stmt.setString(2, email);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) > 0;
			}
		}
		return false;
	}

	// Add user-related methods here
	public boolean createUser(User user) {
		try {
			if (isUsernameOrEmailTaken(user.getUsername(), user.getEmail())) {
				return false;
			}
			String query = "INSERT INTO users (id, username, email, password, score, elo) VALUES (?, ?, ?, ?, ?, ?)";
			try (PreparedStatement statement = connection.prepareStatement(query)) {
				statement.setString(1, user.getId());
				statement.setString(2, user.getUsername());
				statement.setString(3, user.getEmail());
				statement.setString(4, user.getPassword());
				statement.setInt(5, user.getScore());
				statement.setInt(6, user.getElo());
				statement.executeUpdate();
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
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
