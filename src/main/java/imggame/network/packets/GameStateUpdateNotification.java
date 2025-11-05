package imggame.network.packets;

import imggame.game.Player;
import imggame.game.GameRoom.GameState;

public class GameStateUpdateNotification extends BasePacket {
	private static final long serialVersionUID = 1L;

	public String roomId;
	public GameState gameState;
	public Player player1;
	public Player player2;

	
	public GameStateUpdateNotification(String roomId, GameState gameState, Player player1, Player player2) {
		this.roomId = roomId;
		this.gameState = gameState;
		this.player1 = player1;
		this.player2 = player2;
	}

	@Override
	public String getType() {
		return "GAME_STATE_UPDATE";
	}
}
