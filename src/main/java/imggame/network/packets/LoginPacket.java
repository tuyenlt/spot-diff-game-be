package imggame.network.packets;

import java.io.Serializable;

public class LoginPacket implements Serializable {
	public String username;
	public String password;

	public LoginPacket(String username, String password) {
		this.username = username;
		this.password = password;

	}
}
