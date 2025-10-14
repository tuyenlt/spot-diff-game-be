package imggame.controllers;

import java.sql.Connection;

import imggame.database.Database;

public class GameController {
	private Connection connection;
	private UserController userController;

	public GameController() {
		this.connection = Database.getConnection();
		this.userController = new UserController();
	}

	public void getRandomImageSet() {

	}
}
