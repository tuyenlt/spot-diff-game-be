package imggame;

import imggame.database.Database;
import imggame.network.GameServer;

public class Main {
	public static void main(String[] args) {
		Database.init();
		GameServer server = new GameServer();
		server.listen();
	}
}