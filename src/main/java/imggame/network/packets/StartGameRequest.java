package imggame.network.packets;

public class StartGameRequest extends BasePacket {
	private static final long serialVersionUID = 1L;

	private String roomId;
	private int userId;

	public StartGameRequest(String roomId, int userId) {
		this.roomId = roomId;
		this.userId = userId;
	}

	public int getUserId() {
		return userId;
	}

	public String getRoomId() {
		return roomId;
	}

	@Override
	public String getType() {
		return "START_GAME";
	}
}
