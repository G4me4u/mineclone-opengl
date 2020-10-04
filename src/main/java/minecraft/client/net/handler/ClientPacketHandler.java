package minecraft.client.net.handler;

import minecraft.common.net.INetworkConnection;
import minecraft.common.net.NetworkManager;
import minecraft.common.net.handler.IClientPacketHandler;
import minecraft.common.net.packet.s2c.WorldChunkS2CPacket;
import minecraft.server.net.handler.AbstractPacketHandler;

public class ClientPacketHandler extends AbstractPacketHandler implements IClientPacketHandler {

	public ClientPacketHandler(INetworkConnection connection, NetworkManager manager) {
		super(connection, manager);
	}

	@Override
	public void onWorldChunk(WorldChunkS2CPacket packet) {
		
	}
}
