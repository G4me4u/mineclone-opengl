package mineclone.client;

import mineclone.client.net.IntegratedNetworkConnection;
import mineclone.common.net.INetworkConnection;
import mineclone.common.net.handler.IPacketHandler;
import mineclone.server.MinecloneServer;
import mineclone.server.net.handler.ServerPacketHandler;

public class IntegratedMinecloneServer extends MinecloneServer {

	public IntegratedMinecloneServer() {
	}
	
	public void startAsync() throws InterruptedException {
		Thread thread = new Thread(this::start);
		thread.setName("Integrated Server Thread");
		
		synchronized (this) {
			thread.start();
			// Wait for the server thread to start
			wait();
		}
	}
	
	@Override
	protected void init() {
		synchronized (this) {
			notifyAll();
		}
	}
	
	@Override
	protected void stop() {
	}
	
	public INetworkConnection connect(IntegratedNetworkConnection connection) {
		networkManager.addConnection(connection);
		IPacketHandler handler = new ServerPacketHandler(this, connection);
		return new IntegratedNetworkConnection(handler);
	}
}
