package imggame.network.packets;

public class LoginPacket {
	public String username;
	public String password;

	public LoginPacket(String username, String password) {
		this.username = username;
		this.password = password;

	}
}
