package imggame.network.packets;

import java.io.Serializable;

public class RegisterPacket implements Serializable {
	public String username;
	public String email;
	public String password;

	public RegisterPacket(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
	}
}
