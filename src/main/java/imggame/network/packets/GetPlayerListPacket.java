package imggame.network.packets;

import java.io.Serializable;

public class GetPlayerListPacket implements Serializable {
	public int pageSize;
	public int offset;
	public Boolean isDESC;

	public GetPlayerListPacket(int pageSize, int offset, Boolean isDESC) {
		this.pageSize = pageSize;
		this.offset = offset;
		this.isDESC = isDESC;
	}
}
