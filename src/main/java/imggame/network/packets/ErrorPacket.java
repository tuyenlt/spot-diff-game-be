package imggame.network.packets;

public class ErrorPacket {
	public String message;

	public ErrorPacket(String message) {
		this.message = message;
	}
}
