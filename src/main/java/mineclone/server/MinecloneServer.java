package mineclone.server;

import mineclone.server.net.ServerNetworkManager;

public class MinecloneServer {

	public static void main(String[] args) throws Exception {
		ServerNetworkManager server = new ServerNetworkManager();
		server.bind(8080);
		
		while (server.isActive())
			Thread.sleep(1000L);
		
		server.close();
	}
}
