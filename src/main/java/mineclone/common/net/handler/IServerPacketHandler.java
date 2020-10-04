package mineclone.common.net.handler;

import mineclone.common.net.packet.c2s.PlayerJoinC2SPacket;

public interface IServerPacketHandler extends IPacketHandler {

	public void onPlayerJoin(PlayerJoinC2SPacket packet);
	
}
