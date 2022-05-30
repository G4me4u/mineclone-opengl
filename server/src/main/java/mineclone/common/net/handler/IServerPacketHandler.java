package mineclone.common.net.handler;

import mineclone.common.net.packet.c2s.PlayerJoinC2SPacket;
import mineclone.common.net.packet.universal.StateChangeUPacket;

public interface IServerPacketHandler extends IPacketHandler {

	public void onPlayerJoin(PlayerJoinC2SPacket packet);

	public void onStateChange(StateChangeUPacket packet);
	
}
