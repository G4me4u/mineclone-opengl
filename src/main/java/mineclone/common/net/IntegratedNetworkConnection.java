package mineclone.common.net;

import mineclone.common.net.packet.IPacket;

public class IntegratedNetworkConnection implements INetworkConnection {

	private IntegratedEndpoint endpoint;
	
	public IntegratedNetworkConnection() {
		this(null);
	}

	public IntegratedNetworkConnection(IntegratedEndpoint endpoint) {
		this.endpoint = endpoint;
	}
	
	public void setEndpoint(IntegratedEndpoint endpoint) {
		if (endpoint != null)
			throw new IllegalStateException("Endpoint is already set!");

		this.endpoint = endpoint;
	}
	
	@Override
	public void send(IPacket<?> packet) {
		endpoint.handlePacket(packet);
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
