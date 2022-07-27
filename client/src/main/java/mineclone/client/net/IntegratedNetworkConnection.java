package mineclone.client.net;

import mineclone.common.net.INetworkConnection;
import mineclone.common.net.handler.IPacketHandler;
import mineclone.common.net.packet.IPacket;

public class IntegratedNetworkConnection implements INetworkConnection {

	private IPacketHandler endpoint;
	private boolean closed;
	
	public IntegratedNetworkConnection() {
		this(null);
	}

	public IntegratedNetworkConnection(IPacketHandler endpoint) {
		this.endpoint = endpoint;
		closed = false;
	}
	
	public void setEndpoint(IPacketHandler endpoint) {
		if (this.endpoint != null)
			throw new IllegalStateException("Endpoint is already set!");
		if (closed)
			throw new IllegalStateException("Connection is closed!");

		this.endpoint = endpoint;
	}
	
	@Override
	public void send(IPacket packet) {
		if (endpoint != null)
			endpoint.onPacket(packet);
	}

	@Override
	public boolean isOpen() {
		return !closed;
	}

	@Override
	public void close() {
		endpoint = null;
		closed = true;
	}
}
