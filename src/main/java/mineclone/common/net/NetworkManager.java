package mineclone.common.net;

import java.util.Collection;

import mineclone.common.IResource;

public abstract class NetworkManager implements IResource {

	protected final NetworkSide side;
	
	protected NetworkManager(NetworkSide side) {
		this.side = side;
	}
	
	public abstract Collection<NetworkConnection> getConnections();

	public abstract boolean isActive();
	
	public NetworkSide getSide() {
		return side;
	}
}
