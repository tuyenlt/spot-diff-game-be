package imggame.network.packets;

import java.io.Serializable;

public class InvitePacket implements Serializable {
	public int senderId;
	public int receiverId;

	public InvitePacket(int senderId, int receiverId) {
		this.senderId = senderId;
		this.receiverId = receiverId;
	}
}
