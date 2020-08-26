package mineclone.server.net;

import mineclone.common.net.NetworkConnection;
import mineclone.common.net.packet.IServerPacketHandler;

public class ServerPacketHandler implements IServerPacketHandler {

	private final NetworkConnection connection;
	
	private ServerPacketHandler(NetworkConnection connection) {
		this.connection = connection;
	}
	
	public NetworkConnection getConnection() {
		return connection;
	}
}
