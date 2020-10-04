package mineclone.client.net.handler;

import mineclone.common.net.INetworkConnection;
import mineclone.common.net.handler.AbstractPacketHandler;
import mineclone.common.net.handler.IClientPacketHandler;
import mineclone.common.net.packet.s2c.WorldChunkS2CPacket;
import mineclone.common.net.NetworkManager;

public class ClientPacketHandler extends AbstractPacketHandler implements IClientPacketHandler {

	public ClientPacketHandler(INetworkConnection connection, NetworkManager manager) {
		super(connection, manager);
	}

	@Override
	public void onWorldChunk(WorldChunkS2CPacket packet) {
		
	}
}
