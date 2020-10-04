package minecraft.common.net.handler;

import minecraft.common.net.packet.c2s.PlayerJoinC2SPacket;

public interface IServerPacketHandler extends IPacketHandler {

	public void onPlayerJoin(PlayerJoinC2SPacket packet);
	
}
