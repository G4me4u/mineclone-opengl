package mineclone.common.net.handler;

import mineclone.common.net.INetworkConnection;
import mineclone.common.net.handler.IPacketHandler;
import mineclone.common.net.NetworkManager;

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
