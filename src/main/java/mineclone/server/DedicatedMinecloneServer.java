package mineclone.server;

import mineclone.common.util.DebugUtil;
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
		// TODO: find a better solution for this, thanks :-)
		DebugUtil.RUNNING_SERVER = true;
		
		new DedicatedMinecloneServer().start();
	}
}
