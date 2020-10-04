package minecraft.server.net.handler;

import minecraft.common.net.INetworkConnection;
import minecraft.common.net.NetworkManager;
import minecraft.common.net.handler.IPacketHandler;

public abstract class AbstractPacketHandler implements IPacketHandler {

	protected final INetworkConnection connection;
	protected final NetworkManager manager;
	
	public AbstractPacketHandler(INetworkConnection connection, NetworkManager manager) {
		this.connection = connection;
		this.manager = manager;
	}
	
	public INetworkConnection getConnection() {
		return connection;
	}
	
	public NetworkManager getNetworkManager() {
		return manager;
	}
}
