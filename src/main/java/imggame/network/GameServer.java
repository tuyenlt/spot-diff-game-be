package imggame.network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import imggame.controllers.UserController;
import imggame.engine.GameState;
import imggame.engine.ImageSet;
import imggame.engine.Player;
import imggame.models.User;

public class GameServer {
	private UserController userController;
	private GameState gameState;
	private Player player1;
	private Player player2;

	public GameServer(Player p1, Player p2){
		this.player1 = p1;
		this.player2 = p2;
		this.gameState = new GameState(p1, p2, new ImageSet());
		this.userController = new UserController();
	}
}
