package imggame.network.packets;

import java.io.Serializable;

public class GameMatchingPacket implements Serializable {
	public int playerId;

	public GameMatchingPacket(int playerId) {
		this.playerId = playerId;
	}
}
