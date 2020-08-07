package minecraft.common.net;

import minecraft.common.IResource;

public abstract class NetworkManager implements IResource {

	protected final NetworkSide side;
	
	protected NetworkManager(NetworkSide side) {
		this.side = side;
	}
	
	public NetworkSide getSide() {
		return side;
	}
}
