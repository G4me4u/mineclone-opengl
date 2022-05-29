package mineclone.server;

import mineclone.server.net.ServerNetworkSocket;

public class DedicatedMinecloneServer extends MinecloneServer {

	private static final int SERVER_PORT = 2202;
	
	private ServerNetworkSocket socket;
	
	@Override
	protected void init() {
		socket = new ServerNetworkSocket(this, networkManager);
		try {
			socket.bind(SERVER_PORT);
		} catch (Exception e) {
			throw new RuntimeException("Unable to bind socket.", e);
		}
	}

	@Override
	protected void stop() {
		socket.close();
	}

	public static void main(String[] args) {
		new DedicatedMinecloneServer().start();
	}
}
