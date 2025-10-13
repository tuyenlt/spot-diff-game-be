package imggame.network.packets;

public class InvitePacket {
	public int senderId;
	public int receiverId;

	public InvitePacket(int senderId, int receiverId) {
		this.senderId = senderId;
		this.receiverId = receiverId;
	}
}
