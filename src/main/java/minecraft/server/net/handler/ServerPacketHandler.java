package minecraft.server.net.handler;

import minecraft.common.net.INetworkConnection;
import minecraft.common.net.handler.IServerPacketHandler;
import minecraft.common.net.packet.c2s.PlayerJoinC2SPacket;

public class ServerPacketHandler extends AbstractPacketHandler implements IServerPacketHandler {

	public ServerPacketHandler(INetworkConnection connection) {
		super(connection, null);
	}

	@Override
	public void onPlayerJoin(PlayerJoinC2SPacket packet) {
	}
}
