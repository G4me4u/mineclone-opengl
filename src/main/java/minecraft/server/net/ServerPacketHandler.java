package minecraft.server.net;

import minecraft.common.net.NetworkConnection;
import minecraft.common.net.packet.IServerPacketHandler;

public class ServerPacketHandler implements IServerPacketHandler {

	private final NetworkConnection connection;
	
	private ServerPacketHandler(NetworkConnection connection) {
		this.connection = connection;
	}
	
	public NetworkConnection getConnection() {
		return connection;
	}
}
