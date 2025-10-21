package imggame;

import imggame.database.Database;
import imggame.network.GameNetwork;

public class Main {
	public static void main(String[] args) {
		Database.init();
		GameNetwork server = new GameNetwork();
		server.listen();
	}
}