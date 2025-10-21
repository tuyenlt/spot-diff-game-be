package imggame.network.packets;

import java.io.Serializable;

public class ErrorPacket implements Serializable {
	public String message;

	public ErrorPacket(String message) {
		this.message = message;
	}
}
