package mineclone.server.net.handler;

import mineclone.common.net.INetworkConnection;
import mineclone.common.net.handler.AbstractPacketHandler;
import mineclone.common.net.handler.IServerPacketHandler;
import mineclone.common.net.packet.c2s.PlayerJoinC2SPacket;

public class ServerPacketHandler extends AbstractPacketHandler implements IServerPacketHandler {

	public ServerPacketHandler(INetworkConnection connection) {
		super(connection, null);
	}

	@Override
	public void onPlayerJoin(PlayerJoinC2SPacket packet) {
	}
}
