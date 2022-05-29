package mineclone.common.net;

import mineclone.common.net.handler.IPacketHandler;
import mineclone.common.net.packet.IPacket;

public class IntegratedNetworkConnection implements INetworkConnection {

	private IPacketHandler endpoint;
	
	public IntegratedNetworkConnection() {
		this(null);
	}

	public IntegratedNetworkConnection(IPacketHandler endpoint) {
		this.endpoint = endpoint;
	}
	
	public void setEndpoint(IPacketHandler endpoint) {
		if (this.endpoint != null)
			throw new IllegalStateException("Endpoint is already set!");

		this.endpoint = endpoint;
	}
	
	@Override
	public void send(IPacket packet) {
		if (endpoint != null)
			endpoint.onPacket(packet);
	}

	@Override
	public boolean isConnected() {
		return (endpoint != null);
	}

	@Override
	public void close() {
		endpoint = null;
	}
}
