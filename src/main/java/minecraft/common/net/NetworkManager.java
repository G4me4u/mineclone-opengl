package minecraft.common.net;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import minecraft.common.IResource;

public class NetworkManager implements IResource {

	protected final NetworkSide side;
	
	protected final List<INetworkConnection> connections;
	
	protected NetworkManager(NetworkSide side) {
		this.side = side;
		
		this.connections = new ArrayList<>();
	}

	public void addConnection(INetworkConnection connection) {
		connections.add(connection);
	}

	public void removeConnection(INetworkConnection connection) {
		connections.remove(connection);
	}
	
	public Collection<INetworkConnection> getConnections() {
		return connections;
	}

	public NetworkSide getSide() {
		return side;
	}

	@Override
	public void close() {
	}
}
