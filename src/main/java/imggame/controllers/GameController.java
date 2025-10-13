package imggame.controllers;

import java.sql.Connection;

public class GameController {
	private Connection connection;
	private UserController userController;

	public GameController(Connection connection) {
		this.connection = connection;
		this.userController = UserController.getInstance();
	}

	public void getRandomImageSet() {

	}
}
