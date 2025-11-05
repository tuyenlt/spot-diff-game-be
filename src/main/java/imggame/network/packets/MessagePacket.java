package imggame.network.packets;

public class MessagePacket extends BasePacket {
	public String message;
	public String context;

	public MessagePacket(String message) {
		this.message = message;
		this.context = null;
	}

	public MessagePacket(String message, String context) {
		this.message = message;
		this.context = context;
	}

	@Override
	public String getType() {
		return "MESSAGE";
	}

}
